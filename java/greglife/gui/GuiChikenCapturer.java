package greglife.gui;

import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import greglife.tileentity.TileChikenCapturer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiChikenCapturer extends GuiContainer{

	public static final ResourceLocation TEXTURE = new ResourceLocation("greglife", "textures/gui/guiChiken.png");

	public TileChikenCapturer machine;

	private int guiUpdateTick;

	public GuiChikenCapturer(InventoryPlayer invPlayer, TileChikenCapturer tile){
		super(new ContainerChikenCapturer(invPlayer, tile));
		this.machine = tile;

		this.xSize = 176;
		this.ySize = 176;
	}

	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		this.drawInfomation(mouseX, mouseY);

		int nmPos = (this.xSize + 14 - this.ySize) / 2;
	    this.fontRendererObj.drawString("Chiken Capturer", nmPos, 6, 7718655);

	    this.fontRendererObj.drawString("RF: " + this.machine.getEnergyStored() + " / " + this.machine.getMaxEnergyStored(), 9, 20, 13487565);
	    this.fontRendererObj.drawString("Max Input: 32768 RF", 9, 30, 13487565);
	    this.fontRendererObj.drawString("Progress: " + this.machine.getProgress() + " %", 9, 40, 13487565);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3){
		GL11.glColor4f(1F, 1F, 1F, 1F);

		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

		int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);

        float progress = (float)machine.getProgress() / (float)100 * -1F + 1F;
        drawTexturedModalRect(
				guiLeft + 119,
				guiTop + 29 + (int)(progress*39),
				xSize + 1,
				0 + (int)(progress*39),
				11,
				39 - (int)(progress*39));

		if(machine.getProgress() > 0){
			drawTexturedModalRect(
					guiLeft + 116,
					guiTop + 70,
					177,
					41,
					16,
					17);
		}
	}

	private void drawInfomation(int x, int y){
		int minX = guiLeft + 133;
		int maxX = guiLeft + 143;
		int minY = guiTop + 29;
		int maxY = guiTop + 67;
		if(x >= minX && x <= maxX && y >= minY && y <= maxY){
			this.drawHoveringText(Arrays.asList(machine.getMaxEnergyStored() + " / " + machine.getEnergyStored() + " Rf"), x -guiLeft - 6, y - guiTop, fontRendererObj);
		}
	}

	@Override
	public void initGui(){
		super.initGui();
	}

	@Override
	protected void actionPerformed(GuiButton button){

	}

	@Override
	public void updateScreen(){
		guiUpdateTick++;
		if(this.guiUpdateTick >= 10){
			this.initGui();
			this.guiUpdateTick = 0;
		}
		super.updateScreen();
	}
}
