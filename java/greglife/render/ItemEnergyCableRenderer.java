package greglife.render;

import org.lwjgl.opengl.GL11;

import greglife.model.ModelCable;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

public class ItemEnergyCableRenderer implements IItemRenderer{

	public static final ItemEnergyCableRenderer instance = new ItemEnergyCableRenderer();
	ResourceLocation TEXTURE = new ResourceLocation("greglife", "textures/models/cableEnergy.png");
	ResourceLocation TEXTURE2 = new ResourceLocation("greglife", "textures/models/cableEnergy2.png");

	@Override
	public boolean handleRenderType(ItemStack itemstack, ItemRenderType type)
	{
		return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON  || type == ItemRenderType.INVENTORY  || type == ItemRenderType.ENTITY;
	}


	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		int meta = item.getItemDamage();
		GL11.glPushMatrix();
		GL11.glTranslated(0.5F, 1.4F, 0.5F);
		GL11.glRotatef(180, 0, 0, 1);

		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

		Minecraft mc = Minecraft.getMinecraft();
		if (type == ItemRenderType.INVENTORY)
		{
			GL11.glPushMatrix();
			GL11.glScalef(13F, 13F, 13F);
			GL11.glTranslated(1.2, 2.2, 0);
			GL11.glRotatef(180F, 1F, 0F, 0F);
			GL11.glRotatef(mc.getSystemTime() / -10, 0F, 1F, 0F);
			GL11.glRotatef(-20F, 1F, 0F, 0F);
			ModelCable.instance.renderAll();
			GL11.glPopMatrix();
		}
		else if (type == ItemRenderType.EQUIPPED_FIRST_PERSON)
		{
			GL11.glPushMatrix();
			GL11.glScalef(0.8F, 0.8F, 0.8F);
			GL11.glTranslated(2, 0.5, 0);
			GL11.glRotatef(20F, 0F, 0F, 1F);
			GL11.glRotatef(mc.getSystemTime() / -10, 0F, 1F, 0F);
			GL11.glRotatef(-20F, 1F, 0F, 0F);
			ModelCable.instance.renderAll();
			GL11.glPopMatrix();
		}
		else if (type == ItemRenderType.EQUIPPED)
		{
			GL11.glPushMatrix();
			GL11.glScalef(0.8F, 0.8F, 0.8F);
			GL11.glTranslated(1, 0.5, 0);
			GL11.glRotatef(20F, 0F, 0F, 1F);
			GL11.glRotatef(mc.getSystemTime() / -10, 0F, 1F, 0F);
			GL11.glRotatef(-20F, 1F, 0F, 0F);
			ModelCable.instance.renderAll();
			GL11.glPopMatrix();
		}
		else
		{
			GL11.glPushMatrix();
			GL11.glScalef(1.5F, 1.5F, 1.5F);
			GL11.glRotatef(mc.getSystemTime() / -10, 0F, 1F, 0F);
			GL11.glRotatef(-20F, 1F, 0F, 0F);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			ModelCable.instance.renderAll();
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
		}

		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}


	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return true;
	}
}
