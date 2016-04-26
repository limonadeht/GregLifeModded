package greglife.block.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.GLContent;
import greglife.GregLife;
import greglife.util.FXutil;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGregLifeLeave extends BlockLeaves{

private Random rng = new Random();

	public String[][] textures = new String[][] {{"glLeave"}};

	public BlockGregLifeLeave(String name){
		super();
		this.setBlockName(name);
		this.setLightLevel(.6F);
		this.setLightOpacity(0);
		this.setCreativeTab(GregLife.GLTab);
	}

	@Override
	public Item getItemDropped(int meta, Random random, int chance) {
		return Item.getItemFromBlock(GLContent.blockGregLifeSapling);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		if (MathHelper.getRandomIntegerInRange(rng, 0, 16-fortune) == 1) {
			drops.add(new ItemStack(GLContent.blockGregLifeSapling, 1, damageDropped(metadata)));
		}
		if (MathHelper.getRandomIntegerInRange(rng, 0, 10-fortune) == 1) {
			drops.add(new ItemStack(GLContent.itemGregLifeNugget));
		}
		return drops;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

//	@Override
//	@SideOnly(Side.CLIENT)
//	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
//		return Minecraft.getMinecraft().gameSettings.fancyGraphics || super.shouldSideBeRendered(world, x, y, z, side);
//	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if (MathHelper.getRandomIntegerInRange(random, 0, 3) == 1) {
			FXutil.makeShiny(world, x, y, z, 2, 16776960, 3, true);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockColor() {
		return 16777215;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int side) {
		return 16777215;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
		return 16777215;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return (meta & 3) == 1 ? this.field_150129_M[Minecraft.getMinecraft().gameSettings.fancyGraphics ? 0 : 1][1] : ((meta & 3) == 3 ? this.field_150129_M[Minecraft.getMinecraft().gameSettings.fancyGraphics ? 0 : 1][3] : ((meta & 3) == 2 ? this.field_150129_M[Minecraft.getMinecraft().gameSettings.fancyGraphics ? 0 : 1][2] : this.field_150129_M[Minecraft.getMinecraft().gameSettings.fancyGraphics ? 0 : 1][0]));
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
		for (int i = 0; i < textures.length; ++i) {
			this.field_150129_M[i] = new IIcon[textures[i].length];

			for (int j = 0; j < textures[i].length; ++j) {
				this.field_150129_M[i][j] = iconRegister.registerIcon("greglife:"+textures[i][j]);
			}
		}
	}

	/*@Override
	public String[] func_150125_e() {
		return variants;
	}*/

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

	@Override
	public String[] func_150125_e() {
		return null;
	}
}
