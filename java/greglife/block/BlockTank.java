package greglife.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.tileentity.TileTank;
import greglife.util.Utils;
import net.minecraft.block.Block;
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

public class BlockTank extends BlockContainer{

	@SideOnly(Side.CLIENT)
	IIcon front;

	@SideOnly(Side.CLIENT)
	IIcon side;

	@SideOnly(Side.CLIENT)
	IIcon bottom;

	public int tankCapacity;

	public BlockTank(String name, int tankCapacity){
		super(Material.iron);
		this.setStepSound(soundTypeMetal);
		this.setBlockName(name);
		this.setHardness(100.0F);
		this.setCreativeTab(greglife.GregLife.GLTab);
		this.setResistance(100.0F);
		this.tankCapacity = tankCapacity;
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister){
		this.blockIcon = iconregister.registerIcon("greglife:baseIcon_t1");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta){
		return side == 1 ? this.blockIcon/*正面*/ : (side == 0 ? this.blockIcon/*底*/ : (side != meta ? this.blockIcon : this.blockIcon));
	}

	public TileEntity createNewTileEntity(World world, int meta){
		return new TileTank(tankCapacity);
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float f1, float f2, float f3){
		TileTank tile = (TileTank)world.getTileEntity(x, y, z);

		if(player.getCurrentEquippedItem() != null){
			if(Utils.hasUsableWrench(player, x, y, z)){
				if((!world.isRemote) && (player.isSneaking())){
					dismantleBlock(world, x, y, z);
					return true;
				}
				world.notifyBlocksOfNeighborChange(x, y, z, this);
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

	public void dismantleBlock(World world, int x, int y, int z)
	{
		ItemStack itemStack = new ItemStack(this);
		float motion = 0.7F;
		double motionX = world.rand.nextFloat() * motion + (1.0F - motion) * 0.5D;
		double motionY = world.rand.nextFloat() * motion + (1.0F - motion) * 0.5D;
		double motionZ = world.rand.nextFloat() * motion + (1.0F - motion) * 0.5D;
		EntityItem entityItem = new EntityItem(world, x + motionX, y + motionY, z + motionZ, itemStack);
		TileTank tileEntity = (TileTank)world.getTileEntity(x, y, z);

		if(tileEntity.tank.getFluidAmount() > 0){
			NBTTagCompound tankTag = tileEntity.getItemNBT();
			NBTTagCompound itemTag = Utils.getItemTag(itemStack);
			itemTag.setTag("tank", tankTag);
		}

		world.setBlockToAir(x, y, z);
		world.spawnEntityInWorld(entityItem);
	}

	@SuppressWarnings("unused")
	private void setDefaultDirection(World world, int x, int y, int z){
		if(!world.isRemote){
			Block b1 = world.getBlock(x, y, z - 1);
			Block b2 = world.getBlock(x, y, z + 1);
			Block b3 = world.getBlock(x - 1, y, z);
			Block b4 = world.getBlock(x + 1, y, z);
			byte b0=3;
			if(b1.func_149730_j()&&!b2.func_149730_j()){
				b0=3;
			}
			if(b2.func_149730_j()&&!b1.func_149730_j()){
				b0=2;
			}
			if(b3.func_149730_j()&&!b4.func_149730_j()){
				b0=5;
			}
			if(b4.func_149730_j()&&!b3.func_149730_j()){
				b0=4;
			}
			world.setBlockMetadataWithNotify(x, y, z, b0,2);
		}
	}

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityplayer, ItemStack itemstack){

		TileTank tile = (TileTank)world.getTileEntity(x, y, z);
		int direction = MathHelper.floor_double((double)(entityplayer.rotationYaw*4.0f/360.F)+0.5D)&3;

		if(direction==0){
			world.setBlockMetadataWithNotify(x,y, z, 2, 2);
		}
		if(direction==1){
			world.setBlockMetadataWithNotify(x,y, z, 5, 2);
		}
		if(direction==2){
			world.setBlockMetadataWithNotify(x,y, z, 3, 2);
		}
		if(direction==3){
			world.setBlockMetadataWithNotify(x,y, z, 4, 2);
		}

		if(itemstack.stackTagCompound != null)
		{
			TileTank tileEntity = (TileTank)world.getTileEntity(x, y, z);

			NBTTagCompound itemTag = itemstack.getTagCompound();

			if(itemTag != null && itemTag.hasKey("tank")){
				tileEntity.tank.readFromNBT(itemTag.getCompoundTag("tank"));
			}
		}
	}

	public static class Advanced extends BlockTank{

		public Advanced(String name, int tankCapacity){
			super(name, tankCapacity);
		}

		@SideOnly(Side.CLIENT)
		public void registerBlockIcons(IIconRegister iconregister){
			this.blockIcon = iconregister.registerIcon("greglife:baseIcon_t2");
		}

		@SideOnly(Side.CLIENT)
		public IIcon getIcon(int side, int meta){
			return side == 1 ? this.blockIcon/*正面*/ : (side == 0 ? this.blockIcon/*底*/ : (side != meta ? this.blockIcon : this.blockIcon));
		}

		public TileEntity createNewTileEntity(World world, int meta){
			return new TileTank.Advanced(tankCapacity);
		}

		public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack){
			if(stack.stackTagCompound != null)
			{
				TileTank tileEntity = (TileTank)world.getTileEntity(x, y, z);

				NBTTagCompound itemTag = stack.getTagCompound();

				if(itemTag != null && itemTag.hasKey("tank")){
					tileEntity.tank.readFromNBT(itemTag.getCompoundTag("tank"));
				}
			}
		}
	}

	public static class GregLife extends BlockTank{

		public GregLife(String name, int tankCapacity){
			super(name, tankCapacity);
		}

		@SideOnly(Side.CLIENT)
		public void registerBlockIcons(IIconRegister iconregister){
			this.blockIcon = iconregister.registerIcon("greglife:baseIcon_t3");
		}

		@SideOnly(Side.CLIENT)
		public IIcon getIcon(int side, int meta){
			return side == 1 ? this.blockIcon/*正面*/ : (side == 0 ? this.blockIcon/*底*/ : (side != meta ? this.blockIcon : this.blockIcon));
		}

		public TileEntity createNewTileEntity(World world, int meta){
			return new TileTank.GregLife(tankCapacity);
		}

		public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack){
			if(stack.stackTagCompound != null)
			{
				TileTank tileEntity = (TileTank)world.getTileEntity(x, y, z);

				NBTTagCompound itemTag = stack.getTagCompound();

				if(itemTag != null && itemTag.hasKey("tank")){
					tileEntity.tank.readFromNBT(itemTag.getCompoundTag("tank"));
				}
			}
		}
	}
}
