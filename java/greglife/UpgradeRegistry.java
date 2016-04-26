package greglife;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import greglife.util.IGregLifeArmor;
import greglife.util.IGregLifeTool;
import greglife.util.IGregLifeUpgrade;
import greglife.util.IUpgradeRegistry;
import net.minecraft.item.Item;
import vazkii.botania.api.item.ISortableTool.ToolType;

public class UpgradeRegistry implements IUpgradeRegistry {

	private List<IGregLifeUpgrade> upgrades = new ArrayList<IGregLifeUpgrade>();
	private List<IGregLifeArmor> armor = new ArrayList<IGregLifeArmor>();
	private List<IGregLifeTool> tools = new ArrayList<IGregLifeTool>();


	@Override
	public Set<Item> getRegisteredItems() {
		ImmutableSet.Builder<Item> builder = ImmutableSet.builder();
		for (IGregLifeUpgrade upgrade : upgrades)
			builder.add(upgrade.getItem());
		return builder.build();
	}

	@Override
	public Set<IGregLifeUpgrade> getUpgrades() {
		return ImmutableSet.copyOf(upgrades);
	}

	@Override
	public Set<Item> getTools() {
		return getTools(EnumSet.allOf(ToolType.class));
	}

	@Override
	public Set<Item> getTools(EnumSet<ToolType> filter) {
		ImmutableSet.Builder<Item> builder = ImmutableSet.builder();
		for (ToolType t : filter) {
			for (IGregLifeTool tool : tools)
				if (tool.getToolTypes().contains(t))
					builder.add((Item) tool);
			for (IGregLifeArmor armor : this.armor)
				if (armor.getToolTypes().contains(t))
					builder.add((Item) armor);
		}
		return builder.build();
	}

	@Override
	public Set<IGregLifeArmor> getForceArmor() {
		return ImmutableSet.copyOf(armor);
	}

	@Override
	public Set<IGregLifeTool> getForceTools() {
		return ImmutableSet.copyOf(tools);
	}

	@Override
	public void registerUpgrade(IGregLifeUpgrade upgrade) {
		upgrades.add(upgrade);
	}

	@Override
	public void registerTool(IGregLifeTool tool) {
		tools.add(tool);
	}

	@Override
	public void registerArmor(IGregLifeArmor armor) {
		this.armor.add(armor);
	}
}
