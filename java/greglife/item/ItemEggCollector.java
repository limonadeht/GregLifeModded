package greglife.item;

import java.util.List;

import greglife.GregLife;
import greglife.block.BlockEggCollector;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class ItemEggCollector extends ItemBlock{

	public ItemEggCollector(Block block){
		super(block);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool){

		BlockEggCollector collector = (BlockEggCollector)this.field_150939_a;
		list.add("The Egg Collector!");
		if(stack.stackTagCompound != null){
			list.add("Progress: " + stack.getTagCompound().getInteger("Progress") + " %");
		}else{
			list.add("Progress: <Empty> %");
		}

		if(GregLife.proxy.isShiftKeyDown()){
			list.add("-----------------------");
		}else{
			list.add(EnumChatFormatting.DARK_PURPLE + "§o--Press Shift for Info--");
		}
	}
}
