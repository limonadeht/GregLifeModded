package greglife;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import greglife.render.BlockGregLifeOreRenderer;
import greglife.render.ItemChikenRenderer;
import greglife.render.ItemFluidCableRenderer;
import greglife.render.ItemGregLifeInfuserRenderer;
import greglife.render.ItemTankRenderer;
import greglife.render.TileEnergyCableRenderer;
import greglife.render.TileFluidCableRenderer;
import greglife.render.TileGregLifeInfuserRenderer;
import greglife.render.TileSprinklerRenderer;
import greglife.render.TileTankRenderer;
import greglife.tileentity.TileEnergyCable;
import greglife.tileentity.TileFluidCable;
import greglife.tileentity.TileGregLifeInfuser;
import greglife.tileentity.TileSprinkler;
import greglife.tileentity.TileTank;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
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
		ClientRegistry.bindTileEntitySpecialRenderer(TileFluidCable.class, new TileFluidCableRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileTank.class, new TileTankRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileSprinkler.class, new TileSprinklerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileGregLifeInfuser.class, new TileGregLifeInfuserRenderer());

		//RenderingRegistry.instance().registerBlockHandler(0, new BlockGregLifeOreRenderer());
		RenderingRegistry.registerBlockHandler(new BlockGregLifeOreRenderer());

		MinecraftForgeClient.registerItemRenderer(GLContent.itemChiken, new ItemChikenRenderer());
		//MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(GLContent.blockEnergyCable), new ItemEnergyCableRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(GLContent.blockFluidCable), new ItemFluidCableRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(GLContent.blockTank), new ItemTankRenderer());
		//MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(GLContent.blockSprinkler), new ItemSprinklerRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(GLContent.blockGregLifeInfuser), new ItemGregLifeInfuserRenderer(new TileGregLifeInfuserRenderer(), new TileGregLifeInfuser()));
	}

	@Override
	public void bindGLTexture(String textureFile){
		if(textureFile != null){
			getClientInstance().getTextureManager().bindTexture(new ResourceLocation("greglife", textureFile));
		}
	}

	@Override
	public void bindTexture(String textureFile){
		if(textureFile != null){
			getClientInstance().getTextureManager().bindTexture(new ResourceLocation("greglife", textureFile));
		}
	}

}
