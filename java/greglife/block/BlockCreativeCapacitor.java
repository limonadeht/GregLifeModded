package greglife.block;

import greglife.GregLife;
import greglife.tileentity.TileCreativeCapacitor;
import greglife.util.Utils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCreativeCapacitor extends BlockContainer{

	public final int energyCapacity = Integer.MAX_VALUE;
	public final int energyTransfer = Integer.MAX_VALUE;

	public BlockCreativeCapacitor(String name){
		super(Material.iron);
		this.setStepSound(soundTypeMetal);
		this.setBlockName(name);
		this.setBlockTextureName("greglife:capacitor");
		this.setHardness(-1);
		this.setCreativeTab(GregLife.GLTab);
		this.setResistance(10000000000.0F);
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
		return new TileCreativeCapacitor(this.energyCapacity, this.energyTransfer);
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
		TileCreativeCapacitor tileEntity = (TileCreativeCapacitor)world.getTileEntity(x, y, z);
		world.setBlockToAir(x, y, z);
		world.spawnEntityInWorld(entityItem);
	}
}
