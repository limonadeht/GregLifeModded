package greglife.block;

import java.util.Random;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.GLContent;
import greglife.GregLife;
import greglife.tileentity.TileBurningGenerator;
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
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockBurningGenerator extends BlockContainer{

	@SideOnly(Side.CLIENT)
	IIcon front;

	@SideOnly(Side.CLIENT)
	IIcon top;

	@SideOnly(Side.CLIENT)
	IIcon bottom;

	public final int energyGeneration;
	public final int energyTransfer;
	public final int energyCapacity;

	private Random rand = new Random();

	private final boolean keepInventory = true;

	public BlockBurningGenerator(String name, int energyGeneration){
		super(Material.iron);
		this.setStepSound(soundTypeMetal);
		this.setBlockName(name);
		this.setHardness(100.0F);
		this.setCreativeTab(GregLife.GLTab);
		this.setResistance(100.0F);

		this.energyGeneration = energyGeneration;
		this.energyTransfer = (energyGeneration * 5);
		this.energyCapacity = (energyGeneration * 1000);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta){
		//return side == 1 ? this.blockIcon/*top*/ : (side == 0 ? this.blockIcon/*底*/ : (side != meta ? this.blockIcon : this.front/*正面*/));
		return meta == 0 && side == 3 ? this.front : side == 1 ? this.top : (side == 0 ? this.top : (side == meta ? this.front : this.blockIcon));
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister){
		this.blockIcon = iconregister.registerIcon("greglife:baseIcon_t1");
		this.top = iconregister.registerIcon("greglife:baseIcon_t1");
		this.front = iconregister.registerIcon("greglife:burn_front_t1");
	}

	public int getEnergyGeneration(){
		return this.energyGeneration;
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
		return new TileBurningGenerator(this.energyGeneration, this.energyTransfer, this.energyCapacity);
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float f1, float f2, float f3){

		TileBurningGenerator tile = (TileBurningGenerator)world.getTileEntity(x, y, z);

		if(!player.isSneaking()){
			FMLNetworkHandler.openGui(player, GregLife.Instance, 1, world, x, y, z);
			return true;
		}

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
		TileBurningGenerator tileEntity = (TileBurningGenerator)world.getTileEntity(x, y, z);
		int energyStored = tileEntity.getEnergyStored();

		if (energyStored >= 1)
		{
			if (itemStack.getTagCompound() == null) {
				itemStack.setTagCompound(new NBTTagCompound());
			}
			itemStack.getTagCompound().setInteger("Energy", energyStored);
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

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityplayer, ItemStack stack){
		TileBurningGenerator tile = (TileBurningGenerator)world.getTileEntity(x, y, z);
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
			TileBurningGenerator tileEntity = (TileBurningGenerator)world.getTileEntity(x, y, z);
			tileEntity.setEnergyStored(stack.stackTagCompound.getInteger("Energy"));
		}

		if(stack.hasDisplayName()) {
			((TileBurningGenerator)world.getTileEntity(x, y, z)).setGuiDisplayName(stack.getDisplayName());
		}
	}

	public static void updateFurnaceBlockState(boolean bool, World world, int x, int y, int z)
    {
        int l = world.getBlockMetadata(x, y, z);
        TileEntity tileentity = world.getTileEntity(x, y, z);

        if (bool)
        {
            world.setBlock(x, y, z, GLContent.blockBurningGenerator);
        }
        else
        {
            world.setBlock(x, y, z, GLContent.blockBurningGenerator);
        }

        world.setBlockMetadataWithNotify(x, y, z, l, 2);

        if (tileentity != null)
        {
            tileentity.validate();
            world.setTileEntity(x, y, z, tileentity);
        }
    }

	public void randomDisplayTick(World world, int x, int y, int z, Random random){
		TileBurningGenerator tile = (TileBurningGenerator)world.getTileEntity(x, y, z);
		if(tile.burnTimeRemaining > 0){
			int direction = world.getBlockMetadata(x, y, z);

			float x1 = (float)x + 0.5F;
			float y1 = (float)y + random.nextFloat();
			float z1 = (float)z + 0.5F;

			float f = 0.52F;
			float f1 = random.nextFloat() * 0.6F - 0.3F;

			if(direction == 4){
				world.spawnParticle("smoke", (double)(x1 - f), (double)(y1), (double)(z1 + f1), 0D, 0D, 0D);
				world.spawnParticle("flame", (double)(x1 - f), (double)(y1), (double)(z1 + f1), 0D, 0D, 0D);
			}

			if(direction == 5){
				world.spawnParticle("smoke", (double)(x1 + f), (double)(y1), (double)(z1 + f1), 0D, 0D, 0D);
				world.spawnParticle("flame", (double)(x1 + f), (double)(y1), (double)(z1 + f1), 0D, 0D, 0D);
			}

			if(direction == 2){
				world.spawnParticle("smoke", (double)(x1 + f1), (double)(y1), (double)(z1 - f), 0D, 0D, 0D);
				world.spawnParticle("flame", (double)(x1 + f1), (double)(y1), (double)(z1 - f), 0D, 0D, 0D);
			}

			if(direction == 3){
				world.spawnParticle("smoke", (double)(x1 + f1), (double)(y1), (double)(z1 + f), 0D, 0D, 0D);
				world.spawnParticle("flame", (double)(x1 + f1), (double)(y1), (double)(z1 + f), 0D, 0D, 0D);
			}
		}
	}
}
