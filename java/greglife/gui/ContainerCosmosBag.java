package greglife.gui;

import greglife.GLContent;
import greglife.gui.GLSlot.ICallbackContainer;
import greglife.item.ItemCosmosBag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerCosmosBag extends Container implements ICallbackContainer{

	private World worldObj;
	private int blockedSlot;
	public IInventory input;
	ItemStack bag = null;
	EntityPlayer player = null;
	public final int internalSlots;

	public ContainerCosmosBag(InventoryPlayer invPlayer, World world){
		this.worldObj = world;
		this.player = invPlayer.player;
		this.bag = invPlayer.getCurrentItem();
		this.internalSlots = ((ItemCosmosBag)this.bag.getItem()).getInternalSlots(bag);
		this.input = new InventoryStorageItem(this, bag);
		this.blockedSlot = (invPlayer.currentItem + 27 + internalSlots);

		/*COSMOS BAG SLOT*/
		int g = 0;
		for(int i = 0; i < 7; i++){
			for(int k = 0; k < 13; k++){
				this.addSlotToContainer(new GLSlot(this, this.input, g++, 8 + k * 18, 6 + i * 18));
			}
		}

		/*PLAYER SLOT(UP)*/
		for (int i = 0; i < 3; i++) {
			for(int k = 0; k < 9; k++) {
				this.addSlotToContainer(new Slot(invPlayer, k + i * 9 + 9, 44 + k * 18, 143 + i * 18));
			}
		}

		/*PLAYER HOTBAR SLOT(DOWN)*/
		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(invPlayer, i, 44 + i * 18, 201));
		}

		if (!world.isRemote)
			try {
				((InventoryStorageItem)this.input).stackList = ((ItemCosmosBag)this.bag.getItem()).getContainedItems(this.bag);
			}
		catch (Exception e)
		{
			//e.printStackTrace();
		}
		this.onCraftMatrixChanged(this.input);
	}

	@Override
	public boolean canInsert(ItemStack stack, int slotNumer, Slot slotObject)
	{
		if(GLContent.itemCosmosBag.equals(stack.getItem())){
			return false;
		}
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slot)
	{
		ItemStack stack = null;
		Slot slotObject = (Slot) inventorySlots.get(slot);

		if(slotObject != null && slotObject.getHasStack())
		{
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();

			if(slot < internalSlots)
			{
				if(!this.mergeItemStack(stackInSlot, internalSlots, (internalSlots + 36), true))
					return null;
			}
			else if(stackInSlot!=null)
			{
				boolean b = true;
				for(int i=0; i<internalSlots; i++)
				{
					Slot s = (Slot)inventorySlots.get(i);
					if(s!=null && s.isItemValid(stackInSlot))
						if(this.mergeItemStack(stackInSlot, i, i+1, true))
						{
							b = false;
							break;
						}
						else
							continue;
				}
				if(b)
					return null;
			}

			if (stackInSlot.stackSize == 0)
				slotObject.putStack(null);
			else
				slotObject.onSlotChanged();

			if (stackInSlot.stackSize == stack.stackSize)
				return null;
			slotObject.onPickupFromSlot(player, stack);
		}
		return stack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}

	@Override
	public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer)
	{
		if(par1 == this.blockedSlot || (par3!=0&&par2==par4EntityPlayer.inventory.currentItem))
			return null;
		((ItemCosmosBag)this.bag.getItem()).setContainedItems(this.bag, ((InventoryStorageItem)this.input).stackList);

		return super.slotClick(par1, par2, par3, par4EntityPlayer);
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);
		if (!this.worldObj.isRemote)
		{
			((ItemCosmosBag)this.bag.getItem()).setContainedItems(this.bag, ((InventoryStorageItem)this.input).stackList);
			ItemStack hand = this.player.getCurrentEquippedItem();
			if(hand!=null&&!this.bag.equals(hand))
				this.player.setCurrentItemOrArmor(0, this.bag);
			this.player.inventory.markDirty();
		}
	}

	@Override
	public boolean canTake(ItemStack stack, int slotNumer, Slot slotObject) {
		return true;
	}
}
