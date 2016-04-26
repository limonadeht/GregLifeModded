package greglife.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.GregLife;
import greglife.tileentity.TileSprinkler;
import greglife.util.Utils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BlockSprinkler extends BlockContainer{

	private IIcon top, side;

	private int currentPass = 0;

	public BlockSprinkler(String name){
		super(Material.iron);
		this.setStepSound(soundTypeMetal);
		this.setBlockName(name);
		this.setHardness(100.0F);
		this.setCreativeTab(GregLife.GLTab);
		this.setResistance(100.0F);

        setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 1.0F, 0.875F);
	}

	public boolean isOpaqueCube(){
		return false;
	}

	public boolean renderAsNormalBlock(){
		 return false;
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister){
		this.top = iconregister.registerIcon("greglife:spBot");
		this.side = iconregister.registerIcon("greglife:spSide");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		switch (side){
		case 0:
		case 1:
			return this.top;
		}
		return this.currentPass == 0 ? this.side : this.top;
	}

	public TileEntity createNewTileEntity(World world, int meta){
		return new TileSprinkler();
	}

	public int getEnergyStored(ItemStack itemStack){
		if(itemStack.stackTagCompound != null){
			return itemStack.stackTagCompound.getInteger("Energy");
		}
		return 0;
	}

	public int getMaxEnergyStored(ItemStack itemStack){
		return 100000;
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float f1, float f2, float f3){

		TileSprinkler tile = (TileSprinkler)world.getTileEntity(x, y, z);

		if(player.getCurrentEquippedItem() != null){
			if (Utils.hasUsableWrench(player, x, y, z))
			{
				if ((!world.isRemote) && (player.isSneaking()))
				{
					dismantleBlock(world, x, y, z);

					return true;
				}
				world.notifyBlocksOfNeighborChange(x, y, z, this);

				return false;
			}
		}

		ItemStack itemstack = player.inventory.getCurrentItem();
        if(tile != null){
        	FluidStack fluid = tile.tank.getFluid();
        	if(itemstack == null){
        		String s = "";
        		if(fluid != null && fluid.getFluid() != null){
        			s = "Contain a fluid: " + fluid.getFluid().getLocalizedName(fluid) + tile.tank.getFluidAmount() + " / " + tile.tankCapacity;
        		}else{
        			s = "Not contain a fluid.";
        		}
        		if(!world.isRemote)player.addChatMessage(new ChatComponentText(s));
        		return true;
        	}else{
        		FluidStack fluid2 =  FluidContainerRegistry.getFluidForFilledItem(itemstack);
        		if(fluid2 != null && fluid2.getFluid() != null){
        			int put = tile.fill(ForgeDirection.UNKNOWN, fluid2, false);
        			if(put == fluid2.amount){
        				tile.fill(ForgeDirection.UNKNOWN, fluid2, true);

        				ItemStack emptyContainer = FluidContainerRegistry.drainFluidContainer(itemstack);
        				if(emptyContainer != null){
        					if(!player.inventory.addItemStackToInventory(emptyContainer.copy())){
        						player.entityDropItem(emptyContainer.copy(), 1);
        					}
        				}

        				if(!player.capabilities.isCreativeMode && itemstack.stackSize-- <= 0){
        					player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
        				}
        				tile.markDirty();
        				player.inventory.markDirty();
        				world.markBlockForUpdate(x, y, z);
        				world.playSoundAtEntity(player, "random.pop", 0.4F, 1.8F);
        				return true;
        			}
        		}else{
        			if(fluid != null && fluid.getFluid() != null){
        				if(fluid.amount < 1000)return true;
        				ItemStack get = FluidContainerRegistry.fillFluidContainer(new FluidStack(fluid.getFluid(), 1000), itemstack);
        				if(get != null){
        					tile.drain(ForgeDirection.UNKNOWN, 1000, true);
        					if(!player.inventory.addItemStackToInventory(get.copy())){
        						player.entityDropItem(get.copy(), 1);
        					}
        					if(!player.capabilities.isCreativeMode && itemstack.stackSize-- <= 0){
        						player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
        					}
        					tile.markDirty();
        					player.inventory.markDirty();
        					world.markBlockForUpdate(x, y, z);
        					world.playSoundAtEntity(player, "random.pop", 0.4F, 1.8F);
        				}
        				return true;
        			}else{
        				return true;
        			}
        		}
        	}
        }

		return false;
	}

	public void dismantleBlock(World world, int x, int y, int z){
		ItemStack itemStack = new ItemStack(this);
		float motion = 0.7F;
		double motionX = world.rand.nextFloat() * motion + (1.0F - motion) * 0.5D;
		double motionY = world.rand.nextFloat() * motion + (1.0F - motion) * 0.5D;
		double motionZ = world.rand.nextFloat() * motion + (1.0F - motion) * 0.5D;
		EntityItem entityItem = new EntityItem(world, x + motionX, y + motionY, z + motionZ, itemStack);
		TileSprinkler tileEntity = (TileSprinkler)world.getTileEntity(x, y, z);
		int energyStored = tileEntity.getEnergyStored();

		if(energyStored >= 1)
		{
			if(itemStack.getTagCompound() == null) {
				itemStack.setTagCompound(new NBTTagCompound());
			}
			itemStack.getTagCompound().setInteger("Energy", energyStored);
		}

		if(tileEntity.tank.getFluidAmount() > 0){
			NBTTagCompound tankTag = tileEntity.getItemNBT();
			NBTTagCompound itemTag = Utils.getItemTag(itemStack);
			itemTag.setTag("tank", tankTag);
		}

		world.setBlockToAir(x, y, z);
		world.spawnEntityInWorld(entityItem);
	}

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityplayer, ItemStack stack){
		TileSprinkler tile = (TileSprinkler)world.getTileEntity(x, y, z);
		int direction = MathHelper.floor_double((double)(entityplayer.rotationYaw*4.0f/360.F)+0.5D)&3;

		world.setBlockMetadataWithNotify(x,y, z, 3, 2);

		if(stack.stackTagCompound != null)
		{
			TileSprinkler tileEntity = (TileSprinkler)world.getTileEntity(x, y, z);
			tileEntity.setEnergyStored(stack.stackTagCompound.getInteger("Energy"));
		}

		if(stack.stackTagCompound != null)
		{
			TileSprinkler tileEntity = (TileSprinkler)world.getTileEntity(x, y, z);

			NBTTagCompound itemTag = stack.getTagCompound();

			if(itemTag != null && itemTag.hasKey("tank")){
				tileEntity.tank.readFromNBT(itemTag.getCompoundTag("tank"));
			}
		}
	}

	/*public void randomDisplayTick(World world, int x, int y, int z, Random random){
		float f1 = (float)x + 0.5F;
		float f2 = (float)y + 1.1F;
		float f3 = (float)z + 0.5F;
		float f4 = random.nextFloat() * 0.6F -0.3F;
		float f5 = random.nextFloat() * -0.6F - -0.3F;

		world.spawnParticle("reddust", (double)(f1+f4), (double)f2, (double)(f3+f5), 0.0D, 0.0D, 0.0D);
		world.spawnParticle("reddust", (double)(f1+f4), (double)f2, (double)(f3+f5), 0.0D, 0.0D, 0.0D);
		world.spawnParticle("reddust", (double)(f1+f4), (double)f2, (double)(f3+f5), 0.0D, 0.0D, 0.0D);
	}*/
}
