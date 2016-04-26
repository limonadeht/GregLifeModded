package greglife.render;

import org.lwjgl.opengl.GL11;

import greglife.tileentity.TileSprinkler;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public class TileSprinklerRenderer extends TileEntitySpecialRenderer{

	@Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float var8) {
        TileSprinkler tileTank = (TileSprinkler)tileentity;

        if(tileTank == null)
            return;

        IFluidTank tank = tileTank.getTank();

        FluidStack fluid = tank.getFluid();

        if(fluid == null || fluid.amount <= 0)
            return;

        int color = fluid.getFluid().getColor();

        GL11.glPushMatrix();
        GL11.glPushAttrib(8192);
        GL11.glEnable(2884);
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);

        bindTexture(TextureMap.locationBlocksTexture);

        float red = (float)(color >> 16 & 255) / 255F;
        float green = (float)(color >> 8 & 255) / 255F;
        float blue = (float)(color & 255) / 255F;
        GL11.glColor4f(red, green, blue, 1.0F);



        GL11.glTranslatef((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F);
        GL11.glScalef(0.74F, 0.999F, 0.74F);
        //GL11.glTranslatef(0.0F, -0.5F, 0.0F);


        RenderBlocks renderBlocks = RenderBlocks.getInstance();
        renderBlocks.setOverrideBlockTexture(fluid.getFluid().getStillIcon());
        renderBlocks.renderBlockAsItem(Blocks.stone, 0, 1.0f);

        renderBlocks.clearOverrideBlockTexture();

        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
}
