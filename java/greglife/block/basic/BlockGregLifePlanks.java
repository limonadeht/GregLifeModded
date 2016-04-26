package greglife.block.basic;

import greglife.GregLife;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockGregLifePlanks extends Block{

	public BlockGregLifePlanks(String name){
		super(Material.wood);
		this.setBlockName(name);
		this.blockHardness = 2F;
		this.setBlockTextureName("greglife:glWood");
		this.setCreativeTab(GregLife.GLTab);
	}
}
