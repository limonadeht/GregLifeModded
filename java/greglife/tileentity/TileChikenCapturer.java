package greglife.tileentity;

import cofh.api.energy.IEnergyHandler;
import greglife.GLContent;
import greglife.energy.EnergyStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileChikenCapturer extends TileEntityBase implements IEnergyHandler, ISidedInventory{

	private EnergyStorage energyStorage;
	private int energyCapacity;
	private final int energyPerUse = 100000;

	private ItemStack chiken;
	private int progress;

	private static final int[] slots_upgrade = new int[]{0};
	private static final int[] slots_result = new int[]{1};
	private ItemStack[] itemStacks = new ItemStack[2];

	private int tick;

	public TileChikenCapturer(){
		this(0,0);
	}

	public TileChikenCapturer(int energyCapacity, int energyTransfer){
		this.energyCapacity = energyCapacity;
		this.energyStorage = new EnergyStorage(energyCapacity, energyTransfer);
	}

	@Override
    public void updateEntity(){
		if(++progress >= 100){
			if(chiken == null)
				chiken = new ItemStack(GLContent.itemChiken, 1, 0);
//			else if(this.itemStacks[1].stackSize < 64);
			this.itemStacks[1] = chiken.copy();

			progress = 0;
			this.markDirty();
		}
    }

	public boolean isUpgrade(){
		if(this.getStackInSlot(0) != null){
			if(this.getStackInSlot(0).getItem() == GLContent.itemUpgradeEnergy){
				return true;
			}
		}
		return false;
	}

	private boolean canCreateItemChiken(){
		if(this.itemStacks[0] == null){
			return false;
		}else{
			chiken = new ItemStack(GLContent.itemChiken, 1, 0);
			if(this.itemStacks[1] == null)return true;
			if(!this.itemStacks[1].isItemEqual(chiken)) return false;
			int result = this.itemStacks[1].stackSize + chiken.stackSize;
			return result <= this.getInventoryStackLimit() && result <= this.itemStacks[1].getMaxStackSize();
		}
	}

	public void createItemChiken(){
		if(this.canCreateItemChiken() && this.getEnergyStored() > this.energyPerUse){
			chiken = new ItemStack(GLContent.itemChiken, 1, 0);

			if(this.itemStacks[1] == null){
				this.itemStacks[1] = chiken.copy();
			}
			else if(this.itemStacks[1].getItem() == chiken.getItem()){
				this.itemStacks[1].stackSize += chiken.stackSize;
				this.setEnergyStored(this.getEnergyStored() - this.energyPerUse);
			}
		}
	}

    public int getProgress(){
    	return this.progress;
    }

    public void setProgress(int i){
    	this.progress += i;
    }

    @Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
    {
        this.chiken = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Chiken"));
        this.progress = nbt.getInteger("Progress");

        this.energyStorage.readFromNBT(nbt);

        NBTTagCompound[] tag = new NBTTagCompound[itemStacks.length];
		for(int i = 0; i < itemStacks.length; i++){
			tag[i] = nbt.getCompoundTag("Item" + i);
			itemStacks[i] = ItemStack.loadItemStackFromNBT(tag[i]);
		}
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
    {
        nbt.setInteger("Progress", this.progress);
        this.energyStorage.writeToNBT(nbt);

        NBTTagCompound[] tag = new NBTTagCompound[itemStacks.length];
		for(int i = 0; i < itemStacks.length; i++){
			tag[i] = new NBTTagCompound();
			if(this.itemStacks[i] != null){
				tag[i] = itemStacks[i].writeToNBT(tag[i]);
			}
			nbt.setTag("Item" + i, tag[i]);
		}

        if(chiken != null) {
            NBTTagCompound produce = new NBTTagCompound();
            chiken.writeToNBT(produce);
            nbt.setTag("Chiken", produce);
        }
        else
            nbt.removeTag("Chiken");
    }

    public ItemStack getUpgradeItemStacks(){
		return this.getStackInSlot(0);
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

	public NBTTagCompound getItemNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		energyStorage.writeToNBT(nbt);
		return nbt;
	}

	/*GUI, Inventory*/

	@SuppressWarnings("static-access")
	public int getSizeInventory() {
		return this.slots_result.length;
	}

	@Override
	public ItemStack getStackInSlot(int par1){
		return this.itemStacks[par1];
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2){
		ItemStack itemstack = this.getStackInSlot(par1);
		if(itemstack != null){
			if(itemstack.stackSize <= par2){
				this.setInventorySlotContents(par1, null);
			}else{
				itemstack = itemstack.splitStack(par2);
				if(itemstack.stackSize == 0){
					this.setInventorySlotContents(par1, null);
				}
			}
		}
		return itemstack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i){
		ItemStack item = this.getStackInSlot(i);
		if(item != null){
			this.setInventorySlotContents(i, null);
		}
		return item;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack){
		this.itemStacks[i] = itemstack;
		if(itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()){
			itemstack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public int getInventoryStackLimit(){
		return 64;
	}

	@Override
	public void openInventory(){}

	@Override
	public void closeInventory(){}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack stack){
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int par1){
		return new int[1];
	}

	@Override
	public boolean canInsertItem(int par1, ItemStack par2, int par3){
		return true;
	}

	@Override
	public boolean canExtractItem(int par1, ItemStack par2, int par3){
		return true;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player){
		if(this.worldObj == null){
			return true;
		}
		if(this.worldObj.getTileEntity(xCoord, yCoord, zCoord) != this){
			return false;
		}
		return player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.4) < 64;
	}

	@Override
	public String getInventoryName(){
		return "container.capturer.chiken";
	}

	@Override
	public boolean hasCustomInventoryName(){
		return false;
	}
}
