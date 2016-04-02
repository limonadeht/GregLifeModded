package greglife.item;

import java.util.List;

import greglife.GregLife;
import greglife.block.BlockEnergyCable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class ItemEnergyCable extends ItemBlock{

	public ItemEnergyCable(Block block){
		super(block);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool){

		BlockEnergyCable cable = (BlockEnergyCable)this.field_150939_a;
		list.add("The GabaGaba Energy Cable! by Noob Programmer");
		list.add(EnumChatFormatting.AQUA + "Not Loss! Good Cable! Fooo!!");
		if(GregLife.proxy.isShiftKeyDown()){
			list.add("-----------------------");
			list.add("Max Transfer: " + Integer.MAX_VALUE + " RF/t");
			list.add(EnumChatFormatting.GOLD + "Wonderful transfer rates!");
		}else{
			list.add(EnumChatFormatting.DARK_PURPLE + "§o--Press Shift for Info--");
		}
	}
}
