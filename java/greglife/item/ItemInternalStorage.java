package greglife.item;

import greglife.util.IInternalStorageItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public abstract class ItemInternalStorage extends Item implements IInternalStorageItem {

	public ItemInternalStorage()
	{
		super();
	}

	@Override
	public ItemStack[] getContainedItems(ItemStack stack)
	{
		ItemStack[] stackList = new ItemStack[getInternalSlots(stack)];
		if(stack.hasTagCompound())
		{
			NBTTagList inv = stack.getTagCompound().getTagList("Inv",10);
			for (int i=0; i<inv.tagCount(); i++)
			{
				NBTTagCompound tag = inv.getCompoundTagAt(i);
				int slot = tag.getByte("Slot") & 0xFF;
				if ((slot >= 0) && (slot < stackList.length))
					stackList[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
		return stackList;
	}

	@Override
	public void setContainedItems(ItemStack stack, ItemStack[] stackList)
	{
		NBTTagList inv = new NBTTagList();
		for (int i = 0; i < stackList.length; i++)
			if (stackList[i] != null)
			{
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte)i);
				stackList[i].writeToNBT(tag);
				inv.appendTag(tag);
			}
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setTag("Inv",inv);
	}

	@Override
	public abstract int getInternalSlots(ItemStack stack);
}
