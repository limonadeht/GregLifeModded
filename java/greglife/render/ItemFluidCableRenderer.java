package greglife.render;

import org.lwjgl.opengl.GL11;

import greglife.model.ModelFluidCable;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

public class ItemFluidCableRenderer implements IItemRenderer{

	public static final ItemEnergyCableRenderer instance = new ItemEnergyCableRenderer();
	ResourceLocation TEXTURE = new ResourceLocation("greglife", "textures/models/FluidCable.png");

	@Override
	public boolean handleRenderType(ItemStack itemstack, ItemRenderType type)
	{
		return true;
	}


	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		int meta = item.getItemDamage();
		GL11.glPushMatrix();
		GL11.glTranslated(0.5F, 1.4F, 0.5F);
		GL11.glRotatef(180, 0, 0, 1);
		if(meta == 0)
		{
			Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
			ModelFluidCable.instance.renderFront();
			ModelFluidCable.instance.renderBack();
			ModelFluidCable.instance.renderMiddle();
		}
		GL11.glPopMatrix();
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return true;
	}
}
