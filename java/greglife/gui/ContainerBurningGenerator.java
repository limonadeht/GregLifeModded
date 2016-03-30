package greglife.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.tileentity.TileBurningGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBurningGenerator extends Container{

	private TileBurningGenerator generator;

	public int laseBurnTime;
	public int lastBurnTimeRemaining;

	public ContainerBurningGenerator(InventoryPlayer invPlayer, TileBurningGenerator tileentity){
		this.generator = tileentity;

		this.addSlotToContainer(new Slot(tileentity, 0, 152, 17));
		this.addSlotToContainer(new Slot(tileentity, 1, 152, 35));

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
		return generator.isUseableByPlayer(player);
	}

	public void addCraftingToCrafters(ICrafting iCrafting){
		super.addCraftingToCrafters(iCrafting);
		iCrafting.sendProgressBarUpdate(this, 0, this.generator.burnTimeRemaining);
		iCrafting.sendProgressBarUpdate(this, 1, this.generator.getEnergyStored());
	}

	public void detectAndSendChanges(){
		super.detectAndSendChanges();
		for(int i = 0; i < this.crafters.size(); i++){
			ICrafting iCrafting = (ICrafting)this.crafters.get(i);
			if(this.laseBurnTime != this.generator.burnTime){
				iCrafting.sendProgressBarUpdate(this, 0, this.generator.burnTime);
			}
			if(this.lastBurnTimeRemaining != this.generator.burnTimeRemaining){
				iCrafting.sendProgressBarUpdate(this, 1, this.generator.burnTimeRemaining);
			}
		}

		this.laseBurnTime = this.generator.burnTime;
		this.lastBurnTimeRemaining = this.generator.burnTimeRemaining;
	}

	@SideOnly(Side.CLIENT)
	public void updareProgressBar(int par1, int par2){
		if(par1 == 0){
			this.generator.burnTime = par2;
		}
		if(par1 == 1){
			this.generator.burnTimeRemaining = par2;
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int clickedIndex){
		return null;
	}

	/*private TileBurningGenerator generator;

	public int laseBurnTime;
	public int lastBurnTimeRemaining;

	public ContainerBurningGenerator(InventoryPlayer invPlayer, TileBurningGenerator tileentity){
		this.generator = tileentity;

		this.addSlotToContainer(new Slot(tileentity, 0, 62, 66));
		this.addSlotToContainer(new Slot(tileentity, 1, 98, 66));

		for (int i = 0; i < 3; i++) {
			for(int k = 0; k < 9; k++) {
				this.addSlotToContainer(new Slot(invPlayer, k + i * 9 + 9, 8 + k * 18, 88 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 151));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player){
		return generator.isUseableByPlayer(player);
	}

	public void addCraftingToCrafters(ICrafting iCrafting){
		super.addCraftingToCrafters(iCrafting);
		iCrafting.sendProgressBarUpdate(this, 0, this.generator.burnTimeRemaining);
		iCrafting.sendProgressBarUpdate(this, 1, this.generator.getEnergyStored());
	}

	public void detectAndSendChanges(){
		super.detectAndSendChanges();
		for(int i = 0; i < this.crafters.size(); i++){
			ICrafting iCrafting = (ICrafting)this.crafters.get(i);
			if(this.laseBurnTime != this.generator.burnTime){
				iCrafting.sendProgressBarUpdate(this, 0, this.generator.burnTime);
			}
			if(this.lastBurnTimeRemaining != this.generator.burnTimeRemaining){
				iCrafting.sendProgressBarUpdate(this, 1, this.generator.burnTimeRemaining);
			}
		}

		this.laseBurnTime = this.generator.burnTime;
		this.lastBurnTimeRemaining = this.generator.burnTimeRemaining;
	}

	@SideOnly(Side.CLIENT)
	public void updareProgressBar(int par1, int par2){
		if(par1 == 0){
			this.generator.burnTime = par2;
		}
		if(par1 == 1){
			this.generator.burnTimeRemaining = par2;
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int clickedIndex){
		return null;
	}*/
}
