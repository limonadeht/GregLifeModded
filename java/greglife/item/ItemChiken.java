package greglife.item;

import greglife.GregLife;
import net.minecraft.item.Item;

public class ItemChiken extends Item{

	public ItemChiken(String name){
		this.setUnlocalizedName(name);
		this.setCreativeTab(GregLife.GLTab);
	}
}
