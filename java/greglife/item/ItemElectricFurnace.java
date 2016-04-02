package greglife.item;

import java.util.List;

import greglife.GregLife;
import greglife.block.BlockElectricFurnace;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class ItemElectricFurnace extends ItemBlock{

	public ItemElectricFurnace(Block block){
		super(block);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool){

		BlockElectricFurnace blockFurnace = (BlockElectricFurnace)this.field_150939_a;
		list.add("The Power Furnace!");
		list.add("Stored: " + blockFurnace.getEnergyStored(stack) + " / " + blockFurnace.getEnergyCapacity());
		if(stack.stackTagCompound != null){
			list.add("BurnTime: " + stack.getTagCompound().getInteger("BurnTime"));
			list.add("BurnTimeRemaining: " + stack.getTagCompound().getInteger("BurnTimeRemaining"));
		}else{
			list.add("BurnTime: <Empty>");
			list.add("BurnTimeRemaining: <Empty>");
		}
		if(GregLife.proxy.isShiftKeyDown()){
			list.add("-----------------------");
			list.add("Max Storage: " + blockFurnace.getEnergyCapacity());
			list.add("Max Transfer: " + blockFurnace.getEnergyTransfer());
		}else{
			list.add(EnumChatFormatting.DARK_PURPLE + "§o--Press Shift for Info--");
		}
	}
}
