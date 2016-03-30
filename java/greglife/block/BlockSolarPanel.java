package greglife.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.tileentity.TileSolarPanel;
import greglife.util.Utils;
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
import net.minecraft.world.World;

public class BlockSolarPanel extends BlockContainer{

	@SideOnly(Side.CLIENT)
	private IIcon top;

	public final int energyGeneration;
	public final int energyTransfer;
	public final int energyCapacity;

	public BlockSolarPanel(String name, int energyGeneration){
		super(Material.iron);
		this.setBlockName(name);
		this.setResistance(100.0F);
		this.setHardness(10.0F);
		this.setCreativeTab(greglife.GregLife.GLTab);
		this.energyGeneration = energyGeneration;
		this.energyTransfer = (energyGeneration * 2);
		this.energyCapacity = (energyGeneration * 1000);
		this.setStepSound(soundTypeMetal);

		this.setHarvestLevel("pickaxe", 3);
		//this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3F, 1.0F);
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister){
		this.blockIcon = iconregister.registerIcon("greglife:baseIcon_t1");
		this.top = iconregister.registerIcon("greglife:solar_top_t1");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta){
		return side == 1 ? this.top : (side == 0 ? this.blockIcon/*底*/ : (side != meta ? this.blockIcon : this.blockIcon));
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
		if(itemStack.stackTagCompound != null){
			return itemStack.stackTagCompound.getInteger("Energy");
		}
		return 0;
	}

	public int getMaxEnergyStored(ItemStack itemStack){
		return this.energyCapacity;
	}

	public TileEntity createNewTileEntity(World world, int meta){
		return new TileSolarPanel(this.energyGeneration, this.energyTransfer, this.energyCapacity);
	}

	public boolean onBlockEventReceived(World world, int x, int y, int z, int eventNumber, int eventArgument)
	{
		super.onBlockEventReceived(world, x, y, z, eventNumber, eventArgument);

		TileEntity tileentity = world.getTileEntity(x, y, z);
		if (tileentity != null) {
			return tileentity.receiveClientEvent(eventNumber, eventArgument);
		}
		return false;
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float f1, float f2, float f3)
	  {
		  if (player.getCurrentEquippedItem() != null) {
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

	  public void dismantleBlock(World world, int x, int y, int z)
	  {
	    ItemStack itemStack = new ItemStack(this);
	    float motion = 0.7F;
	    double motionX = world.rand.nextFloat() * motion + (1.0F - motion) * 0.5D;
	    double motionY = world.rand.nextFloat() * motion + (1.0F - motion) * 0.5D;
	    double motionZ = world.rand.nextFloat() * motion + (1.0F - motion) * 0.5D;
	    EntityItem entityItem = new EntityItem(world, x + motionX, y + motionY, z + motionZ, itemStack);
	    TileSolarPanel tileEntity = (TileSolarPanel)world.getTileEntity(x, y, z);
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

	  public static class Advanced extends BlockSolarPanel{

		  public Advanced(String name, int energyGeneration){
			  super(name,energyGeneration);
		  }

		  @SideOnly(Side.CLIENT)
		  IIcon top1;

		  @SideOnly(Side.CLIENT)
		  public void registerBlockIcons(IIconRegister iconregister){
			  this.blockIcon = iconregister.registerIcon("greglife:baseIcon_t2");
			  this.top1 = iconregister.registerIcon("greglife:solar_top_t2");
		  }

		  @SideOnly(Side.CLIENT)
		  public IIcon getIcon(int side, int meta){
			  return side == 1 ? this.top1 : (side == 0 ? this.blockIcon/*底*/ : (side != meta ? this.blockIcon : this.blockIcon));
		  }

		  public TileEntity createNewTileEntity(World world, int meta){
			  return new TileSolarPanel.Advanced(this.energyGeneration, this.energyTransfer, this.energyCapacity);
		  }

		  public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack){
			  if(itemStack.stackTagCompound != null){
				  TileSolarPanel tileEntity = (TileSolarPanel)world.getTileEntity(x, y, z);
				  tileEntity.setEnergyStored(itemStack.stackTagCompound.getInteger("Energy"));
			  }
		  }
	  }

	  public static class GregLife extends BlockSolarPanel{

		  public GregLife(String name, int energyGeneration){
			  super(name,energyGeneration);
		  }

		  @SideOnly(Side.CLIENT)
		  IIcon top2;

		  @SideOnly(Side.CLIENT)
		  public void registerBlockIcons(IIconRegister iconregister){
			  this.blockIcon = iconregister.registerIcon("greglife:baseIcon_t3");
			  this.top2 = iconregister.registerIcon("greglife:solar_top_t3");
		  }

		  @SideOnly(Side.CLIENT)
		  public IIcon getIcon(int side, int meta){
			  return side == 1 ? this.top2 : (side == 0 ? this.blockIcon/*底*/ : (side != meta ? this.blockIcon : this.blockIcon));
		  }

		  public TileEntity createNewTileEntity(World world, int meta){
			  return new TileSolarPanel.GregLife(this.energyGeneration, this.energyTransfer, this.energyCapacity);
		  }

		  public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack){
			  if(itemStack.stackTagCompound != null){
				  TileSolarPanel tileEntity = (TileSolarPanel)world.getTileEntity(x, y, z);
				  tileEntity.setEnergyStored(itemStack.stackTagCompound.getInteger("Energy"));
			  }
		  }
	  }
}
