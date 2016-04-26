package greglife.item.tool;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import buildcraft.api.tools.IToolWrench;
import cofh.api.energy.IEnergyContainerItem;
import greglife.GLContent;
import greglife.GregLife;
import greglife.util.ItemNBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLever;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class ItemWrench extends Item implements IToolWrench
{
	private final Set<Class<? extends Block>> shiftRotations = new HashSet<Class<? extends Block>>();
	private final Set<Class<? extends Block>> blacklistedRotations = new HashSet<Class<? extends Block>>();

	public ItemWrench()
	{
		super();

		this.setFull3D();
		this.setMaxStackSize(1);
		this.shiftRotations.add(BlockLever.class);
		this.shiftRotations.add(BlockButton.class);
		this.shiftRotations.add(BlockChest.class);
		this.blacklistedRotations.add(BlockBed.class);
		this.setHarvestLevel("Wrench", 3);
		this.setUnlocalizedName("gl.itemWrench");
		this.setMaxDamage(5);
		this.setCreativeTab(GregLife.GLTab);
		this.setTextureName("greglife:wrench");
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool){
		list.add("The Wrench.");
		list.add(EnumChatFormatting.DARK_RED + "UnCharged. Please Charge!");
		list.add("");
		list.add("Durability: " + (10 - this.getDamage(stack) - 5) + " / " + this.getMaxDamage(stack));

		if(GregLife.proxy.isShiftKeyDown()){
			list.add("-----------------------");
		}else{
			list.add(EnumChatFormatting.DARK_PURPLE + "§o--Press Shift for Info--");
		}
	}

	@SuppressWarnings("unused")
	private boolean isClass(Set<Class<? extends Block>> set, Class<? extends Block> cls)
	{
		for (Class<? extends Block> shift : set)
			if (shift.isAssignableFrom(cls))
				return true;
		return false;
	}

	@Override
	public boolean canWrench(EntityPlayer player, int x, int y, int z)
	{
		return true;
	}

	@Override
	public void wrenchUsed(EntityPlayer player, int x, int y, int z)
	{
		if(player.inventory.getCurrentItem().getItem() == GLContent.itemWrench){
			player.inventory.getCurrentItem().damageItem(1, player);
		}
		player.swingItem();
	}

	@Override
	public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player)
	{
		return true;
	}

	public static class Charged extends ItemWrench implements IEnergyContainerItem{

		public int maxEnergy = 10000000;
		private int maxTransfer = maxEnergy / 5;
		private int energyPerUse = 1000;

		public Charged()
		{
			super();
			this.setMaxDamage(-1);
			this.setHarvestLevel("Wrench", 3);
			this.setUnlocalizedName("gl.itemWrench.charged");
		}

		@Override
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool){
			list.add("The Wrench.");
			list.add(EnumChatFormatting.DARK_RED + "§dCharged.");
			list.add("");
			list.add("Stored: " + this.getEnergyStored(stack) + " / " + this.getMaxEnergyStored(stack) + " RF");
			list.add("Durability: §6INFINITY");

			if(GregLife.proxy.isShiftKeyDown()){
				list.add("-----------------------");
				list.add("EnergyPerUse: " + this.energyPerUse + " RF");
			}else{
				list.add(EnumChatFormatting.DARK_PURPLE + "§o--Press Shift for Info--");
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public void getSubItems(Item stack, CreativeTabs tab, List list) {
			list.add(ItemNBTHelper.setInteger(new ItemStack(stack), "Energy", this.maxEnergy));
		}

		@Override
		public double getDurabilityForDisplay(ItemStack stack) {
			return 1D - (double)ItemNBTHelper.getInteger(stack, "Energy", 0) / (double)getMaxEnergyStored(stack);
		}

		@Override
		public boolean showDurabilityBar(ItemStack stack) {
			return getEnergyStored(stack) < getMaxEnergyStored(stack);
		}

		@Override
		public boolean hasEffect(ItemStack p_77636_1_){
			return true;
		}

		@Override
		public boolean canWrench(EntityPlayer player, int x, int y, int z)
		{
			if(this.getEnergyStored(player.inventory.getCurrentItem()) > 0){
				return true;
			}
			return false;
		}

		@Override
		public void wrenchUsed(EntityPlayer player, int x, int y, int z)
		{
			if(player.inventory.getCurrentItem().getItem() == GLContent.itemWrench){
				player.inventory.getCurrentItem().damageItem(1, player);
			}else{
				extractEnergy(player.inventory.getCurrentItem(), energyPerUse, false);
			}
			player.swingItem();
		}

		/* IEnergyContainerItem */
		@Override
		public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
			int stored = ItemNBTHelper.getInteger(container, "Energy", 0);
			int receive = Math.min(maxReceive, Math.min(getMaxEnergyStored(container) - stored, maxTransfer));

			if (!simulate) {
				stored += receive;
				ItemNBTHelper.setInteger(container, "Energy", stored);
			}
			return receive;
		}

		@Override
		public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {

			int stored = ItemNBTHelper.getInteger(container, "Energy", 0);
			int extract = Math.min(maxExtract, stored);

			if (!simulate) {
				stored -= extract;
				ItemNBTHelper.setInteger(container, "Energy", stored);
			}
			return extract;
		}

		@Override
		public int getEnergyStored(ItemStack container) {
			return ItemNBTHelper.getInteger(container, "Energy", 0);
		}

		@Override
		public int getMaxEnergyStored(ItemStack container) {;
			return this.maxEnergy;
		}
	}
}
