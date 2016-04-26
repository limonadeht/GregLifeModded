package greglife.render;

import org.lwjgl.opengl.GL11;

import cofh.api.energy.IEnergyHandler;
import greglife.model.ModelCable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEnergyCableRenderer extends TileEntitySpecialRenderer{

	public static final TileEnergyCableRenderer instance = new TileEnergyCableRenderer();
	ResourceLocation TEXTURE = new ResourceLocation("greglife", "textures/models/cableEnergy.png");
	ResourceLocation TEXTURE2 = new ResourceLocation("greglife", "textures/models/cableEnergy2.png");

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double xOffset, double yOffset, double zOffset,float partialTickTime)
	{
		int meta = tileentity.getWorldObj().getBlockMetadata(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord);
		boolean[][] sides = new boolean[2][6];
		boolean[] cables = new boolean[6];
		for(ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
		{
			sides[meta][direction.ordinal()] = tileentity.getWorldObj().getTileEntity(tileentity.xCoord + direction.offsetX, tileentity.yCoord + direction.offsetY, tileentity.zCoord + direction.offsetZ) instanceof IEnergyHandler && ((IEnergyHandler) tileentity.getWorldObj().getTileEntity(tileentity.xCoord + direction.offsetX, tileentity.yCoord + direction.offsetY, tileentity.zCoord + direction.offsetZ)).canConnectEnergy(direction);
			cables [direction.ordinal()] = tileentity.getWorldObj().getTileEntity(tileentity.xCoord + direction.offsetX, tileentity.yCoord + direction.offsetY, tileentity.zCoord + direction.offsetZ) instanceof IEnergyHandler && ((IEnergyHandler) tileentity.getWorldObj().getTileEntity(tileentity.xCoord + direction.offsetX, tileentity.yCoord + direction.offsetY, tileentity.zCoord + direction.offsetZ)) instanceof IEnergyHandler;
		}
		GL11.glPushMatrix();
		GL11.glTranslatef((float) (xOffset + 0.5F), (float) (yOffset + 1.5F), (float) (zOffset + 0.5F));
		GL11.glRotatef(180, 0, 0, 1);
		if(meta == 0)
		{
			/*Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE2);
			ModelCable.instance.renderMiddle();
			if(sides[0][0]){
				ModelEnCable.instance.renderDown();
				ModelEnCable.instance.renderDownConnector();
			}
				if(sides[0][1]){
					ModelEnCable.instance.renderUp();
					ModelEnCable.instance.renderUpConnector();
				}
					if(sides[0][2]){
						ModelEnCable.instance.renderFront();
						ModelEnCable.instance.renderFrontConnector();
					}
						if(sides[0][3]){
							ModelEnCable.instance.renderBack();
							ModelEnCable.instance.renderBackConnector();
						}
							if(sides[0][4]){
								ModelEnCable.instance.renderLeft();
								ModelEnCable.instance.renderLeftConnector();
							}
								if(sides[0][5]){
									ModelEnCable.instance.renderRight();
									ModelEnCable.instance.renderRightConnector();
								}

			ModelEnCable.instance.renderMiddle();
			if(sides[0][0]) ModelEnCable.instance.renderDown();
				if(sides[0][1]) ModelEnCable.instance.renderUp();
					if(sides[0][2]) ModelEnCable.instance.renderFront();
						if(sides[0][3]) ModelEnCable.instance.renderBack();
							if(sides[0][4]) ModelEnCable.instance.renderLeft();
								if(sides[0][5]) ModelEnCable.instance.renderRight();*/

			Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
			ModelCable.instance.renderMiddle();
			if(sides[0][0]) ModelCable.instance.renderDown();
				if(sides[0][1]) ModelCable.instance.renderUp();
					if(sides[0][2]) ModelCable.instance.renderFront();
						if(sides[0][3]) ModelCable.instance.renderBack();
							if(sides[0][4]) ModelCable.instance.renderLeft();
								if(sides[0][5]) ModelCable.instance.renderRight();
		}
		GL11.glPopMatrix();
	}
}
