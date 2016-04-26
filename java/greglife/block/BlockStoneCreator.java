package greglife.block;

import greglife.GregLife;
import greglife.tileentity.TileStoneCreator;
import greglife.util.Utils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class BlockStoneCreator extends BlockContainer{

	public BlockStoneCreator(String name){
		super(Material.iron);
		this.setStepSound(soundTypeMetal);
		this.setBlockName(name);
		this.setHardness(100.0F);
		this.setCreativeTab(GregLife.GLTab);
		this.setResistance(100.0F);
	}

	public TileEntity createNewTileEntity(World world, int meta){
		return new TileStoneCreator();
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float f1, float f2, float f3){
		TileStoneCreator tile = (TileStoneCreator)world.getTileEntity(x, y, z);

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
        	FluidStack fluidW = tile.waterTank.getFluid();
        	FluidStack fluidL = tile.lavaTank.getFluid();
        	if(itemstack == null){
        		String s = "";
        		if(fluidW != null && fluidW.getFluid() != null || fluidL != null && fluidL.getFluid() != null){
        			s = "Contain a fluid: " + fluidW.getFluid().getLocalizedName(fluidW) + tile.waterTank.getFluidAmount() + " / " + tile.tankCapacity;
        		}else{
        			s = "Not contain a fluid.";
        		}
        		if(!world.isRemote)player.addChatMessage(new ChatComponentText(s));
        		return true;
        	}/*else{
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
        			if(fluidW != null && fluidW.getFluid() != null || fluidL != null && fluidL.getFluid() != null){
        				if(fluidW.amount < 1000)return true;
        				if(fluidL.amount < 1000)return true;
        				ItemStack getW = FluidContainerRegistry.fillFluidContainer(new FluidStack(fluidW.getFluid(), 1000), itemstack);
        				ItemStack getL = FluidContainerRegistry.fillFluidContainer(new FluidStack(fluidL.getFluid(), 1000), itemstack);
        				if(getW != null || getL != null){
        					tile.drain(ForgeDirection.UNKNOWN, 1000, true);
        					if(!player.inventory.addItemStackToInventory(getW.copy())){
        						player.entityDropItem(getW.copy(), 1);
        					}
        					if(!player.inventory.addItemStackToInventory(getL.copy())){
        						player.entityDropItem(getL.copy(), 1);
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
        }*/
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
		TileStoneCreator tileEntity = (TileStoneCreator)world.getTileEntity(x, y, z);

		if(tileEntity.waterTank.getFluidAmount() > 0){
			NBTTagCompound tankTag = tileEntity.getItemNBT();
			NBTTagCompound itemTag = Utils.getItemTag(itemStack);
			itemTag.setTag("WaterTank", tankTag);
		}
		if(tileEntity.lavaTank.getFluidAmount() > 0){
			NBTTagCompound tankTag = tileEntity.getItemNBT();
			NBTTagCompound itemTag = Utils.getItemTag(itemStack);
			itemTag.setTag("LavaTank", tankTag);
		}

		world.setBlockToAir(x, y, z);
		world.spawnEntityInWorld(entityItem);
	}

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityplayer, ItemStack itemstack){

		TileStoneCreator tile = (TileStoneCreator)world.getTileEntity(x, y, z);
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
			TileStoneCreator tileEntity = (TileStoneCreator)world.getTileEntity(x, y, z);

			NBTTagCompound itemTag = itemstack.getTagCompound();

			if(itemTag != null && itemTag.hasKey("WaterTank")){
				tileEntity.waterTank.readFromNBT(itemTag.getCompoundTag("WaterTank"));
			}
			if(itemTag != null && itemTag.hasKey("LavaTank")){
				tileEntity.lavaTank.readFromNBT(itemTag.getCompoundTag("LavaTank"));
			}
		}
	}
}
