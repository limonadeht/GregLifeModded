package greglife.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.GregLife;
import greglife.tileentity.TileGregLifeInfuser;
import greglife.util.FXutil;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockGregLifeInfuser extends BlockContainer implements ITileEntityProvider{

	public BlockGregLifeInfuser(String name) {
		super(Material.iron);
		this.setBlockName(name);
		this.setBlockBounds(0, 0, 0, 1, .5F, 1);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileGregLifeInfuser();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float hitX, float hitY, float hitZ){
		TileEntity te = world.getTileEntity(x, y, z);
		if (!world.isRemote) {
			if (te != null && te instanceof TileGregLifeInfuser)
				player.openGui(GregLife.Instance, 8, world, x, y, z);
		}
		return true;
	}

	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer renderer){
		FXutil.makeShiny(world, x, y, z, 2, 16776960, 32, true);
		return true;
	}

	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(World world, MovingObjectPosition target, EffectRenderer renderer){
		if((world != null) && (target != null)){
			FXutil.makeShiny(world, target.blockX, target.blockY, target.blockZ, 2, 16776960, 3, true);
		}
		return true;
	}
}
