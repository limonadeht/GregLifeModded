package greglife.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileStorage extends TileEntityBase implements ISidedInventory{

	ItemStack[] itemStacks = new ItemStack[117];

	public TileStorage(){

	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket){
		NBTTagCompound[] tag = new NBTTagCompound[itemStacks.length];
		for(int i = 0; i < itemStacks.length; i++){
			tag[i] = nbt.getCompoundTag("Item" + i);
			itemStacks[i] = ItemStack.loadItemStackFromNBT(tag[i]);
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket){
		NBTTagCompound[] tag = new NBTTagCompound[itemStacks.length];
		for(int i = 0; i < itemStacks.length; i++){
			tag[i] = new NBTTagCompound();
			if(this.itemStacks[i] != null){
				tag[i] = itemStacks[i].writeToNBT(tag[i]);
			}
			nbt.setTag("Item" + i, tag[i]);
		}
	}

	public String getInventoryName() {
		return "container.tileStorage";
	}

	@Override
	public int getSizeInventory(){
		return this.itemStacks.length;
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
	public boolean hasCustomInventoryName(){
		return false;
	}

	@Override
	public int getInventoryStackLimit(){
		return 64;
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
	public void openInventory(){}

	@Override
	public void closeInventory(){}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_){
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
}
