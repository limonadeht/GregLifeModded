package greglife.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.tileentity.TileAdvancedNeutron;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class ContainerAdvancedNeutron extends Container{

	private TileAdvancedNeutron machine;

	public int lastProgress;

	public ContainerAdvancedNeutron(InventoryPlayer invPlayer, TileAdvancedNeutron tileentity){
		this.machine = tileentity;

		this.addSlotToContainer(new SlotFurnace(invPlayer.player, tileentity, 0, 152, 17));

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
		return true;
	}

	public void addCraftingToCrafters(ICrafting iCrafting){
		super.addCraftingToCrafters(iCrafting);
		iCrafting.sendProgressBarUpdate(this, 0, this.machine.getProgress());
	}

	public void detectAndSendChanges(){
		super.detectAndSendChanges();
		for(int i = 0; i < this.crafters.size(); i++){
			ICrafting iCrafting = (ICrafting)this.crafters.get(i);
			if(this.lastProgress != this.machine.getProgress()){
				iCrafting.sendProgressBarUpdate(this, 0, this.machine.getProgress());
			}
		}

		this.lastProgress = this.machine.getProgress();
	}

	@SuppressWarnings("unused")
	@SideOnly(Side.CLIENT)
	public void updareProgressBar(int par1, int par2){
		if(par1 == 0){
			int progress = this.machine.getProgress();
			progress = par2;
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int clickedIndex){
		return null;
	}
}
