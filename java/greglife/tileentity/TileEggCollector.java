package greglife.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEggCollector extends TileEntityBase implements IInventory{

	private ItemStack egg;
    private int facing = 2;
    private int progress;

    @Override
    public void updateEntity(){
        if(++progress >= 100){
        //if(++progress >= 7111){
        //if(++progress >= 300){
            if(egg == null)
                egg = new ItemStack(Items.egg, 1, 0);
            else if(egg.getItem() == Items.egg && egg.getItemDamage() == 0 && egg.stackSize < 16)
                egg.stackSize++;
            progress = 0;
            markDirty();
        }
    }

    public int getProgress(){
    	return this.progress;
    }

    public void setProgress(int i){
    	this.progress += i;
    }

    public int getFacing(){
        return facing;
    }

    public void setFacing(int dir){
        facing = dir;
    }

    @Override
	public void readCustomNBT(NBTTagCompound tag, boolean descPacket)
    {
        this.egg = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Egg"));
        this.progress = tag.getInteger("Progress");
        this.facing = tag.getShort("Facing");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound tag, boolean descPacket)
    {
        tag.setInteger("Progress", this.progress);
        tag.setShort("Facing", (short) this.facing);
        if(egg != null) {
            NBTTagCompound produce = new NBTTagCompound();
            egg.writeToNBT(produce);
            tag.setTag("Egg", produce);
        }
        else
            tag.removeTag("Egg");
    }

    @Override
    public int getSizeInventory()
    {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot){
        return egg;
    }

    @Override
    public ItemStack decrStackSize(int slot, int decrement){
        if(egg == null)
            return null;
        else {
            if(decrement < egg.stackSize){
                ItemStack take = egg.splitStack(decrement);
                if(egg.stackSize <= 0)
                    egg = null;
                return take;
            }
            else {
                ItemStack take = egg;
                egg = null;
                return take;
            }
        }
    }

    @Override
    public void openInventory() {}
    @Override
    public void closeInventory() {}

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack){
        return false;
    }

    @Override
    public int getInventoryStackLimit(){
        return 64;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack){
        egg = stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot){
        return null;
    }

    /**
     * Returns the name of the inventory
     */
    @Override
    public String getInventoryName()
    {
        return  "container.collector.egg";
    }

    /**
     * Returns if the inventory is named
     */
    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }
}
