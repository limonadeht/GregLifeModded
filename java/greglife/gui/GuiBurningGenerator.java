package greglife.gui;

import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import greglife.block.BlockBurningGenerator;
import greglife.tileentity.TileBurningGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiBurningGenerator extends GuiContainer{

	public static final ResourceLocation TEXTURE = new ResourceLocation("greglife", "textures/gui/guiBurn2.png");

	public TileBurningGenerator generator;
	public BlockBurningGenerator generator1;

	public EntityPlayer player;

	private int guiUpdateTick;

	public GuiBurningGenerator(InventoryPlayer invPlayer, TileBurningGenerator tile){
		super(new ContainerBurningGenerator(invPlayer, tile));
		this.generator = tile;

		this.xSize = 176;
		this.ySize = 176;
	}

	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		this.drawInfomation(mouseX, mouseY);

		int nmPos = (this.xSize + 14 - this.ySize) / 2;
	    this.fontRendererObj.drawString("Burning Generator", nmPos, 6, 7718655);

	    this.fontRendererObj.drawString("RF: " + this.generator.getEnergyStored() + "/" + this.generator.getMaxEnergyStored(), 9, 20, 13487565);
	    this.fontRendererObj.drawString("Max Input: " + this.generator.getMaxGenerateRf() * 2, 9, 30, 13487565);
	    this.fontRendererObj.drawString("Max Output: " + this.generator.getMaxGenerateRf() * 2, 9, 40, 13487565);
	    this.fontRendererObj.drawString("Burning Speed: " + this.generator.getBurnSpeed(), 9, 50, 13487565);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3){
		GL11.glColor4f(1F, 1F, 1F, 1F);

		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

		int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);

        float power = (float) generator.getEnergyStored() / (float) generator.getMaxEnergyStored() * -1F + 1F;
        float fuel = generator.burnTimeRemaining / ((float)generator.burnTime) * -1 + 1;

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

		if(generator.getEnergyStored() > 0){
			drawTexturedModalRect(
					guiLeft + 131,
					guiTop + 70,
					194,
					41,
					16,
					17);
		}

		if(generator.burnTimeRemaining > 0){
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
			this.drawHoveringText(Arrays.asList(generator.getMaxEnergyStored() + " / " + generator.getEnergyStored() + " Rf"), x -guiLeft - 6, y - guiTop, fontRendererObj);
		}

		int minX2 = guiLeft + 151;
		int maxX2 = guiLeft + 168;
		int minY2 = guiTop + 34;
		int maxY2 = guiTop + 51;
		if(x >= minX2 && x <= maxX2 && y >= minY2 && y <= maxY2){
			if(!this.generator.isUpgrade()){
				this.drawHoveringText(Arrays.asList("Upgrades"), x -guiLeft - 6, y - guiTop, fontRendererObj);
			}
		}

		int minX3 = guiLeft + 151;
		int maxX3 = guiLeft + 168;
		int minY3 = guiTop + 16;
		int maxY3 = guiTop + 33;
		if(x >= minX3 && x <= maxX3 && y >= minY3 && y <= maxY3){
			if(this.generator.getStackInSlot(0) == null){
				this.drawHoveringText(Arrays.asList("Fuels"), x -guiLeft - 6, y - guiTop, fontRendererObj);
			}
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

	/*public static final ResourceLocation TEXTURE = new ResourceLocation("greglife", "textures/gui/guiBurn.png");

	public TileBurningGenerator generator;
	public BlockBurningGenerator generator1;

	public EntityPlayer player;

	private int guiUpdateTick;

	public GuiBurningGenerator(InventoryPlayer invPlayer, TileBurningGenerator tile){
		super(new ContainerBurningGenerator(invPlayer, tile));
		this.generator = tile;

		this.xSize = 177;
		this.ySize = 188;
	}

	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		this.drawInfomation(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3){
		GL11.glColor4f(1F, 1F, 1F, 1F);

		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

		int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);

        float power = (float) generator.getEnergyStored() / (float) generator.getMaxEnergyStored() * -1F + 1F;
        float fuel = generator.burnTimeRemaining / ((float)generator.burnTime) * -1 + 1;

        drawTexturedModalRect(
				guiLeft + 153,
				guiTop + 32 + (int) (power * 50),
				xSize + 1,
				0 + (int) (power * 50),
				16,
				50 - (int) (power * 50));

		drawTexturedModalRect(
				guiLeft + 81,
				guiTop + 68 + (int)(fuel*13),
				xSize + 17,
				0 + (int)(fuel*13),
				14,
				14 - (int)(fuel*13));
	}

	private void drawInfomation(int x, int y){
		int minX = guiLeft + 152;
		int maxX = guiLeft + 169;
		int minY = guiTop + 31;
		int maxY = guiTop + 82;
		if(x >= minX && x <= maxX && y >= minY && y <= maxY)
		{
			this.drawHoveringText(Arrays.asList(EnumChatFormatting.LIGHT_PURPLE + "" + generator.getMaxEnergyStored() + " / " + generator.getEnergyStored() + " Rf"), x -guiLeft - 6, y - guiTop, fontRendererObj);
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
	}*/
}
