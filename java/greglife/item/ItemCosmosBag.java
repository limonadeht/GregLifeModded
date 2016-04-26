package greglife.item;

import java.util.List;

import greglife.GregLife;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class ItemCosmosBag extends ItemInternalStorage{

	public ItemCosmosBag(String name)
	{
		super();
		this.setUnlocalizedName(name);
		this.setTextureName("greglife:cosmos_bag");
		this.setCreativeTab(GregLife.GLTab);
		this.setMaxStackSize(1);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool){
		list.add("The Large Backpacks.");
		list.add("So this is item is not bugs. maybe.");
		if(GregLife.proxy.isShiftKeyDown()){
			list.add("-----------------------");
			list.add("Contained Items:");

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
					list.add(EnumChatFormatting.GREEN + stackList[slot].getItem().getItemStackDisplayName(stackList[slot]) + " : " + EnumChatFormatting.LIGHT_PURPLE + stackList[slot].stackSize);
				}
			}
		}else{
			list.add(EnumChatFormatting.DARK_PURPLE + "§o--Press Shift for Info--");
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack , World world, EntityPlayer player)
	{
		if(!world.isRemote)
			player.openGui(GregLife.Instance, 6, world, (int)player.posX,(int)player.posY,(int)player.posZ);
		return stack;
	}

	@Override
	public int getInternalSlots(ItemStack stack)
	{
		return 91;
	}
}
