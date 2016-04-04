package greglife.gui;

import greglife.tileentity.TileStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerStorage extends Container{

	private TileStorage storage;

	public ContainerStorage(InventoryPlayer invPlayer, TileStorage tileentity){
		this.storage = tileentity;

		for(int k = 0; k < 9; k++){
			for(int i = 0; i < 13; i++){
				this.addSlotToContainer(new Slot(tileentity, 0, 11 + k * 18, 15 + i * 18));
			}
		}

		for (int i = 0; i < 3; i++) {
			for(int k = 0; k < 9; k++) {
				this.addSlotToContainer(new Slot(invPlayer, k + i * 9 + 9, 47 + k * 18, 174 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(invPlayer, i, 47 + i * 18, 232));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player){
		return storage.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int clickedIndex){
		return null;
	}
}
