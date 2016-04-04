package greglife.block;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import greglife.GregLife;
import greglife.tileentity.TileStorage;
import greglife.util.Utils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockStorage extends BlockContainer{

	public BlockStorage(String name){
		super(Material.iron);
		this.setStepSound(soundTypeMetal);
		this.setBlockName(name);
		this.setHarvestLevel("wrench", 3);
		this.setHardness(100.0F);
		this.setResistance(100.0F);
		this.setBlockTextureName("greglife:baseIcon_t3");
	}

	public TileEntity createNewTileEntity(World world, int meta){
		return new TileStorage();
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float f1, float f2, float f3){

		if(!player.isSneaking()){
			FMLNetworkHandler.openGui(player, GregLife.Instance, 5, world, x, y, z);
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
		TileStorage tileEntity = (TileStorage)world.getTileEntity(x, y, z);

		/*if (energyStored >= 1)
		{
			if (itemStack.getTagCompound() == null) {
				itemStack.setTagCompound(new NBTTagCompound());
			}
			itemStack.getTagCompound().setInteger("Energy", energyStored);
		}*/
		world.setBlockToAir(x, y, z);
		world.spawnEntityInWorld(entityItem);
	}

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityplayer, ItemStack stack){

		/*if(stack.stackTagCompound != null)
		{
			TileStorage tileEntity = (TileStorage)world.getTileEntity(x, y, z);
			tileEntity.readFromNBT(stack.stackTagCompound.getInteger("Energy"));
		}*/
	}
}
