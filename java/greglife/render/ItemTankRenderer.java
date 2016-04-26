package greglife.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

public class ItemTankRenderer implements IItemRenderer{

	public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type)
	  {
	    return true;
	  }

	  public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper)
	  {
	    if (helper == IItemRenderer.ItemRendererHelper.EQUIPPED_BLOCK) {
	      GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	    }
	    return true;
	  }

	  private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

	  public void renderItem(IItemRenderer.ItemRenderType type, ItemStack par2ItemStack, Object... data)
	  {
	    RenderBlocks renderBlocksIr = (RenderBlocks)data[0];

	    TextureManager texturemanager = RenderManager.instance.renderEngine;
	    //if (texturemanager == null) {
	      //return;
	    //}
	    GL11.glPushAttrib(1048575);

	    //texturemanager.bindTexture(texturemanager.getResourceLocation(par2ItemStack.getItemSpriteNumber()));
	    Item item = par2ItemStack.getItem();
	    Block block = ((ItemBlock)item).field_150939_a;

	    block.canRenderInPass(0);
	    GL11.glPushMatrix();
	    GL11.glDepthFunc(515);
	    GL11.glEnable(3042);

	    GL11.glEnable(3008);
	    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
	    renderBlocksIr.renderBlockAsItem(block, par2ItemStack.getItemDamage(), 1.0F);
	    GL11.glDisable(3042);
	    GL11.glDepthFunc(515);
	    GL11.glPopMatrix();

	    GL11.glPushMatrix();
	    GL11.glDepthFunc(515);
	    GL11.glEnable(3042);
	    block.canRenderInPass(1);
	    renderBlocksIr.renderBlockAsItem(block, par2ItemStack.getItemDamage(), 1.0F);
	    GL11.glDisable(3042);
	    GL11.glDepthFunc(515);
	    GL11.glPopMatrix();

	    GL11.glPopAttrib();
	    if (par2ItemStack.hasEffect(0))
	    {
	      GL11.glPushMatrix();
	      GL11.glPushAttrib(1048575);

	      GL11.glDepthFunc(514);
	      GL11.glDisable(2896);

	      texturemanager.bindTexture(RES_ITEM_GLINT);
	      GL11.glEnable(3042);
	      OpenGlHelper.glBlendFunc(769, 1, 1, 0);
	      float f7 = 0.76F;
	      GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
	      GL11.glMatrixMode(5890);
	      GL11.glPushMatrix();
	      float f8 = 0.125F;
	      GL11.glScalef(f8, f8, f8);
	      float f9 = (float)(Minecraft.getSystemTime() % 15000L) / 15000.0F * 8.0F;
	      GL11.glTranslatef(f9, 0.0F, 0.0F);
	      GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);

	      GL11.glMatrixMode(5888);
	      GL11.glPushMatrix();
	      block.canRenderInPass(1);
	      renderBlocksIr.renderBlockAsItem(block, par2ItemStack.getItemDamage(), 1.0F);
	      GL11.glPopMatrix();
	      GL11.glMatrixMode(5890);

	      GL11.glPopMatrix();
	      GL11.glPushMatrix();
	      GL11.glScalef(f8, f8, f8);
	      f9 = (float)(Minecraft.getSystemTime() % 14873L) / 14873.0F * 8.0F;
	      GL11.glTranslatef(-f9, 0.0F, 0.0F);
	      GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);

	      GL11.glMatrixMode(5888);
	      GL11.glPushMatrix();
	      block.canRenderInPass(1);
	      renderBlocksIr.renderBlockAsItem(block, par2ItemStack.getItemDamage(), 1.0F);
	      GL11.glPopMatrix();
	      GL11.glMatrixMode(5890);

	      GL11.glPopMatrix();
	      GL11.glMatrixMode(5888);
	      GL11.glDisable(3042);
	      GL11.glEnable(2896);
	      GL11.glDepthFunc(515);

	      GL11.glPopAttrib();
	      GL11.glPopMatrix();
	      texturemanager.bindTexture(texturemanager.getResourceLocation(par2ItemStack.getItemSpriteNumber()));
	    }
	  }
}
