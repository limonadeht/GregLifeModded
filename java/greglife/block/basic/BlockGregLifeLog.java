package greglife.block.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.GregLife;
import greglife.util.FXutil;
import net.minecraft.block.BlockLog;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockGregLifeLog extends BlockLog{

	private Random rng = new Random();
	public String[] variants = new String[]{"Oak", "Spruce", "Birch", "Jungle"};

	public BlockGregLifeLog(String name) {
		super();
		this.setBlockName(name);
		this.setHardness(2.0F);
		this.setStepSound(soundTypeWood);
		this.setCreativeTab(GregLife.GLTab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List items) {
		//for (int i = 0; i < variants.length; i++)
		//	items.add(new ItemStack(item, 1, i));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.field_150167_a = new IIcon[variants.length];
		this.field_150166_b = new IIcon[variants.length];

		for (int i = 0; i < this.field_150167_a.length; ++i) {
			this.field_150167_a[i] = iconRegister.registerIcon("greglife:logSide");
			this.field_150166_b[i] = iconRegister.registerIcon("greglife:logTop");
		}
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

	private Double[] getOffsetCoordsForSide(double x, double y, double z, int side) {
		List<Double> coords = new ArrayList<Double>();
		switch (side) {
			case 0: //Bottom
				y -= .75;
				x += rng.nextGaussian()/3;
				z += rng.nextGaussian()/3;
				break;
			case 1: //Top
				y += .75;
				x += rng.nextGaussian()/3;
				z += rng.nextGaussian()/3;
				break;
			case 2: //East
				z -= .75;
				x += rng.nextGaussian()/3;
				y += rng.nextGaussian()/3;
				break;
			case 3: //West
				z += .75;
				x += rng.nextGaussian()/3;
				y += rng.nextGaussian()/3;
				break;
			case 4: //North
				x -= .75;
				y += rng.nextGaussian()/3;
				z += rng.nextGaussian()/3;
				break;
			case 5: //South
				x += .75;
				y += rng.nextGaussian()/3;
				z += rng.nextGaussian()/3;
				break;
		}
		coords.add(x);
		coords.add(y);
		coords.add(z);
		return coords.toArray(new Double[3]);
	}
}
