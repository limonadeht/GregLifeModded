package greglife.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.GLContent;
import greglife.GregLife;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class ItemLiquidGLBucket extends ItemBucket{

	public ItemLiquidGLBucket(Fluid fluid) {
		super(fluid.getBlock());
		this.setUnlocalizedName("liquidGLBucket");
		this.setContainerItem(Items.bucket);
		this.setCreativeTab(GregLife.GLTab);
		this.setTextureName("greglife:liquidGLBucket");
	}

	@Override
	public String getUnlocalizedName(){//Formats the name
		return String.format("item.%s%s", "greglife:", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}

	@Override
	public String getUnlocalizedName(ItemStack item){//Formats the name
		return String.format("item.%s%s", "greglife:", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister){//Sets the icon
		itemIcon = iconRegister.registerIcon(String.format("%s", getUnwrappedUnlocalizedName(this.getUnlocalizedName())));
	}

	protected String getUnwrappedUnlocalizedName(String unlocalizedName){//Removes the "item." from the item name
		return unlocalizedName.substring(unlocalizedName.indexOf(".")+1);
	}

	@Override
	public boolean tryPlaceContainedLiquid(World world, int x, int y, int z) {
		Material material = world.getBlock(x, y, z).getMaterial();
		boolean flag = !material.isSolid();

		if (!world.isAirBlock(x, y, z) && !flag) {
			return false;
		}

		if (!world.isRemote && flag && !material.isLiquid()) {
			world.func_147480_a(x, y, z, true);
		}

		world.setBlock(x, y, z, GLContent.blockliquidGregLife, 0, 3);
		return true;
	}
}
