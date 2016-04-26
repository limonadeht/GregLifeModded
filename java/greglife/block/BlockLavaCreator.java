package greglife.block;

import greglife.GregLife;
import greglife.tileentity.TileLavaCreator;
import greglife.util.Utils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockLavaCreator extends BlockContainer{

	public final int energyCapacity;
	public final int energyTransfer;

	public BlockLavaCreator(String name, int energyCapacity){
		super(Material.iron);
		this.setStepSound(soundTypeMetal);
		this.setBlockName(name);
		this.setBlockTextureName("greglife:lavaCreator");
		this.setHardness(100.0F);
		this.setCreativeTab(GregLife.GLTab);
		this.setResistance(100.0F);

		this.energyCapacity = energyCapacity;
		this.energyTransfer = Integer.MAX_VALUE;
	}

	public int getEnergyTransfer(){
		return this.energyTransfer;
	}

	public int getEnergyCapacity(){
		return this.energyCapacity;
	}

	public int getEnergyStored(ItemStack itemStack){
	    if (itemStack.stackTagCompound != null) {
	      return itemStack.stackTagCompound.getInteger("Energy");
	    }
	    return 0;
	}

	public TileEntity createNewTileEntity(World world, int meta){
		return new TileLavaCreator(this.energyCapacity, this.energyTransfer);
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float f1, float f2, float f3){
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
		return false;
	}

	public void dismantleBlock(World world, int x, int y, int z){
		ItemStack itemStack = new ItemStack(this);
		float motion = 0.7F;
		double motionX = world.rand.nextFloat() * motion + (1.0F - motion) * 0.5D;
		double motionY = world.rand.nextFloat() * motion + (1.0F - motion) * 0.5D;
		double motionZ = world.rand.nextFloat() * motion + (1.0F - motion) * 0.5D;
		EntityItem entityItem = new EntityItem(world, x + motionX, y + motionY, z + motionZ, itemStack);
		TileLavaCreator tileEntity = (TileLavaCreator)world.getTileEntity(x, y, z);
		int energyStored = tileEntity.getEnergyStored();

		if (energyStored >= 1)
		{
			if (itemStack.getTagCompound() == null) {
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

		TileLavaCreator tile = (TileLavaCreator)world.getTileEntity(x, y, z);
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

		if(stack.stackTagCompound != null)
		{
			TileLavaCreator tileEntity = (TileLavaCreator)world.getTileEntity(x, y, z);

			NBTTagCompound itemTag = stack.getTagCompound();

			if(itemTag != null && itemTag.hasKey("tank")){
				tileEntity.tank.readFromNBT(itemTag.getCompoundTag("tank"));
			}
		}

		if(stack.stackTagCompound != null)
		{
			TileLavaCreator tileEntity = (TileLavaCreator)world.getTileEntity(x, y, z);
			tileEntity.setEnergyStored(stack.stackTagCompound.getInteger("Energy"));
		}
	}
}
