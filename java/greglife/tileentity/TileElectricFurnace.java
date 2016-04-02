package greglife.tileentity;

import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.GLContent;
import greglife.block.BlockElectricFurnace;
import greglife.energy.EnergyStorage;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileElectricFurnace extends TileEntityBase implements IEnergyHandler, ISidedInventory{

	private EnergyStorage energyStorage;
	private int energyCapacity;
	private final int energyPerSmelt = 1000;

	private static final int[] slotsTop = new int[]{0};
	private static final int[] upgrade_slot = new int[]{1};
	private static final int[] slotsResult = new int[]{2};
	private ItemStack[] furnaceItemStacks = new ItemStack[3];

	public int burnTime = 1;
	public int burnTimeRemaining = 0;
	public int cookTime;
	private int burnSpeed = 1;
	public final int isUpgrableBurnSpeed = 300;

	public boolean isBurning = false;
	public boolean isBurningCach = false;

	private int tick;

	public TileElectricFurnace(){
		this(0,0);
	}

	public TileElectricFurnace(int energyCapacity, int energyTransfer){
		this.energyCapacity = energyCapacity;
		this.energyStorage = new EnergyStorage(energyCapacity, energyTransfer);
	}

	public void updateEntity()
    {
        boolean flag = this.getEnergyStored() > 1000;
        boolean flag1 = false;

        if(!this.worldObj.isRemote){
            if(this.burnTime != 0 || this.furnaceItemStacks[1] != null && this.furnaceItemStacks[0] != null){
                if(this.burnTime == 0 && this.canSmelt()){
                    this.burnTimeRemaining = this.burnTime = getItemBurnTime(this.furnaceItemStacks[1]);

                    if(this.burnTime > 0){
                        flag1 = true;

                        /*if(this.furnaceItemStacks[1] != null){
                            --this.furnaceItemStacks[1].stackSize;

                            if(this.furnaceItemStacks[1].stackSize == 0){
                                this.furnaceItemStacks[1] = furnaceItemStacks[1].getItem().getContainerItem(furnaceItemStacks[1]);
                            }
                        }*/
                    }
                }

                if(this.isBurning() && this.canSmelt()){
                    ++this.cookTime;
                    int en = this.getEnergyStored();
                    --en;
                    if(this.cookTime == this.burnSpeed){
                        this.cookTime = 0;
                        this.smeltItem();
                        flag1 = true;
                    }
                }else{
                    this.cookTime = 0;
                }
            }

            if(this.energyStorage.getEnergyStored() > 0){
    			BlockElectricFurnace.updateFurnaceBlockState(this.energyStorage.getEnergyStored() > 0, worldObj, xCoord, yCoord, zCoord);
    		}

            detectAndSentChanges(tick % 500 == 0);
    		tick++;
        }

        if (flag1){
            this.markDirty();
        }
    }

	public boolean isUpgrade(){
		if(this.getStackInSlot(1) != null){
			if(this.getStackInSlot(1).getItem() == GLContent.itemUpgradeEnergy){
				return true;
			}
		}
		return false;
	}

	public void setBurnTime(int par1){
		this.burnTime += par1;
	}

	public void setBurnTimeRemain(int par1){
		this.burnTimeRemaining += par1;
	}

	private void detectAndSentChanges(boolean sendAnyway){
		if (isBurning != isBurningCach || sendAnyway) isBurningCach = isBurning;
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket){
		this.energyStorage.readFromNBT(nbt);

		NBTTagCompound[] tag = new NBTTagCompound[furnaceItemStacks.length];
		for(int i = 0; i < furnaceItemStacks.length; i++){
			tag[i] = nbt.getCompoundTag("Item" + i);
			furnaceItemStacks[i] = ItemStack.loadItemStackFromNBT(tag[i]);
		}
		this.burnTime = nbt.getInteger("BurnTime");
		this.burnTimeRemaining = nbt.getInteger("BurnTimeRemaining");
		this.cookTime = nbt.getInteger("CookTime");
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket){
		this.energyStorage.writeToNBT(nbt);

		NBTTagCompound[] tag = new NBTTagCompound[furnaceItemStacks.length];
		for(int i = 0; i < furnaceItemStacks.length; i++){
			tag[i] = new NBTTagCompound();
			if(this.furnaceItemStacks[i] != null){
				tag[i] = furnaceItemStacks[i].writeToNBT(tag[i]);
			}
			nbt.setTag("Item" + i, tag[i]);
		}
		nbt.setInteger("BurnTime", this.burnTime);
		nbt.setInteger("BurnTimeRemaining", this.burnTimeRemaining);
		nbt.setShort("CookTime", (short)this.cookTime);
	}

	public boolean canConnectEnergy(ForgeDirection from){
		return true;
	}

	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate){
		return this.energyStorage.receiveEnergy(maxReceive, simulate);
	}

	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate){
		return 0;
	}

	public int getEnergyStored(ForgeDirection from){
		return this.energyStorage.getEnergyStored();
	}

	public int getMaxEnergyStored(ForgeDirection from){
		return this.energyStorage.getMaxEnergyStored();
	}

	public void setEnergyStored(int energyStored){
		this.energyStorage.setEnergyStored(energyStored);
	}

	public int getEnergyStored(){
		return getEnergyStored(ForgeDirection.DOWN);
	}

	public int getMaxEnergyStored(){
		return getMaxEnergyStored(ForgeDirection.DOWN);
	}

	public int getSizeInventory()
    {
        return this.furnaceItemStacks.length;
    }

	@Override
	public ItemStack getStackInSlot(int par1){
		return this.furnaceItemStacks[par1];
	}

	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_)
    {
        if (this.furnaceItemStacks[p_70298_1_] != null)
        {
            ItemStack itemstack;

            if (this.furnaceItemStacks[p_70298_1_].stackSize <= p_70298_2_)
            {
                itemstack = this.furnaceItemStacks[p_70298_1_];
                this.furnaceItemStacks[p_70298_1_] = null;
                return itemstack;
            }
            else
            {
                itemstack = this.furnaceItemStacks[p_70298_1_].splitStack(p_70298_2_);

                if (this.furnaceItemStacks[p_70298_1_].stackSize == 0)
                {
                    this.furnaceItemStacks[p_70298_1_] = null;
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

	public ItemStack getStackInSlotOnClosing(int p_70304_1_)
    {
        if (this.furnaceItemStacks[p_70304_1_] != null)
        {
            ItemStack itemstack = this.furnaceItemStacks[p_70304_1_];
            this.furnaceItemStacks[p_70304_1_] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_)
    {
        this.furnaceItemStacks[p_70299_1_] = p_70299_2_;

        if (p_70299_2_ != null && p_70299_2_.stackSize > this.getInventoryStackLimit())
        {
            p_70299_2_.stackSize = this.getInventoryStackLimit();
        }
    }

	public String getInventoryName()
    {
        return "container.furnace.electric";
    }

	public int getInventoryStackLimit()
    {
        return 64;
    }

	@SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int p_145953_1_)
    {
        return this.cookTime * p_145953_1_ / 200;
    }

	public boolean isBurning()
    {
        return this.burnTime > 0;
    }

	public boolean isCanSmelt(){
		return this.getEnergyStored() > 0;
	}

	private boolean canSmelt()
    {
        if (this.furnaceItemStacks[0] == null){
            return false;
        }else{
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);
            if (itemstack == null) return false;
            if (this.furnaceItemStacks[2] == null) return true;
            if (!this.furnaceItemStacks[2].isItemEqual(itemstack)) return false;
            int result = furnaceItemStacks[2].stackSize + itemstack.stackSize;
            return result <= getInventoryStackLimit() && result <= this.furnaceItemStacks[2].getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
        }
    }

	public void smeltItem()
    {
        if(this.canSmelt() && this.getEnergyStored() > this.energyPerSmelt){
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);

            if(this.furnaceItemStacks[2] == null){
                this.furnaceItemStacks[2] = itemstack.copy();
            }
            else if(this.furnaceItemStacks[2].getItem() == itemstack.getItem()){
                this.furnaceItemStacks[2].stackSize += itemstack.stackSize;
                this.setEnergyStored(this.getEnergyStored() - this.energyPerSmelt);
            }

            --this.furnaceItemStacks[0].stackSize;

            if(this.furnaceItemStacks[0].stackSize <= 0){
                this.furnaceItemStacks[0] = null;
            }
        }
    }

	public static int getItemBurnTime(ItemStack p_145952_0_){
        if(p_145952_0_ == null){
            return 0;
        }else{
            Item item = p_145952_0_.getItem();

            if(item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air){
                Block block = Block.getBlockFromItem(item);

                if(block == Blocks.wooden_slab){
                    return 150;
                }

                if(block.getMaterial() == Material.wood){
                    return 300;
                }

                if(block == Blocks.coal_block){
                    return 16000;
                }
            }

            if (item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemHoe && ((ItemHoe)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item == Items.stick) return 100;
            if (item == Items.coal) return 1600;
            if (item == Items.lava_bucket) return 20000;
            if (item == Item.getItemFromBlock(Blocks.sapling)) return 100;
            if (item == Items.blaze_rod) return 2400;
            return GameRegistry.getFuelValue(p_145952_0_);
        }
    }

	public static boolean isItemFuel(ItemStack p_145954_0_){
        return getItemBurnTime(p_145954_0_) > 0;
    }

	public boolean isUseableByPlayer(EntityPlayer p_70300_1_){
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : p_70300_1_.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

	public void openInventory() {}

    public void closeInventory() {}

    public boolean isItemValidForSlot(int i, ItemStack stack){
        return i == 2 ? false : (i == 1 ? isItemFuel(stack) : true);
    }

    @Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return var1 == 0 ? upgrade_slot : (var1 == 1 ? slotsTop : slotsResult);
	}

    @Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		return this.isItemValidForSlot(i, itemstack);
	}

    @Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		return j != 0 || i != 1 || itemstack.getItem() == Items.bucket;
	}

	@Override
	public boolean hasCustomInventoryName(){
		return true;
	}
}
