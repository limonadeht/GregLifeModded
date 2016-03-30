package greglife.item;

import greglife.GregLife;
import net.minecraft.item.Item;

public class ItemUpgradeEnergy extends Item{

	public ItemUpgradeEnergy(String name){
		this.setUnlocalizedName(name);
		this.setCreativeTab(GregLife.GLTab);
		this.setTextureName("greglife:upgrade_energy");
		this.setMaxStackSize(1);
	}
}
