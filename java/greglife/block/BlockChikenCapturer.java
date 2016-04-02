package greglife.block;

import java.util.Random;

import greglife.GregLife;
import greglife.tileentity.TileChikenCapturer;
import greglife.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockChikenCapturer extends BlockContainer{

	public final int energyCapacity;
	public final int energyTransfer;

    private Random randy = new Random();

    public BlockChikenCapturer(String name, int energyCapacity){
        super(Material.iron);
        setStepSound(soundTypeMetal);
        setHardness(20.0F);
        setBlockName(name);
        setHarvestLevel("pickaxe", 3);
        setCreativeTab(GregLife.GLTab);
        this.setBlockTextureName("greglife:egg_collector");

        this.energyCapacity = energyCapacity;
        this.energyTransfer = 32768;
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

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
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

        if (world.isRemote)
        {
            return true;
        }
        else
        {
            player.openGui(GregLife.Instance, 4, world, x, y, z);
            return true;
        }
    }

    public void dismantleBlock(World world, int x, int y, int z){
		ItemStack itemStack = new ItemStack(this);
		float motion = 0.7F;
		double motionX = world.rand.nextFloat() * motion + (1.0F - motion) * 0.5D;
		double motionY = world.rand.nextFloat() * motion + (1.0F - motion) * 0.5D;
		double motionZ = world.rand.nextFloat() * motion + (1.0F - motion) * 0.5D;
		EntityItem entityItem = new EntityItem(world, x + motionX, y + motionY, z + motionZ, itemStack);
		TileChikenCapturer tileEntity = (TileChikenCapturer)world.getTileEntity(x, y, z);
		int progress = tileEntity.getProgress();
		int energyStored = tileEntity.getEnergyStored();

		if (progress >= 1)
		{
			if (itemStack.getTagCompound() == null) {
				itemStack.setTagCompound(new NBTTagCompound());
			}
			itemStack.getTagCompound().setInteger("Progress", progress);
		}

		if (energyStored >= 1)
		{
			if (itemStack.getTagCompound() == null) {
				itemStack.setTagCompound(new NBTTagCompound());
			}
			itemStack.getTagCompound().setInteger("Energy", energyStored);
		}

		TileChikenCapturer collector = (TileChikenCapturer)world.getTileEntity(x, y, z);

        if (collector != null)
        {
            ItemStack itemstack = collector.getStackInSlot(0);

            if (itemstack != null)
            {
                float f = this.randy.nextFloat() * 0.8F + 0.1F;
                float f1 = this.randy.nextFloat() * 0.8F + 0.1F;
                float f2 = this.randy.nextFloat() * 0.8F + 0.1F;

                while (itemstack.stackSize > 0)
                {
                    int j1 = this.randy.nextInt(21) + 10;

                    if (j1 > itemstack.stackSize)
                    {
                        j1 = itemstack.stackSize;
                    }

                    itemstack.stackSize -= j1;
                    EntityItem entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                    if (itemstack.hasTagCompound())
                    {
                        entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                    }

                    float f3 = 0.05F;
                    entityitem.motionX = (double)((float)this.randy.nextGaussian() * f3);
                    entityitem.motionY = (double)((float)this.randy.nextGaussian() * f3 + 0.2F);
                    entityitem.motionZ = (double)((float)this.randy.nextGaussian() * f3);
                    world.spawnEntityInWorld(entityitem);
                }
            }
        }
		world.setBlockToAir(x, y, z);
		world.spawnEntityInWorld(entityItem);
	}

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileChikenCapturer(this.energyCapacity, this.energyTransfer);
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityplayer, ItemStack stack){
		TileChikenCapturer tile = (TileChikenCapturer)world.getTileEntity(x, y, z);

		if(stack.stackTagCompound != null)
		{
			TileChikenCapturer tileEntity = (TileChikenCapturer)world.getTileEntity(x, y, z);
			tileEntity.setEnergyStored(stack.stackTagCompound.getInteger("Energy"));
		}

		if(stack.stackTagCompound != null)
		{
			TileChikenCapturer tileEntity = (TileChikenCapturer)world.getTileEntity(x, y, z);
			tileEntity.setProgress(stack.stackTagCompound.getInteger("Progress"));
		}
	}

    public void breakBlock(World world, int x, int y, int z, Block block, int wut)
    {
            TileChikenCapturer collector = (TileChikenCapturer)world.getTileEntity(x, y, z);

            if (collector != null)
            {
                ItemStack itemstack = collector.getStackInSlot(0);

                if (itemstack != null)
                {
                    float f = this.randy.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.randy.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.randy.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0)
                    {
                        int j1 = this.randy.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize)
                        {
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        EntityItem entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)this.randy.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)this.randy.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)this.randy.nextGaussian() * f3);
                        world.spawnEntityInWorld(entityitem);
                    }
                }

                world.func_147453_f(x, y, z, block);
            }

        super.breakBlock(world, x, y, z, block, wut);
    }
}
