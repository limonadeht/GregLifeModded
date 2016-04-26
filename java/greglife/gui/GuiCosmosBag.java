package greglife.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiCosmosBag extends GuiContainer{

	public static final ResourceLocation TEXTURE = new ResourceLocation("greglife", "textures/gui/cosmos_bag.png");

	public GuiCosmosBag(InventoryPlayer invPlayer, World world)
	{
		super(new ContainerCosmosBag(invPlayer, world));

		this.xSize = 248;//225
		this.ySize = 225;//248
	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);
		ArrayList<String> tooltip = new ArrayList<String>();
		int slot = -1;
		for(int i=0; i<((ContainerCosmosBag)this.inventorySlots).internalSlots; i++)
		{
			Slot s = (Slot)this.inventorySlots.inventorySlots.get(i);
			if(!s.getHasStack() && mx>guiLeft+s.xDisplayPosition&&mx<guiLeft+s.xDisplayPosition+16 && my>guiTop+s.yDisplayPosition&&my<guiTop+s.yDisplayPosition+16)
				slot = i;
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

		int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}
}
