package greglife.gui;

import org.lwjgl.opengl.GL11;

import greglife.tileentity.TileStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiStorage extends GuiContainer{

	public static final ResourceLocation TEXTURE = new ResourceLocation("greglife", "textures/gui/guiStorage.png");
	//public static final ResourceLocation TEXTURE = new ResourceLocation("greglife", "textures/gui/guiBurn2.png");

	public TileStorage storage;

	public GuiStorage(InventoryPlayer invPlayer, TileStorage tile){
		super(new ContainerStorage(invPlayer, tile));
		this.storage = tile;

		this.xSize = 256;
		this.ySize = 256;
	}

	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY){

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3){
		GL11.glColor4f(1F, 1F, 1F, 1F);

		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

		int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}
}
