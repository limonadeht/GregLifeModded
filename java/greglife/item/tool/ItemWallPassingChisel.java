package greglife.item.tool;

import java.util.List;
import java.util.Random;

import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.GregLife;
import greglife.util.ItemNBTHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemWallPassingChisel extends Item implements IEnergyContainerItem{

	public int maxEnergy = 10000000;
	private int maxTransfer = 8192;
	private int energyPerUse = 10000;

	private IIcon iconDef, iconAct;

	private Random rand = new Random();

	private static final String TAG_ENABLED = "enabled";

	public ItemWallPassingChisel(String name){
		super();
		//this.setTextureName("greglife:WPchisel");
		this.setMaxStackSize(1);
		this.setCreativeTab(GregLife.GLTab);
		this.setFull3D();
		this.setUnlocalizedName(name);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4){
		list.add("§m§lトイレの栓抜き");
		list.add("Stored: " + this.getEnergyStored(stack) + " / " + this.getMaxEnergyStored(stack));
		list.add("Right-Click to Change Mode.");

		if(this.isEnabled(stack)){
			list.add("Mode: §aAwakening");
		}else{
			list.add("Mode: §aIdle");
		}

		if(GregLife.proxy.isShiftKeyDown()){
			list.add("-----------------------");
			list.add("Max Storage: " + this.getMaxEnergyStored(stack));
			list.add("Max Transfer: " + this.maxTransfer);
		}else{
			list.add(EnumChatFormatting.DARK_PURPLE + "§o--Press Shift for Info--");
		}
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		iconDef = par1IconRegister.registerIcon("greglife:WPchisel");
		iconAct = par1IconRegister.registerIcon("greglife:WPchiselAct");
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return pass == 1 && isEnabled(stack) ? iconAct : iconDef;
	}

	boolean isEnabled(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_ENABLED, false);
	}

	void setEnabled(ItemStack stack, boolean enabled) {
		ItemNBTHelper.setBoolean(stack, TAG_ENABLED, enabled);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list){
		list.add(ItemNBTHelper.setInteger(new ItemStack(item), "Energy", 0));
		list.add(ItemNBTHelper.setInteger(new ItemStack(item), "Energy", maxEnergy));
		list.add(ItemNBTHelper.setBoolean(new ItemStack(item), TAG_ENABLED, true));
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player){
		this.getEnergyStored(stack);

		if(player.isSneaking()){
			setEnabled(stack, !isEnabled(stack));
			player.swingItem();
			if(!world.isRemote){
				world.playSoundAtEntity(player, "random.explode", 0.5F, 0.4F);
				player.addChatComponentMessage(new ChatComponentText(this.isEnabled(stack) ? "WALL-PASSING-CHISEL MODE: §aAwakening" : "WALL-PASSING-CHISEL MODE: §aIdle"));
			}
		}else{
			if(this.isEnabled(stack) && !world.isRemote){
				Vec3 vec = player.getLookVec();
	            double wantedVelocity = 4.7;

	            player.motionX = vec.xCoord * wantedVelocity;
	            player.motionY = vec.yCoord * wantedVelocity;
	            player.motionZ = vec.zCoord * wantedVelocity;
	            player.velocityChanged = true;
	            world.playSoundEffect((double) ((float) player.posX + 0.5F), (double) ((float) player.posY + 0.5F), (double) ((float) player.posZ + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
	            player.fallDistance = 0;
				this.extractEnergy(stack, energyPerUse, false);
			}
		}

		return stack;
	}

	public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int blockX, int blockY, int blockZ, int surface, float hitX, float hiyY, float hitZ){

		int px = 0;
		int py = 0;
		int pz = 0;
		switch(surface){
		case 0:
			py = 1;
			break;
		case 1:
			py = -1;
			break;
		case 2:
			pz = 1;
			break;
		case 3:
			pz = -1;
			break;
		case 4:
			px = 1;
			break;
		default:
			px = -1;
			break;
		}

		if(this.getEnergyStored(itemStack) < this.energyPerUse){
			if(world.isRemote)player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "Err: Out of Energy."));
			return false;
		}

		if(player instanceof EntityPlayerMP && !world.isRemote){
			EntityPlayerMP playerMP = (EntityPlayerMP)player;

			for(int i = 0; i < 16; i++)
			{
				if( world.isAirBlock(blockX, blockY  ,blockZ))
				{
					if( world.isAirBlock(blockX, blockY + 1, blockZ))
					{
						world.playSoundAtEntity(player, "random.fizz", 0.5F, 1.0F);
						player.posX = (double)blockX + 0.5D;
						player.posY = (double)blockY + player.yOffset;
						player.posZ = (double)blockZ + 0.5D;
						player.motionX = 0.0D;
						player.motionY = 0.0D;
						player.motionZ = 0.0D;
						playerMP.playerNetServerHandler.setPlayerLocation((double)blockX + 0.5D, (double)blockY + player.yOffset, (double)blockZ + 0.5D, player.rotationYaw, player.rotationPitch);

						//player.setPosition( (double)blockX + 0.5D, (double)blockY + player.yOffset, (double)blockZ + 0.5D);
						//itemStack.damageItem(1, player);
						this.extractEnergy(itemStack, energyPerUse, false);
						return false;
					}
					else if( world.isAirBlock(blockX, blockY - 1, blockZ))
					{
						world.playSoundAtEntity(player, "random.fizz", 0.5F, 1.0F);
						player.posX = (double)blockX + 0.5D;
						player.posY = (double)blockY + player.yOffset - 1.0D;
						player.posZ = (double)blockZ + 0.5D;
						player.motionX = 0.0D;
						player.motionY = 0.0D;
						player.motionZ = 0.0D;
						playerMP.playerNetServerHandler.setPlayerLocation((double)blockX + 0.5D, (double)blockY + player.yOffset - 1.0D, (double)blockZ + 0.5D, player.rotationYaw, player.rotationPitch);

						//player.setPosition( (double)blockX + 0.5D, (double)blockY + player.yOffset - 1.0D, (double)blockZ + 0.5D);
						//itemStack.damageItem(1, player);
						this.extractEnergy(itemStack, energyPerUse, false);
						return false;
					}
				}

				if(world.getBlock(blockX, blockY, blockZ) == Blocks.bedrock)
				{
					return false;
				}
				blockX += px;
				blockY += py;
				blockZ += pz;
			}
		}
		return false;
	}



	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		super.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);

		if(this.isEnabled(par1ItemStack)){
			for(int i = 0; i < 150; i++){
				this.setEnergyStored(par1ItemStack, 1);
			}
		}
	}

	@Override
	public boolean hasEffect(ItemStack stack, int pass){
		return this.isEnabled(stack) ? true: false;
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
	public int getMaxEnergyStored(ItemStack container) {;
		return this.maxEnergy;
	}
}
