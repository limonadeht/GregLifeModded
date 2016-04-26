package greglife.item.tool;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.GLContent;
import greglife.GregLife;
import greglife.util.IBreakable;
import greglife.util.IconDic;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.EnumHelper;

public class ItemGregLifePickaxe extends ItemPickaxe implements IBreakable{

	private static int damage = 1;
	private static float efficiency = 5.0F;
	private static int toolLevel = 10;
	public static ToolMaterial material = EnumHelper.addToolMaterial("FORCE", toolLevel, 512, efficiency, damage, 0);

	public ItemGregLifePickaxe(String name){
		super(material);
		this.setUnlocalizedName(name);
		this.setCreativeTab(GregLife.GLTab);
	}

	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5)
	{
		if (stack == null) {
			return;
		}
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		/*if (!stack.getTagCompound().hasKey("consumerContents")) {
			ForceConsumerUtils.initializeForceConsumer(stack);
		}
		if (!stack.getTagCompound().hasKey("ID")) {
			stack.getTagCompound().setInteger("ID", ProxyCommon.rand.nextInt());
		}*/
	}

	public boolean hasEffect(ItemStack stack)
	{
		return false;
	}

	public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player)
	{
		try
		{
			World world = player.worldObj;
			Block brokenID = world.getBlock(x, y, z);
			//Block tempBlock = Block.field_71973_m[brokenID];

			boolean force = false;
			if (brokenID.isAir(world, x, y, z)) {
				return false;
			}
			if ((stack.hasTagCompound()) && (stack.getTagCompound().getBoolean("active"))) {
				force = true;
			}
			if (force) {
				for (int i = -1; i < 2; i++) {
					for (int j = -1; j < 2; j++) {
						for (int k = -1; k < 2; k++) {
							if ((i != 0) || (j != 0) || (k != 0)) {
								if ((stack != null) && (stack.getItemDamage() < stack.getMaxDamage())) {
									tryBlock(stack, x + i, y + j, z + k, player, false);
								} else {
									return false;
								}
							}
						}
					}
				}
			}
			if ((stack != null) && (stack.getItemDamage() < stack.getMaxDamage())) {
				return tryBlock(stack, x, y, z, player, true);
			}
		}
		catch (Exception e)
		{
			//DebugUtils.printError(e);
			System.out.println("[GregLife/Err] GregLife Pickaxe Error. Please report to mod author.");
		}
		return false;
	}

	private boolean tryBlock(ItemStack stack, int x, int y, int z, EntityPlayer player, boolean mustSmelt)
	{
		try
		{
			World world = player.worldObj;
			Block brokenID = world.getBlock(x, y, z);
			if (world.isAirBlock(x, y, z)) {
				return false;
			}
			/*Block tempBlock = Block.getBlockFromItem(null)[brokenID];
			NBTTagCompound upgrades = UpgradeHelper.getUpgradeCompound(stack);
			boolean smelted = false;
			if ((upgrades.hasKey("Heat")) && (!upgrades.hasKey("Grinding")) && (!player.isSneaking()) &&
					(func_77641_a(tempBlock))) {
				smelted = DartUtils.smeltBlock(world, stack, player, x, y, z);
			}
			if ((!smelted) && (upgrades.hasKey("Grinding")) && (!player.isSneaking()) && (func_77641_a(tempBlock))) {
				smelted = DartUtils.grindBlock(world, stack, player, x, y, z);
			}
			if ((!mustSmelt) && (!smelted) && (func_77641_a(tempBlock)) &&
					((world.getBlock(x, y, z) == null))
			{*/
				//Blocks.[world.func_72798_a(x, y, z)].func_71893_a(world, player, x, y, z, world.getBlockMetadata(x, y, z));
				//world.setBlockToAir(x, y, z);
				//DartUtils.damageTool(player, stack, 1);
				stack.damageItem(1, player);
			//}
			return false;
		}
		catch (Exception e)
		{
			//DebugUtils.printError(e);
			System.out.println("[GregLife/Err] GregLife Pickaxe Error. Please report to mod author.");
		}
		return false;
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setBoolean("active", !stack.getTagCompound().getBoolean("active"));
		//if (!.isSimulating(world)) {
		String s = "";
			if (stack.getTagCompound().getBoolean("active")) {
				//Proxies.common.sendChatToPlayer(player, "Area mode activated.");
				s = "Area mode activated.";
			} else {
				//Proxies.common.sendChatToPlayer(player, "Area mode deactivated");
				s = "Area mode deactivated";
			}
			if(world.isRemote){
				player.addChatComponentMessage(new ChatComponentText(s));
			}
		//}
		return stack;
	}

	public float func_77638_a(ItemStack stack, Block block)
	{
		return getDigSpeed(stack, block, 0);
	}

	public float getDigSpeed(ItemStack stack, Block block, int meta)
	{
		float modifier = 1.0F;
		if (stack.hasTagCompound())
		{
			NBTTagCompound upgrades = stack.getTagCompound().getCompoundTag("upgrades");
			if (upgrades.hasKey("Speed")) {
				modifier = 1.0F + 0.75F * upgrades.getInteger("Speed");
			}
			if (stack.getTagCompound().getBoolean("active")) {
				modifier /= 9.0F;
			}
		}
		if ((ForgeHooks.isToolEffective(stack, block, meta)) /*|| (this.func_150893_a(stack, block))*/) {
			return this.efficiencyOnProperMaterial * modifier;
		}
		return super.getDigSpeed(stack, block, meta);
	}

	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass)
	{
		if (stack.hasTagCompound())
		{
			NBTTagCompound upgrades = stack.getTagCompound().getCompoundTag("upgrades");
			if (upgrades != null) {
				if (upgrades.hasKey("Heat")) {
					return IconDic.iconPickHeat;
				}
			}
		}
		return this.itemIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reggie)
	{
		IconDic.iconPick = reggie.registerIcon("greglife:pick");
		IconDic.iconPickHeat = reggie.registerIcon("greglife:pick_heat");

		this.itemIcon = IconDic.iconPick;
	}

	public ItemStack itemReturned()
	{
		return new ItemStack(GLContent.itemGregLifeNugget, 1, 0);
	}
}
