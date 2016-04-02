package greglife.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.tileentity.TileChikenCapturer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class ContainerChikenCapturer extends Container{

	private TileChikenCapturer machine;

	public int lastEnergy;

	public ContainerChikenCapturer(InventoryPlayer invPlayer, TileChikenCapturer tile){
		this.machine = tile;

		this.addSlotToContainer(new Slot(tile, 0, 152, 17));
		this.addSlotToContainer(new SlotFurnace(invPlayer.player, tile, 1, 8, 74));

		/*for(int i = 0; i < 5; i++){
			this.addSlotToContainer(new SlotFurnace(invPlayer.player, tile, 1, 8 + i * 18, 74));
		}*/

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
		return machine.isUseableByPlayer(player);
	}

	public void addCraftingToCrafters(ICrafting iCrafting){
		super.addCraftingToCrafters(iCrafting);
		iCrafting.sendProgressBarUpdate(this, 0, this.machine.getEnergyStored());
	}

	public void detectAndSendChanges(){
		super.detectAndSendChanges();
		for(int i = 0; i < this.crafters.size(); i++){
			ICrafting iCrafting = (ICrafting)this.crafters.get(i);
			if(this.lastEnergy != this.machine.getEnergyStored()){
				iCrafting.sendProgressBarUpdate(this, 0, this.machine.getEnergyStored());
			}
		}

		this.lastEnergy = this.machine.getEnergyStored();
	}

	@SideOnly(Side.CLIENT)
	public void updareProgressBar(int par1, int par2){
		if(par1 == 0){
			int furnaceStored = this.machine.getEnergyStored();
			furnaceStored = par2;
		}
	}

	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int clickedIndex){
		return null;
	}
}
