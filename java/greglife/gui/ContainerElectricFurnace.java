package greglife.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.tileentity.TileElectricFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class ContainerElectricFurnace extends Container {

	private TileElectricFurnace furnace;

	public int lastBurnTime;
	public int lastBurnTimeRemaining;
	public int lastEnergy;
	public int lastCookTime;

	public ContainerElectricFurnace(InventoryPlayer invPlayer, TileElectricFurnace tile){
		this.furnace = tile;

		this.addSlotToContainer(new Slot(tile, 0, 152, 17));
		this.addSlotToContainer(new Slot(tile, 1, 152, 35));
		this.addSlotToContainer(new SlotFurnace(invPlayer.player, tile, 2, 152, 70));
		//this.addSlotToContainer(new Slot(tile, 2, 152, 70));

		for (int i = 0; i < 3; i++) {
			for(int k = 0; k < 9; k++) {
				this.addSlotToContainer(new Slot(invPlayer, k + i * 9 + 9, 8 + k * 18, 94 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 152));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player){
		return furnace.isUseableByPlayer(player);
	}

	public void addCraftingToCrafters(ICrafting iCrafting){
		super.addCraftingToCrafters(iCrafting);
		iCrafting.sendProgressBarUpdate(this, 0, this.furnace.burnTimeRemaining);
		iCrafting.sendProgressBarUpdate(this, 1, this.furnace.getEnergyStored());
		iCrafting.sendProgressBarUpdate(this, 2, this.furnace.cookTime);
		iCrafting.sendProgressBarUpdate(this, 3, this.furnace.burnTime);
	}

	public void detectAndSendChanges(){
		super.detectAndSendChanges();
		for(int i = 0; i < this.crafters.size(); i++){
			ICrafting iCrafting = (ICrafting)this.crafters.get(i);
			if(this.lastBurnTimeRemaining != this.furnace.burnTime){
				iCrafting.sendProgressBarUpdate(this, 0, this.furnace.burnTimeRemaining);
			}
			if(this.lastEnergy != this.furnace.getEnergyStored()){
				iCrafting.sendProgressBarUpdate(this, 1, this.furnace.getEnergyStored());
			}
			if(this.lastCookTime != this.furnace.cookTime){
				iCrafting.sendProgressBarUpdate(this, 2, this.furnace.cookTime);
			}
			if(this.lastBurnTime != this.furnace.burnTime){
				iCrafting.sendProgressBarUpdate(this, 3, this.furnace.burnTime);
			}
		}

		this.lastBurnTimeRemaining = this.furnace.burnTimeRemaining;
		this.lastEnergy = this.furnace.getEnergyStored();
		this.lastCookTime = this.furnace.cookTime;
		this.lastBurnTime = this.furnace.burnTime;
	}

	@SideOnly(Side.CLIENT)
	public void updareProgressBar(int par1, int par2){
		if(par1 == 0){
			this.furnace.burnTimeRemaining = par2;
		}
		if(par1 == 1){
			int furnaceStored = this.furnace.getEnergyStored();
			furnaceStored = par2;
		}
		if(par1 == 2){
			this.furnace.cookTime = par2;
		}
		if(par1 == 3){
			this.furnace.burnTime = par2;
		}
	}

	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 == 2) {
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (par2 != 1 && par2 != 0) {
                if (FurnaceRecipes.smelting().getSmeltingResult(itemstack1) != null) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return null;
                    }
                }else if (TileElectricFurnace.isItemFuel(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false))
                    {
                        return null;
                    }
                }else if (par2 >= 3 && par2 < 30){
                    if (!this.mergeItemStack(itemstack1, 30, 39, false)){
                        return null;
                    }
                }else if (par2 >= 30 && par2 < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack)null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        }

        return itemstack;
    }

	/*@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int clickedIndex){
		return null;
	}*/
}
