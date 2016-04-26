package greglife.item.tool;

import java.util.List;

import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.GLContent;
import greglife.GregLife;
import greglife.util.ItemNBTHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemGregLifeCapacitor extends Item implements IEnergyContainerItem{

	IIcon[] icons = new IIcon[2];

	public ItemGregLifeCapacitor(String name){
		this.setUnlocalizedName(name);
		this.setCreativeTab(GregLife.GLTab);
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		icons[0] = iconRegister.registerIcon("greglife:capa0");
		icons[1] = iconRegister.registerIcon("greglife:capa1");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamage(int damage) {
		return icons[damage];
	}

	@SideOnly(Side.CLIENT)
	@SuppressWarnings("unchecked")
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		list.add(ItemNBTHelper.setInteger(new ItemStack(item, 1, 0), "Energy", 0));
		list.add(ItemNBTHelper.setInteger(new ItemStack(item, 1, 0), "Energy", 80000000));
		list.add(ItemNBTHelper.setInteger(new ItemStack(item, 1, 1), "Energy", 0));
		list.add(ItemNBTHelper.setInteger(new ItemStack(item, 1, 1), "Energy", 250000000));
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return super.getUnlocalizedName(itemStack)+itemStack.getItemDamage();
	}

	public int getCapacity(ItemStack stack){
		return stack.getItemDamage() == 0 ? 80000000 * 50000000 : stack.getItemDamage() == 1 ? 250000000 * 50000000 : 0;
 	}

	 public int getMaxExtract(ItemStack stack){
		return stack.getItemDamage() == 0 ? 10000000 : stack.getItemDamage() == 1 ? 100000000 : 0;
	}

	public int getMaxReceive(ItemStack stack){
		return stack.getItemDamage() == 0 ? 1000000 : stack.getItemDamage() == 1 ? 10000000 : 0;
	}

	@Override
	public void onUpdate(ItemStack container, World world, Entity entity, int var1, boolean b) {
		if (!(entity instanceof EntityPlayer)) return;
		EntityPlayer player = (EntityPlayer) entity;

		int mode = ItemNBTHelper.getShort(container, "Mode", (short)0);

		if (mode == 1 || mode == 3){ //Charge Hotbar
			for (int i = 0; i < 9; i++){
				int max = Math.min(getEnergyStored(container), getMaxExtract(container));
				ItemStack stack = player.inventory.getStackInSlot(i);

				if (stack != null && stack.getItem() instanceof IEnergyContainerItem && stack.getItem() != GLContent.itemGregLifeCapacitor) {
					IEnergyContainerItem item = (IEnergyContainerItem)stack.getItem();
					extractEnergy(container, item.receiveEnergy(stack, max, false), false);
				}
			}
		}

		if (mode == 2 || mode == 3){ //Charge Armor and held item
			for (int i = mode == 3 ? 1 : 0; i < 5; i++){
				int max = Math.min(getEnergyStored(container), getMaxExtract(container));
				ItemStack stack = player.getEquipmentInSlot(i);

				if (stack != null && stack.getItem() instanceof IEnergyContainerItem && stack.getItem() != GLContent.itemGregLifeCapacitor) {
					IEnergyContainerItem item = (IEnergyContainerItem)stack.getItem();
					extractEnergy(container, item.receiveEnergy(stack, max, false), false);
				}
			}
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (player.isSneaking()){
			int mode = ItemNBTHelper.getShort(stack, "Mode", (short)0);
			int newMode = mode == 3 ? 0 : mode + 1;
			ItemNBTHelper.setShort(stack, "Mode", (short) newMode);
			if (world.isRemote) player.addChatComponentMessage(new ChatComponentTranslation(ItemNBTHelper.getShort(stack, "Mode", (short)0)+".lore"));
		}
		return stack;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer player, final List list, final boolean extraInformation)
	{
		this.addEnergyInfo(stack, list);
		if(GregLife.proxy.isShiftKeyDown()){
			list.add("-----------------------");
			list.add("Max Storage: " + this.getMaxEnergyStored(stack));
			list.add("Max Input : " + this.getMaxReceive(stack));
			list.add("Max Output: " + this.getMaxExtract(stack));
		}else{
			list.add(EnumChatFormatting.DARK_PURPLE + "§o--Press Shift for Info--");
		}
	}

	public static void addEnergyInfo(ItemStack stack, List list) {
		IEnergyContainerItem item = (IEnergyContainerItem)stack.getItem();
		int energy = item.getEnergyStored(stack);
		int maxEnergy = item.getMaxEnergyStored(stack);
		String eS = "";
		String eM = "";
		if (energy < 1000)
			eS = String.valueOf(energy);
		else if (energy < 1000000)
			eS = String.valueOf(energy);//Math.round((float)energy / 10F)/100F)+"k";
		else
			eS = String.valueOf(Math.round((float)energy / 1000F)/1000F)+"m";
		if (maxEnergy < 1000)
			eM = String.valueOf(maxEnergy);
		else if (maxEnergy < 1000000)
			eM = String.valueOf(Math.round((float)maxEnergy / 100F)/10F)+"k";
		else
			eM = String.valueOf(Math.round((float)maxEnergy / 10000F)/100F)+"m";

		list.add(StatCollector.translateToLocal("gl.charge.lore") + ": " + eS + " / " + eM);
	}

	@Override
	public boolean hasEffect(ItemStack stack, int pass) {
		return ItemNBTHelper.getShort(stack, "Mode", (short)0) > 0;
	}

	public int getMaxTier(ItemStack stack) {
		return stack.getItemDamage() == 0 ? 1 : 2;
	}



	/* IEnergyContainerItem */
	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
		int stored = ItemNBTHelper.getInteger(container, "Energy", 0);
		int receive = Math.min(maxReceive, Math.min(getMaxEnergyStored(container) - stored, this.getMaxReceive(container)));

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
		//return 0;//エネルギー搬入Only
	}

	@Override
	public int getEnergyStored(ItemStack container) {
		return ItemNBTHelper.getInteger(container, "Energy", 0);
	}

	public void setEnergyStored(ItemStack container, int amount){
		ItemNBTHelper.setInteger(container, "Energy", this.getEnergyStored(container) - amount);
	}

	@Override
	public int getMaxEnergyStored(ItemStack container){
		return this.getCapacity(container);
	}
}
