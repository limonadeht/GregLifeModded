package greglife.util;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.item.ISortableTool.ToolType;

/**
 * This interface represents a tool which can be infused with upgrades
 * <b>This MUST be implemented on the item class itself</b>
 */
public interface IGregLifeTool {

	/**
	 * The tool types that this tool represents
	 * @return The set of tool types
	 */
	public EnumSet<ToolType> getToolTypes();

	/**
	 * Called once an infusion has been performed on this item
	 * @param stack The stack infused
	 * @param upgrades The upgrades infused (may contain duplicates!)
	 * @return Whether to continue calculations - if you wish to handle how upgrades work on a tool yourself,
	 * return false
	 */
	public boolean onInfusion(ItemStack stack, List<IGregLifeUpgrade> upgrades);
}
