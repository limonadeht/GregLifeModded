package greglife.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface ICustomItemData {

	public static final String tagName = "TileCompound";

	void writeDataToItem(NBTTagCompound compound, ItemStack stack);

	void readDataFromItem(NBTTagCompound compound, ItemStack stack);
}
