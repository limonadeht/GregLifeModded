package greglife.gui;

import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import greglife.tileentity.TileElectricFurnace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class GuiElectricFurnace extends GuiContainer{

	public static final ResourceLocation TEXTURE = new ResourceLocation("greglife", "textures/gui/guiElectric.png");

	public TileElectricFurnace furnace;

	private int guiUpdateTick;

	public GuiElectricFurnace(InventoryPlayer invPlayer, TileElectricFurnace tile){
		super(new ContainerElectricFurnace(invPlayer, tile));
		this.furnace = tile;

		this.xSize = 176;
		this.ySize = 176;
	}

	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		this.drawInfomation(mouseX, mouseY);

		int nmPos = (this.xSize + 14 - this.ySize) / 2;
	    this.fontRendererObj.drawString("Electric Furnace", nmPos, 6, 7718655);

	    this.fontRendererObj.drawString("RF: " + this.furnace.getEnergyStored() + "/" + this.furnace.getMaxEnergyStored(), 9, 20, 13487565);
	    this.fontRendererObj.drawString("Max Input: 32768 RF", 9, 40, 13487565);
	    if(furnace.isCanSmelt()){
		    this.fontRendererObj.drawString("Burning: " + EnumChatFormatting.GREEN + "Activated", 9, 50, 13487565);
	    }else{
		    this.fontRendererObj.drawString("Burning: " + EnumChatFormatting.RED + "Disabled", 9, 50, 13487565);
	    }
	    //this.fontRendererObj.drawString("Speed: " + furnace.getSpeed(), 63, 74, 13487565);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3){
		GL11.glColor4f(1F, 1F, 1F, 1F);

		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

		int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);

        float power = (float) furnace.getEnergyStored() / (float) furnace.getMaxEnergyStored() * -1F + 1F;
        float fuel = furnace.burnTimeRemaining / ((float)furnace.burnTime) * -1 + 1;

        drawTexturedModalRect(
				guiLeft + 133,
				guiTop + 29 + (int) (power * 39),
				189,
				0 + (int) (power * 39),
				11,
				39 - (int) (power * 39));

		drawTexturedModalRect(
				guiLeft + 119,
				guiTop + 29 + (int)(fuel*39),
				xSize + 1,
				0 + (int)(fuel*39),
				11,
				39 - (int)(fuel*39));

		if(furnace.getEnergyStored() > 0){
			drawTexturedModalRect(
					guiLeft + 131,
					guiTop + 70,
					194,
					41,
					16,
					17);
		}

		if(furnace.burnTimeRemaining > 0){
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
			this.drawHoveringText(Arrays.asList(furnace.getMaxEnergyStored() + " / " + furnace.getEnergyStored() + " Rf"), x -guiLeft - 6, y - guiTop, fontRendererObj);
		}

		int minX2 = guiLeft + 151;
		int maxX2 = guiLeft + 168;
		int minY2 = guiTop + 34;
		int maxY2 = guiTop + 51;
		if(x >= minX2 && x <= maxX2 && y >= minY2 && y <= maxY2){
			if(!this.furnace.isUpgrade()){
				this.drawHoveringText(Arrays.asList("Upgrades"), x -guiLeft - 6, y - guiTop, fontRendererObj);
			}
		}

		int minX3 = guiLeft + 151;
		int maxX3 = guiLeft + 168;
		int minY3 = guiTop + 16;
		int maxY3 = guiTop + 33;
		if(x >= minX3 && x <= maxX3 && y >= minY3 && y <= maxY3){
			if(this.furnace.getStackInSlot(0) == null){
				this.drawHoveringText(Arrays.asList("Resources"), x -guiLeft - 6, y - guiTop, fontRendererObj);
			}
		}
	}

	@Override
	public void initGui(){
		super.initGui();
		/*buttonList.clear();

		buttonList.add(new GuiButton(0, this.guiLeft +  79, this.guiTop + 73, 18, 18, "+"));
		buttonList.add(new GuiButton(1, this.guiLeft +  43, this.guiTop + 73, 18, 18, "-"));*/
	}

	@Override
	protected void actionPerformed(GuiButton button){
		/*if(button.id == 0){
			this.furnace.addSpeed(1);
		}
		if(button.id == 1){
			this.furnace.removeSpeed(1);
		}*/
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
