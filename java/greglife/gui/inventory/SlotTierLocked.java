package greglife.gui.inventory;

import greglife.GregLife;
import greglife.tileentity.TileGregLifeInfuser;
import net.minecraft.item.ItemStack;

public class SlotTierLocked extends ExclusiveSlot{

	private int tier = 0;
	private TileGregLifeInfuser tile;

	public SlotTierLocked(TileGregLifeInfuser inv, int index, int xPos, int yPos) {
		super(inv, index, xPos, yPos);
		setExclusive(GregLife.Upgrade_Registry.getRegisteredItems());
		setWhitelist(true);
		tile = inv;
	}

	public SlotTierLocked setTier(int tier) {
		this.tier = tier;
		return this;
	}

	@Override
	public boolean isItemValid(ItemStack itemStack) {
		if (tier > tile.getTier())
			return false;

		return super.isItemValid(itemStack);
	}
}
