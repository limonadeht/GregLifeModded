package greglife.render;

import org.lwjgl.opengl.GL11;

import greglife.model.ModelGregLifeInfuser;
import greglife.tileentity.TileGregLifeInfuser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderEnchantmentTable;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.ResourceLocation;

public class TileGregLifeInfuserRenderer extends TileEntitySpecialRenderer{

	private ModelGregLifeInfuser model = new ModelGregLifeInfuser();
	private RenderEnchantmentTable renderer = (RenderEnchantmentTable) TileEntityRendererDispatcher.instance.mapSpecialRenderers.get(TileEntityEnchantmentTable.class);

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x+0.5F, (float) y+2F, (float) z+0.5F);
		GL11.glScalef(.8F, .8F, .8F);
		GL11.glRotatef(180F, 0, 0, 1);
		//GL11.glRotatef(tileEntity.getBlockMetadata() * (-90), 0.0F, 0.0F, 1.0F);
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("greglife", "textures/models/Infuser.png"));
		model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1F);
		GL11.glPopMatrix();
		if (((TileGregLifeInfuser)tileEntity).getStackInSlot(0) != null) {
			GL11.glPushMatrix();
			GL11.glTranslatef(0, -.25F, 0);
			if (((TileGregLifeInfuser) tileEntity).dummyTable != null) {
				renderer.renderTileEntityAt(((TileGregLifeInfuser) tileEntity).dummyTable, x, y, z, f);
			}
			GL11.glPopMatrix();
		}
	}
}
