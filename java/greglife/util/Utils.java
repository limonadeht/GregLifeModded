package greglife.util;

import org.lwjgl.input.Keyboard;

import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.ModAPIManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class Utils {

	public static boolean hasUsableWrench(EntityPlayer player, int x, int y, int z)
	  {
	    ItemStack tool = player.getCurrentEquippedItem();
	    if ((ModAPIManager.INSTANCE.hasAPI("BuildCraftAPI|tools")) && ((tool.getItem() instanceof IToolWrench)) && (((IToolWrench)tool.getItem()).canWrench(player, x, y, z))) {
	      return true;
	    }
	    return false;
	  }

	  public static boolean isControlKeyDown()
	  {
	    return (Keyboard.isKeyDown(29)) || (Keyboard.isKeyDown(157));
	  }

	  public static String localize(String string)
	  {
	    return StatCollector.translateToLocal(string);
	  }

	  public static String shiftForDetails()
	  {
	    return EnumChatFormatting.GRAY + localize("info.fmr.shiftkey.hold") + " " + EnumChatFormatting.AQUA + EnumChatFormatting.ITALIC + localize("info.fmr.shiftkey") + " " + EnumChatFormatting.GRAY + localize("info.fmr.shiftkey.down");
	  }

	  public static String controlForDetails()
	  {
	    return EnumChatFormatting.GRAY + localize("info.fmr.controlkey.hold") + " " + EnumChatFormatting.AQUA + EnumChatFormatting.ITALIC + localize("info.fmr.controlkey") + " " + EnumChatFormatting.GRAY + localize("info.fmr.controlkey.down");
	  }

	  /*public static String addEnergyInfo(ItemStack itemStack)
	  {
	    IEnergyContainerItem energyItem = (IEnergyContainerItem)itemStack.getItem();
	    int energy = energyItem.getEnergyStored(itemStack);
	    int maxEnergy = energyItem.getMaxEnergyStored(itemStack);
	    if (energy > maxEnergy / 4 * 3) {
	      return String.format(EnumChatFormatting.DARK_GREEN + "%,d / %,d", new Object[] { Integer.valueOf(energy), Integer.valueOf(maxEnergy) });
	    }
	    if ((energy <= maxEnergy / 4 * 3) && (energy > maxEnergy / 4)) {
	      return String.format(EnumChatFormatting.YELLOW + "%,d / %,d", new Object[] { Integer.valueOf(energy), Integer.valueOf(maxEnergy) });
	    }
	    if ((energy <= maxEnergy / 4) && (energy > 0)) {
	      return String.format(EnumChatFormatting.RED + "%,d / %,d", new Object[] { Integer.valueOf(energy), Integer.valueOf(maxEnergy) });
	    }
	    if (energy == 0) {
	      return String.format("%,d / %,d", new Object[] { Integer.valueOf(energy), Integer.valueOf(maxEnergy) });
	    }
	    return "This is a bug, please report to the mod author.";
	  }*/

	  public static String addEnergyGenerationTooltip()
	  {
	    return EnumChatFormatting.WHITE + localize("info.fmr.solarpanel.generation") + EnumChatFormatting.WHITE + ":";
	  }

	  public static String addEnergyTransferTooltip()
	  {
	    return EnumChatFormatting.WHITE + localize("info.fmr.solarpanel.transfer") + EnumChatFormatting.WHITE + ":";
	  }

	  public static String addEnergyCapacityTooltip()
	  {
	    return EnumChatFormatting.WHITE + localize("info.fmr.solarpanel.capacity") + EnumChatFormatting.WHITE + ":";
	  }

	  public static String addRFtTooltip()
	  {
	    return EnumChatFormatting.DARK_RED + " RF/t";
	  }

	  public static String addRFTooltip()
	  {
	    return EnumChatFormatting.DARK_RED + " RF";
	  }

	  public static NBTTagCompound getItemTag(ItemStack stack) {
			if (stack.stackTagCompound == null) stack.stackTagCompound = new NBTTagCompound();
			return stack.stackTagCompound;
	  }
}
