package greglife.item;

import java.util.List;

import greglife.GregLife;
import greglife.block.BlockSolarPanel;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class ItemSolarPanel extends ItemBlock{

	public ItemSolarPanel(Block block){
		super(block);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool){

		BlockSolarPanel solarPanel = (BlockSolarPanel)this.field_150939_a;
		list.add("Solar Power!");
		list.add("Stored: " + solarPanel.getEnergyStored(stack) + " / " + solarPanel.getMaxEnergyStored(stack));

		if(GregLife.proxy.isShiftKeyDown()){
			list.add("-----------------------");
			list.add("Max Storage: " + solarPanel.getMaxEnergyStored(stack));
			list.add("Max Transfer: " + solarPanel.getEnergyTransfer());
			list.add("Max Generate: " + solarPanel.getEnergyGeneration() + " Rf/t");
		}else{
			list.add(EnumChatFormatting.DARK_PURPLE + "§o--Press Shift for Info--");
		}
	}
}
