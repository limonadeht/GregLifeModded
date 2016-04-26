package greglife.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.GregLife;
import greglife.util.ItemNBTHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class ItemUpgradeBook extends Item{

	public ItemUpgradeBook(String name) {
		super();
		this.setUnlocalizedName(name);
		this.setMaxStackSize(1);
		this.setTextureName("greglife:upgradeBook");
	}

	@SuppressWarnings("unchecked")
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean isAdvanced){

		list.add("Use GregLife Infuser");
		if(GregLife.proxy.isShiftKeyDown()){
			list.add("-----------------------");
			list.add("Current Tier: " + EnumChatFormatting.YELLOW + ItemNBTHelper.getInteger(stack, "tier", 0));
			list.add("Stored Experience: " + EnumChatFormatting.YELLOW + ItemNBTHelper.getInteger(stack, "xp", 0));

			int xpNeeded = getXpNeededForNextTier(stack);
			if(xpNeeded > 0){
				list.add(EnumChatFormatting.AQUA + "Experience To Next Tier: " + EnumChatFormatting.YELLOW + xpNeeded);
			}
		}else{
			list.add(EnumChatFormatting.DARK_PURPLE + "§o--Press Shift for Info--");
		}
		/*if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			information.add(Colors.AQUA+StatCollector.translateToLocal("lore.upgradeTome.tier")+": "+Colors.YELLOW+NBTHelper.getInt(itemStack, "tier"));
			information.add(Colors.AQUA+StatCollector.translateToLocal("lore.upgradeTome.xp")+": "+Colors.YELLOW+NBTHelper.getInt(itemStack, "xp"));
			int xpNeeded = getXpNeededForNextTier(itemStack);
			if (xpNeeded >= 0)
				information.add(Colors.AQUA+StatCollector.translateToLocal("lore.upgradeTome.xpToNextTier")+": "+Colors.YELLOW+xpNeeded);
		} else
			information.add(Colors.GRAY+StatCollector.translateToLocal("lore.shiftMessage"));*/
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean isInHand){
		int xp = getXpNeededForNextTier(itemStack);
		if(xp <= 0 && xp != Integer.MIN_VALUE){
			int remaining = Math.abs(xp);
			ItemNBTHelper.setInteger(itemStack, "xp", remaining);
			ItemNBTHelper.setInteger(itemStack, "tier", ItemNBTHelper.getInteger(itemStack, "tier", 0) + 1);
		}
	}

	private int getXpNeededForNextTier(ItemStack stack) {
		try{
			if(ItemNBTHelper.getInteger(stack, "tier", 0) != 7){
				return ItemNBTHelper.getInteger(stack, "tier", 0) + 1 - ItemNBTHelper.getInteger(stack, "xp", 0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return Integer.MIN_VALUE;
	}
}
