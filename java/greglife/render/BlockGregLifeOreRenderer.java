package greglife.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import greglife.util.IconDic;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockGregLifeOreRenderer extends BlockRenderer implements ISimpleBlockRenderingHandler{

	public void renderInventoryBlock(Block block, int meta, int modelID, RenderBlocks renderer){
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		renderer.getBlockIcon(block);

		IIcon bgIcon = null;
		if (meta == 1) {
			bgIcon = IconDic.nether;
		} else {
			bgIcon = IconDic.stone;
		}
		DrawFaces(renderer, block, bgIcon, false);

		GL11.glColor3f(1.0F, 1.0F, 0.0F);
		DrawFaces(renderer, block, IconDic.top, false);

		GL11.glColor3f(1.0F, 1.0F, 1.0F);
	}

	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer){
		int brightness = setBrightness(world, x, y, z, block);
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		renderer.getBlockIcon(block);
		renderer.renderStandardBlock(block, x, y, z);

		Tessellator tessy = Tessellator.instance;
		GL11.glEnable(3042);

		tessy.setColorOpaque(255, 255, 0);
		tessy.setBrightness(185);
		renderAllSides(world, x, y, z, block, renderer, IconDic.top, false);

		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		renderer.getBlockIcon(block);

		GL11.glDisable(3042);

		return true;
	}

	public int getRenderId(){
		return 1;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId){
		return true;
	}
}
