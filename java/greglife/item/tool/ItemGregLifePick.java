package greglife.item.tool;

import java.awt.Color;
import java.util.List;

import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.GregLife;
import greglife.util.ItemNBTHelper;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.item.ISequentialBreaker;

public class ItemGregLifePick extends ItemPickaxe implements IEnergyContainerItem, ISequentialBreaker{

	private static final String TAG_ENABLED = "enabled";
	private static final String TAG_TIPPED = "tipped";

	private static final int MAX_ENERGY = Integer.MAX_VALUE;
	private static final int ENERGY_PER_DAMAGE = 1000;

	private static final Material[] MATERIALS = new Material[] { Material.rock, Material.iron, Material.ice, Material.glass, Material.piston, Material.anvil, Material.grass, Material.ground, Material.sand, Material.snow, Material.craftedSnow, Material.clay };

	public static final int[] LEVELS = new int[] {
			0, 10000, 1000000, 10000000, 100000000, 1000000000
	};

	private static final int[] CREATIVE_ENERGY = new int[] {
			10000 - 1, 1000000 - 1, 10000000 - 1, 100000000 - 1, 1000000000 - 1, MAX_ENERGY - 1
	};

	IIcon iconTool, iconOverlay, iconTipped;

	public ItemGregLifePick(String name){
		super(ToolMaterial.EMERALD);
		this.setUnlocalizedName(name);
		this.setCreativeTab(GregLife.GLTab);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(int energy : CREATIVE_ENERGY) {
			ItemStack stack = new ItemStack(item);
			ItemNBTHelper.setInteger(stack, "Energy", energy);
			list.add(stack);
		}
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		String rankFormat = StatCollector.translateToLocal("botaniamisc.toolRank");
		String rank = StatCollector.translateToLocal("botania.rank" + getLevel(par1ItemStack));
		par3List.add("Stored: " + this.getEnergyStored(par1ItemStack) + " / " + this.getMaxEnergyStored(par1ItemStack));

		if(GregLife.proxy.isShiftKeyDown()){
			par3List.add("-----------------------");
			par3List.add(EnumChatFormatting.GOLD + "一度につき " + this.ENERGY_PER_DAMAGE + " RF使用します");
			par3List.add("Max Storage: " + this.getMaxEnergyStored(par1ItemStack));
			par3List.add("Max Transfer: §6INFINITY");
		}else{
			par3List.add(EnumChatFormatting.DARK_PURPLE + "§o--Press Shift for Info--");
		}

		par3List.add(String.format(rankFormat, rank).replaceAll("&", "\u00a7"));
		if(this.getEnergyStored(par1ItemStack) == Integer.MAX_VALUE)
			par3List.add(EnumChatFormatting.RED + StatCollector.translateToLocal("botaniamisc.getALife"));
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){
		this.getEnergyStored(par1ItemStack);
		int level = getLevel(par1ItemStack);

		if(level != 0) {
			setEnabled(par1ItemStack, !isEnabled(par1ItemStack));
			if(!par2World.isRemote)
				par2World.playSoundAtEntity(par3EntityPlayer, "botania:terraPickMode", 0.5F, 0.4F);
		}

		return par1ItemStack;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int s, float sx, float sy, float sz) {
		return player.isSneaking() && super.onItemUse(stack, player, world, x, y, z, s, sx, sy, sz);
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		super.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
		if(isEnabled(par1ItemStack)) {
			int level = getLevel(par1ItemStack);

			if(level == 0)
				setEnabled(par1ItemStack, false);
			//else if(par3Entity instanceof EntityPlayer && !((EntityPlayer) par3Entity).isSwingInProgress)
			//	addMana(par1ItemStack, -level);
		}
	}

	@Override
	public void breakOtherBlock(EntityPlayer player, ItemStack stack, int x, int y, int z, int originX, int originY, int originZ, int side){
		if(!isEnabled(stack))
			return;

		World world = player.worldObj;
		Material mat = world.getBlock(x, y, z).getMaterial();
		if(!ToolCommons.isRightMaterial(mat, MATERIALS))
			return;

		if(world.isAirBlock(x, y, z))
			return;

		ForgeDirection direction = ForgeDirection.getOrientation(side);
		int fortune = EnchantmentHelper.getFortuneModifier(player);
		boolean silk = EnchantmentHelper.getSilkTouchModifier(player);
		//boolean thor = ItemThorRing.getThorRing(player) != null;
		boolean doX = true || direction.offsetX == 0;
		boolean doY = stack == null || direction.offsetY == 0;
		boolean doZ = stack == null || direction.offsetZ == 0;

		int origLevel = getLevel(stack);
		int level = origLevel + (true ? 1 : 0);
		//if(ItemTemperanceStone.hasTemperanceActive(player) && level > 2)
		//	level = 2;

		int range = Math.max(0, level - 1);
		int rangeY = Math.max(1, range);

		//if(range == 0 && level != 1)
		//	return;

		//ToolCommons.removeBlocksInIteration(player, stack, world, x, y, z, range, doY ? -1 : 0, range, range + 1, rangeY * 2, range + 1, null, MATERIALS, silk, fortune, isTipped(stack));
		ToolCommons.removeBlocksInIteration(player, stack, world, x, y, z, 11, 11, 11, 11, 11, 11, null, MATERIALS, silk, fortune, isTipped(stack));

		//ToolCommons.removeBlocksInIteration(player, stack, world, x, y, z, doX ? -range : 0, doY ? -1 : 0, doZ ? -range : 0, doX ? range + 1 : 1, doY ? rangeY * 2 : 1, doZ ? range + 1 : 1, null, MATERIALS, silk, fortune, isTipped(stack));

		//if(origLevel == 5)
			//player.addStat(ModAchievements.rankSSPick, 1);
	}

	@Override
	public int getEntityLifespan(ItemStack itemStack, World world) {
		return Integer.MAX_VALUE;
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		iconTool = par1IconRegister.registerIcon("greglife:glPick");
		iconOverlay = par1IconRegister.registerIcon("greglife:glPickOver");
		iconTipped = par1IconRegister.registerIcon("greglife:glPickTip");
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return pass == 1 && isEnabled(stack) ? iconOverlay : isTipped(stack) ? iconTipped : iconTool;
	}

	public static boolean isTipped(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_TIPPED, false);
	}

	public static void setTipped(ItemStack stack) {
		ItemNBTHelper.setBoolean(stack, TAG_TIPPED, true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		if(par2 == 0 || !isEnabled(par1ItemStack))
			return 0xFFFFFF;

		return Color.HSBtoRGB(0.375F, (float) Math.min(1F, Math.sin(System.currentTimeMillis() / 200D) * 0.5 + 1F), 1F);
	}

	boolean isEnabled(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_ENABLED, false);
	}

	void setEnabled(ItemStack stack, boolean enabled) {
		ItemNBTHelper.setBoolean(stack, TAG_ENABLED, enabled);
	}

	public static int getEnergyForNBT(ItemStack stack){
		return ItemNBTHelper.getInteger(stack, "Energy", 0);
	}

	public static int getLevel(ItemStack stack) {
		int energy = getEnergyForNBT(stack);
		for(int i = LEVELS.length - 1; i > 0; i--)
			if(energy >= LEVELS[i])
				return i;

		return 0;
	}

	/* IEnergyContainerItem */
	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
		int stored = ItemNBTHelper.getInteger(container, "Energy", 0);
		int receive = Math.min(maxReceive, Math.min(getMaxEnergyStored(container) - stored, Integer.MAX_VALUE));

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

	@Override
	public int getMaxEnergyStored(ItemStack container) {;
		return this.MAX_ENERGY;
	}

	@Override
	public boolean disposeOfTrashBlocks(ItemStack stack) {
		return isTipped(stack);
	}
}
