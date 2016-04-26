package greglife.block.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.GLContent;
import greglife.GregLife;
import greglife.tileentity.TileGregLifeSapling;
import greglife.util.FXutil;
import greglife.util.Location;
import greglife.util.StructureCreator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockGregLifeSapling extends BlockBush implements IGrowable, ITileEntityProvider {

private Random rng = new Random();

	private static final String[] variants = new String[]{"Oak", "Spruce", "Birch", "Jungle", "Acacia", "Roofed_Oak"};
	private static final IIcon[] icons = new IIcon[variants.length];

	public BlockGregLifeSapling(String name){
		super(Material.plants);
		float f = 0.4F;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
		this.setCreativeTab(GregLife.GLTab);
		this.setBlockName(name);
		this.setStepSound(soundTypeGrass);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if (!world.isRemote) {
			super.updateTick(world, x, y, z, random);

			if (world.getBlockLightValue(x, y + 1, z) >= 9 && random.nextInt(7) == 0) {
				generateTree(world, x, y, z, random);
			}
		}
	}

	public void generateTree(World world, int x, int y, int z, Random random) {
		if (((TileGregLifeSapling)world.getTileEntity(x, y, z)).currentCycle < TileGregLifeSapling.MAX_CYCLES) {
			((TileGregLifeSapling)world.getTileEntity(x, y, z)).currentCycle++;
		} else {
			((TileGregLifeSapling)world.getTileEntity(x, y, z)).currentCycle = 0;
			if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(world, random, x, y, z))
				return;

			int meta = world.getBlockMetadata(x, y, z);
			StructureCreator structureCreator = new StructureCreator();

			if (meta == 0) { //Oak
				int maxHeight = MathHelper.getRandomIntegerInRange(random, 4, 7);
				for (int i = y; i < y+maxHeight; i++)
					structureCreator.addBlock(new Location(x, i, z, world), GLContent.blockGregLifeLog);

				for (int j = 2; j < 4; j++)
					for (int k = -2; k < 3; k++)
						for (int l = -2; l < 3; l++)
							if (!(l == 0 && k == 0))
								if ((k == -2 || k == 2) && (l == -2 || l == 2)) {
									if (MathHelper.getRandomIntegerInRange(random, 0, 1) == 1) {
										structureCreator.addBlock(new Location(x+k, y+maxHeight-j, z+l, world), GLContent.blockGregLifeLeave, 0, new LeafValidityChecker());
									}
								} else
									structureCreator.addBlock(new Location(x+k, y+maxHeight-j, z+l, world), GLContent.blockGregLifeLeave, 0, new LeafValidityChecker());

				for (int j = 0; j < 2; j++)
					for (int k = -1; k < 2; k++)
						for (int l = -1; l < 2; l++)
							if (j == 1 || !(k == 0 && l == 0) || j == 0)
								if ((k == -1 || k == 1) && (l == -1 || l == 1) && j == 1) {
									if (MathHelper.getRandomIntegerInRange(random, 0, 1) == 1)
										structureCreator.addBlock(new Location(x+k, y+maxHeight-j, z+l, world), GLContent.blockGregLifeLeave, 0, new LeafValidityChecker());
								} else if ((k == 0 || l == 0) && !(k == 0 && l == 0 && j == 1)) {
									structureCreator.addBlock(new Location(x+k, y+maxHeight-j, z+l, world), GLContent.blockGregLifeLeave, 0, new LeafValidityChecker());
								}

			} else if (meta == 1) { //Spruce
				int maxHeight = MathHelper.getRandomIntegerInRange(random, 4, 9);
				int logHeight = maxHeight-1+MathHelper.getRandomIntegerInRange(random, 0, 1);
				for (int i = y; i < y+logHeight; i++)
					structureCreator.addBlock(new Location(x, i, z, world), GLContent.blockGregLifeLog, 1);

				if (MathHelper.getRandomIntegerInRange(random, 0, 1) == 1) { //Big leaf base
					for (int j = -3; j < 4; j++)
						for (int k = -3; k < 4; k++)
							if (!(j == 0 && k == 0) && !((j == -3 || j == 3) && (k == -3 || k == 3)))
								structureCreator.addBlock(new Location(x+j, y+maxHeight-6, z+k, world), GLContent.blockGregLifeLeave, 1, new LeafValidityChecker());

				} else { //Small leaf base
					for (int j = -1; j < 2; j++)
						for (int k = -1; k < 2; k++)
							if ((j == 0 || k == 0) && j != k)
								structureCreator.addBlock(new Location(x+j, y+maxHeight-6, z+k, world), GLContent.blockGregLifeLeave, 1, new LeafValidityChecker());
				}

				for (int j = -2; j < 3; j++)
					for (int k = -2; k < 3; k++)
						if (!(j == 0 && k == 0 && maxHeight-5 <= logHeight) && !((j == -2 || j == 2) && (k == -2 || k == 2)))
							structureCreator.addBlock(new Location(x+j, y+maxHeight-5, z+k, world), GLContent.blockGregLifeLeave, 1, new LeafValidityChecker());

				for (int j = -1; j < 2; j++)
					for (int k = -1; k < 2; k++)
						if (!(j == 0 && k == 0 && maxHeight-4 <= logHeight) && (j == 0 || k == 0))
							structureCreator.addBlock(new Location(x+j, y+maxHeight-4, z+k, world), GLContent.blockGregLifeLeave, 1, new LeafValidityChecker());

				for (int j = -2; j < 3; j++)
					for (int k = -2; k < 3; k++)
						if (!(j == 0 && k == 0 && maxHeight-3 <= logHeight) && !((j == -2 || j == 2) && (k == -2 || k == 2)))
							structureCreator.addBlock(new Location(x+j, y+maxHeight-3, z+k, world), GLContent.blockGregLifeLeave, 1, new LeafValidityChecker());

				for (int j = -1; j < 2; j++)
					for (int k = -1; k < 2; k++)
						if (!(j == 0 && k == 0 && maxHeight-1 <= logHeight) && (j == 0 || k == 0))
							structureCreator.addBlock(new Location(x+j, y+maxHeight-2, z+k, world), GLContent.blockGregLifeLeave, 1, new LeafValidityChecker());

				structureCreator.addBlock(new Location(x, y+maxHeight-1, z, world), GLContent.blockGregLifeLeave, 1, new LeafValidityChecker());

				for (int j = -1; j < 2; j++)
					for (int k = -1; k < 2; k++)
						if (j == 0 || k == 0)
							structureCreator.addBlock(new Location(x+j, y+maxHeight, z+k, world), GLContent.blockGregLifeLeave, 1, new LeafValidityChecker());

			} else if (meta == 2) { //Birch
				int maxHeight = MathHelper.getRandomIntegerInRange(random, 4, 7);
				for (int i = y; i < y+maxHeight; i++)
					structureCreator.addBlock(new Location(x, i, z, world), GLContent.blockGregLifeLog, 2);

				for (int j = 2; j < 4; j++)
					for (int k = -2; k < 3; k++)
						for (int l = -2; l < 3; l++)
							if (!(l == 0 && k == 0))
								if ((k == -2 || k == 2) && (l == -2 || l == 2)) {
									if (MathHelper.getRandomIntegerInRange(random, 0, 1) == 1) {
										structureCreator.addBlock(new Location(x+k, y+maxHeight-j, z+l, world), GLContent.blockGregLifeLeave, 2, new LeafValidityChecker());
									}
								} else
									structureCreator.addBlock(new Location(x+k, y+maxHeight-j, z+l, world), GLContent.blockGregLifeLeave, 2, new LeafValidityChecker());

				for (int j = 0; j < 2; j++)
					for (int k = -1; k < 2; k++)
						for (int l = -1; l < 2; l++)
							if (j == 1 || !(k == 0 && l == 0) || j == 0)
								if ((k == -1 || k == 1) && (l == -1 || l == 1) && j == 1) {
									if (MathHelper.getRandomIntegerInRange(random, 0, 1) == 1)
										structureCreator.addBlock(new Location(x+k, y+maxHeight-j, z+l, world), GLContent.blockGregLifeLeave, 2, new LeafValidityChecker());
								} else if ((k == 0 || l == 0) && !(k == 0 && l == 0 && j == 1)) {
									structureCreator.addBlock(new Location(x+k, y+maxHeight-j, z+l, world), GLContent.blockGregLifeLeave, 2, new LeafValidityChecker());
								}

			} else if (meta == 3) { //Jungle TODO: Big tree
				int maxHeight = MathHelper.getRandomIntegerInRange(random, 7, 10);
				for (int i = y; i < y+maxHeight; i++)
					structureCreator.addBlock(new Location(x, i, z, world), GLContent.blockGregLifeLog, 3);

				for (int j = 2; j < 4; j++)
					for (int k = -2; k < 3; k++)
						for (int l = -2; l < 3; l++)
							if (!(l == 0 && k == 0))
								if ((k == -2 || k == 2) && (l == -2 || l == 2)) {
									if (MathHelper.getRandomIntegerInRange(random, 0, 1) == 1) {
										structureCreator.addBlock(new Location(x+k, y+maxHeight-j, z+l, world), GLContent.blockGregLifeLeave, 3, new LeafValidityChecker());
									}
								} else
									structureCreator.addBlock(new Location(x+k, y+maxHeight-j, z+l, world), GLContent.blockGregLifeLeave, 3, new LeafValidityChecker());

				for (int j = 0; j < 2; j++)
					for (int k = -1; k < 2; k++)
						for (int l = -1; l < 2; l++)
							if (j == 1 || !(k == 0 && l == 0) || j == 0)
								if ((k == -1 || k == 1) && (l == -1 || l == 1) && j == 1) {
									if (MathHelper.getRandomIntegerInRange(random, 0, 1) == 1)
										structureCreator.addBlock(new Location(x+k, y+maxHeight-j, z+l, world), GLContent.blockGregLifeLog, 3, new LeafValidityChecker());
								} else if ((k == 0 || l == 0) && !(k == 0 && l == 0 && j == 1)) {
									structureCreator.addBlock(new Location(x+k, y+maxHeight-j, z+l, world), GLContent.blockGregLifeLog, 3, new LeafValidityChecker());
								}

			} else if (meta == 4) { //Acacia
				int treeFlag = MathHelper.getRandomIntegerInRange(random, 0, 1);
				int xOffset = 0, zOffset = 0;
				while (xOffset == 0 && zOffset == 0) {
					xOffset = MathHelper.getRandomIntegerInRange(random, -1, 1);
					zOffset = MathHelper.getRandomIntegerInRange(random, -1, 1);
				}
				boolean didOffset = false;
				int maxHeight = MathHelper.getRandomIntegerInRange(random, 6, 8);

				if (treeFlag == 0) {
					while (Math.abs(xOffset) == Math.abs(zOffset)) {
						xOffset = MathHelper.getRandomIntegerInRange(random, -1, 1);
						zOffset = MathHelper.getRandomIntegerInRange(random, -1, 1);
					}
					for (int i = y; i < y+maxHeight; i++) {
						if (!didOffset)
							didOffset = MathHelper.getRandomIntegerInRange(random, 0, 3) == 0;
						else {
							x += xOffset;
							z += zOffset;
						}
						structureCreator.addBlock(new Location(x, i, z, world), GLContent.blockGregLifeLog);
					}

				} else {
					for (int i = y; i < y+maxHeight; i++) {
						structureCreator.addBlock(new Location(x, i, z, world), GLContent.blockGregLifeLog);
					}

					int newX = x, newZ = z;
					for (int i = y; i < y+maxHeight-2; i++) {
						if (!didOffset)
							didOffset = MathHelper.getRandomIntegerInRange(random, 0, 4) == 0;
						else {
							newX += xOffset;
							newZ += zOffset;
							structureCreator.addBlock(new Location(newX, i, newZ, world), GLContent.blockGregLifeLog);
						}
					}

					if (didOffset) {
						for (int j = -2; j < 3; j++)
							for (int k = -2; k < 3; k++)
								if (!(j == 0 && k == 0) && (Math.abs(j) != Math.abs(k) || Math.abs(j) == 1))
									structureCreator.addBlock(new Location(newX+j, y+maxHeight-3, newZ+k, world), GLContent.blockGregLifeLeave, 0, new LeafValidityChecker());

						for (int j = -1; j < 2; j++)
							for (int k = -1; k < 2; k++)
								structureCreator.addBlock(new Location(newX+j, y+maxHeight-2, newZ+k, world), GLContent.blockGregLifeLeave, 0, new LeafValidityChecker());
					}
				}

				for (int j = -3; j < 4; j++)
					for (int k = -3; k < 4; k++)
						if (!(j == 0 && k == 0) && !((j == -3 || j == 3) && (k == -3 || k == 3)))
							structureCreator.addBlock(new Location(x+j, y+maxHeight-1, z+k, world), GLContent.blockGregLifeLeave, 0, new LeafValidityChecker());

				for (int j = -2; j < 3; j++)
					for (int k = -2; k < 3; k++)
						if ((j == 0 && (k == -2 || k == 2)) || (k == 0 && (j == -2 || j == 2))
								|| ((j <= 1 && j >= -1) && (k <= 1 && k >= -1)))
							structureCreator.addBlock(new Location(x+j, y+maxHeight, z+k, world), GLContent.blockGregLifeLeave, 0, new LeafValidityChecker());

			} else if (meta == 5) { //Roofed Oak FIXME
				Location[] saplings = getSaplingsForBigTree(world, x, y, z, meta);
				if (saplings == null)
					return;
				int maxHeight = MathHelper.getRandomIntegerInRange(random, 5, 8);

				for (Location l : saplings) {
					int xCoord = l.getRoundedX();
					int yCoord = l.getRoundedY();
					int zCoord = l.getRoundedZ();

					for (int i = 0; i < maxHeight-1; i++)
						structureCreator.addBlock(new Location(xCoord, yCoord+i, zCoord, world), GLContent.blockGregLifeLog, 1);

					boolean doBranch = MathHelper.getRandomIntegerInRange(random, 0, 3) == 1;
					if (doBranch) {
						int xOffset = 0, zOffset = 0;
						while (xOffset == 0 && zOffset == 0) {
							xOffset = MathHelper.getRandomIntegerInRange(random, -1, 1);
							zOffset = MathHelper.getRandomIntegerInRange(random, -1, 1);
						}
						int startHeight = MathHelper.getRandomIntegerInRange(random, 4, 6);
						boolean isSecond = true;

						for (int i = startHeight; i < maxHeight-2; i++) {
							xCoord += xOffset;
							zCoord += zOffset;
							if (isSecond) {
								isSecond = false;
								if (MathHelper.getRandomIntegerInRange(random, 0, 3) == 1) {
									xCoord -= xOffset;
									zCoord -= zOffset;
								}
							}
							structureCreator.addBlock(new Location(xCoord, yCoord+i, zCoord, world), GLContent.blockGregLifeLog, 1);
						}


					}

					structureCreator.addBlock(new Location(xCoord, yCoord+maxHeight, zCoord, world), GLContent.blockGregLifeLog, 1, new LeafValidityChecker());

					l.getWorld().setBlockToAir(l.getRoundedX(), l.getRoundedY(), l.getRoundedZ());
				}

				for (int i = -2; i < 5; i++)
					for (int j = -2; j < 5; j++)


				if (structureCreator.isStructureValid()) {
					structureCreator.generateStructure();
				} else {
					for (Location l : saplings)
						l.getWorld().setBlock(l.getRoundedX(), l.getRoundedY(), l.getRoundedZ(), GLContent.blockGregLifeSapling, meta, 0);
				}
				return;
			}

			world.setBlockToAir(x, y, z);
			if (structureCreator.isStructureValid()) {
				structureCreator.generateStructure();
			} else {
				world.setBlock(x, y, z, GLContent.blockGregLifeSapling, meta, 0);
			}
		}
	}

	private Location[] getSaplingsForBigTree(World world, int x, int y, int z, int meta) {
		List<Location> locations = new ArrayList<Location>();
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++)
				if (!world.isAirBlock(x+i, y, z+j) && world.getBlock(x+i, y, z+j) instanceof BlockGregLifeSapling
						&& world.getBlockMetadata(x+i, y, z+j) == meta) {
					Location location = new Location(x+i, y, z+j, world);
					if (!locations.contains(location))
						locations.add(location);
				}
			if (locations.size() == 4)
				break;
		}

		return locations.size() == 4 ? locations.toArray(new Location[4]) : null;
	}

/*	@Override
	public int damageDropped(int meta) {
		return meta;
	}*/

	/*@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List items) {
		items.add(new ItemStack(item, 1, 0));
		items.add(new ItemStack(item, 1, 1));
		items.add(new ItemStack(item, 1, 2));
		items.add(new ItemStack(item, 1, 3));
		items.add(new ItemStack(item, 1, 4));
		items.add(new ItemStack(item, 1, 5));
	}*/

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return icons[MathHelper.clamp_int(meta, 0, 5)];
	}

	/*@Override
	public String getUnlocalizedName(){//Formats the name
		return String.format("tile.%s%s", Reference.MOD_ID.toLowerCase()+":", getUnwrappedUnlocalizedName(getUnwrappedUnlocalizedName(super.getUnlocalizedName())));
	}*/

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister){//Registers the block icon(s)
		for (int i = 0; i < variants.length; i++)
			icons[i] = iconRegister.registerIcon("greglife:sapling");
	}

	/*protected String getUnwrappedUnlocalizedName(String unlocalizedName){//Removes the "item." from the item name
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
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
	public boolean func_149851_a(World world, int x, int y, int z, boolean p_149851_5_) {
		return true;
	}

	@Override
	public boolean func_149852_a(World world, Random random, int x, int y, int z) {
		return (double)world.rand.nextFloat() < 0.45D;
	}

	@Override
	public void func_149853_b(World world, Random random, int x, int y, int z) {
		generateTree(world, x, y, z, random);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileGregLifeSapling();
	}

	private class LeafValidityChecker implements StructureCreator.ICheckBlockValidity {

		@Override
		public boolean isBlockValid(Block block, int meta, Location location) {
			return location.getWorld().isAirBlock(location.getRoundedX(), location.getRoundedY(), location.getRoundedZ())
					|| location.getWorld().getBlock(location.getRoundedX(), location.getRoundedY(), location.getRoundedZ()).getMaterial().isReplaceable();
		}

		@Override
		public boolean canStructureGenerate(Block block, int meta, Location location) {
			return true;
		}
	}
}
