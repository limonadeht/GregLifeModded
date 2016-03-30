package greglife.item;

import java.util.List;

import greglife.GregLife;
import greglife.block.BlockBurningGenerator;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class ItemBurningGenerator extends ItemBlock{

	public ItemBurningGenerator(Block block){
		super(block);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool){

		BlockBurningGenerator blockBurn = (BlockBurningGenerator)this.field_150939_a;
		list.add("The Flammability Power!");
		list.add("Stored: " + blockBurn.getEnergyStored(stack) + " / " + blockBurn.getEnergyCapacity());

		if(GregLife.proxy.isShiftKeyDown()){
			list.add("-----------------------");
			list.add("Max Storage: " + blockBurn.getEnergyCapacity());
			list.add("Max Transfer: " + blockBurn.getEnergyTransfer());
			list.add("Max Generate: " + blockBurn.getEnergyGeneration() + " Rf/t");
			list.add("");
			list.add("If Upgradable for Speeeeed Burn and Total Generation Rf ++");
		}else{
			list.add(EnumChatFormatting.DARK_PURPLE + "§o--Press Shift for Info--");
		}
	}
}
