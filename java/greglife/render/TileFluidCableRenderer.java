package greglife.render;

import org.lwjgl.opengl.GL11;

import greglife.model.ModelFluidCable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

public class TileFluidCableRenderer extends TileEntitySpecialRenderer{

	public static final TileFluidCableRenderer instance = new TileFluidCableRenderer();
	ResourceLocation TEXTURE = new ResourceLocation("greglife", "textures/models/FluidCable.png");

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double xOffset, double yOffset, double zOffset,float partialTickTime)
	{
		int meta = tileentity.getWorldObj().getBlockMetadata(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord);
		boolean[][] sides = new boolean[2][6];
		boolean[] cables = new boolean[6];
		for(ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
		{
			sides[meta][direction.ordinal()] = tileentity.getWorldObj().getTileEntity(tileentity.xCoord + direction.offsetX, tileentity.yCoord + direction.offsetY, tileentity.zCoord + direction.offsetZ) instanceof IFluidHandler /*|| !((IFluidHandler) tileentity.getWorldObj().getTileEntity(tileentity.xCoord + direction.offsetX, tileentity.yCoord + direction.offsetY, tileentity.zCoord + direction.offsetZ)).canDrain(direction, null) || tileentity.getWorldObj().getTileEntity(tileentity.xCoord + direction.offsetX, tileentity.yCoord + direction.offsetY, tileentity.zCoord + direction.offsetZ) instanceof IFluidTank*/;
			cables [direction.ordinal()] = tileentity.getWorldObj().getTileEntity(tileentity.xCoord + direction.offsetX, tileentity.yCoord + direction.offsetY, tileentity.zCoord + direction.offsetZ) instanceof IFluidHandler /*|| ((IFluidHandler) tileentity.getWorldObj().getTileEntity(tileentity.xCoord + direction.offsetX, tileentity.yCoord + direction.offsetY, tileentity.zCoord + direction.offsetZ)) instanceof IFluidHandler*/;
		}
		GL11.glPushMatrix();
		GL11.glTranslatef((float) (xOffset + 0.5F), (float) (yOffset + 1.5F), (float) (zOffset + 0.5F));
		GL11.glRotatef(180, 0, 0, 1);
		if(meta == 0)
		{
			Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
			ModelFluidCable.instance.renderMiddle();
			if(sides[0][0]) ModelFluidCable.instance.renderDown();
				if(sides[0][1]) ModelFluidCable.instance.renderUp();
					if(sides[0][2]) ModelFluidCable.instance.renderFront();
						if(sides[0][3]) ModelFluidCable.instance.renderBack();
							if(sides[0][4]) ModelFluidCable.instance.renderLeft();
								if(sides[0][5]) ModelFluidCable.instance.renderRight();
		}
		GL11.glPopMatrix();
	}
}
