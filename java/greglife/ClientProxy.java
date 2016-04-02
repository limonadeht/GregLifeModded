package greglife;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import greglife.render.ItemChikenRenderer;
import greglife.render.ItemEnergyCableRenderer;
import greglife.render.TileEnergyCableRenderer;
import greglife.tileentity.TileEnergyCable;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy{

	@Override
	public boolean isShiftKeyDown() {
		return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
	}

	@Override
	public boolean isSpaceKeyDown() {
		return Keyboard.isKeyDown(Keyboard.KEY_SPACE);
	}

	public void registerTileEntity(){
	}

	@Override
	public void registerRenderers(){
		ClientRegistry.bindTileEntitySpecialRenderer(TileEnergyCable.class, new TileEnergyCableRenderer());

		MinecraftForgeClient.registerItemRenderer(GLContent.itemChiken, new ItemChikenRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(GLContent.blockEnergyCable), new ItemEnergyCableRenderer());
	}
}
