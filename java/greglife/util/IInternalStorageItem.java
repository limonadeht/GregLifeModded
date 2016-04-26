package greglife.util;

import net.minecraft.item.ItemStack;

public interface IInternalStorageItem
{
	public ItemStack[] getContainedItems(ItemStack stack);

	public void setContainedItems(ItemStack stack, ItemStack[] stackList);

	public int getInternalSlots(ItemStack stack);
}