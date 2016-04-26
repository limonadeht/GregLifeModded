package greglife.util;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import vazkii.botania.api.subtile.signature.SubTileSignature;

public class GLSignature extends SubTileSignature {

	String name;
	IIcon icon;
	public GLSignature(String nombre){
		name = nombre;
	}

	@Override
	public void registerIcons(IIconRegister reg){
		icon = reg.registerIcon("greglife:" + name);
	}

	@Override
	public IIcon getIconForStack(ItemStack item){
		return icon;
	}

	@Override
	public String getUnlocalizedNameForStack(ItemStack item){
		return "gl.blockFlower." + name;
	}

	@Override
	public String getUnlocalizedLoreTextForStack(ItemStack item){
		return "gl.tile.flower." + name + ".lore";
	}
}