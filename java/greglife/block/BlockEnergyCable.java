package greglife.block;

import greglife.GregLife;
import greglife.tileentity.TileEnergyCable;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEnergyCable extends BlockContainer{

	public BlockEnergyCable(String name){
		super(Material.iron);
		this.setStepSound(soundTypeMetal);
		this.setBlockName(name);
		this.setCreativeTab(GregLife.GLTab);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta){
		return new TileEnergyCable();
	}

	/*public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);
        boolean[] blockBounds = new boolean[6];
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            blockBounds[direction.ordinal()] = world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ) instanceof IEnergyHandler && ((IEnergyHandler) world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ)).canConnectEnergy(direction);
        }
        //if (meta == 0) this.setBlockBounds(blockBounds[4] ? 0 : 0.675F, blockBounds[0] ? 0 : 0.675F, blockBounds[2] ? 0 : 0.675F, blockBounds[5] ? 1 : 0.925F, blockBounds[1] ? 1 : 0.925F, blockBounds[3] ? 1 : 0.925F);
        //if (meta == 1) this.setBlockBounds(blockBounds[4] ? 0 : 0.675F + 0.0925F, blockBounds[0] ? 0 : 0.675F + 0.0925F, blockBounds[2] ? 0 : 0.675F + 0.0925F, blockBounds[5] ? 1 : 0.925F - 0.0925F, blockBounds[1] ? 1 : 0.925F - 0.0925F, blockBounds[3] ? 1 : 0.925F - 0.0925F);

        if (meta == 0) this.setBlockBounds(blockBounds[4] ? 0 : 0.375F, blockBounds[0] ? 0 : 0.375F, blockBounds[2] ? 0 : 0.375F, blockBounds[5] ? 1 : 0.625F, blockBounds[1] ? 1 : 0.625F, blockBounds[3] ? 1 : 0.625F);
        if (meta == 1) this.setBlockBounds(blockBounds[4] ? 0 : 0.375F + 0.0625F, blockBounds[0] ? 0 : 0.375F + 0.0625F, blockBounds[2] ? 0 : 0.375F + 0.0625F, blockBounds[5] ? 1 : 0.625F - 0.0625F, blockBounds[1] ? 1 : 0.625F - 0.0625F, blockBounds[3] ? 1 : 0.625F - 0.0625F);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);
        boolean[] blockBounds = new boolean[6];
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            blockBounds[direction.ordinal()] = world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ) instanceof IEnergyHandler && ((IEnergyHandler) world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ)).canConnectEnergy(direction);
        }
        if (meta == 0) return AxisAlignedBB.getBoundingBox(x + (blockBounds[4] ? 0 : 0.375F), y + (blockBounds[0] ? 0 : 0.375F), z + (blockBounds[2] ? 0 : 0.375F), x + (blockBounds[5] ? 1 : 0.625F), y + (blockBounds[1] ? 1 : 0.625F), z + (blockBounds[3] ? 1 : 0.625F));
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }*/

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        return false;
    }
}
