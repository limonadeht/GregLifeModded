package greglife.recipe;

import cpw.mods.fml.common.registry.GameRegistry;
import greglife.GLContent;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GLRecipe {

	public static GLRecipe instance = new GLRecipe();

	public void loadRecipe(){

		GameRegistry.addRecipe(new ItemStack(GLContent.itemWrench),
				new Object[]{
						" A ",
						" BA",
						"C  ",
						'A', new ItemStack(Items.iron_ingot),
						'B', new ItemStack(Items.stick),
						'C', new ItemStack(Items.iron_pickaxe)
				});

		GameRegistry.addRecipe(new ItemStack(GLContent.itemWrenchCharged),
				new Object[]{
						" A ",
						" BA",
						"C  ",
						'A', new ItemStack(Items.iron_ingot),
						'B', new ItemStack(GLContent.itemWrench),
						'C', new ItemStack(Items.diamond_pickaxe)
				});
	}
}
