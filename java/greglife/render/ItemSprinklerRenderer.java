package greglife.render;

import org.lwjgl.opengl.GL11;

import greglife.item.ItemSprinkler;
import greglife.tileentity.TileTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class ItemSprinklerRenderer implements IItemRenderer{

	public static final ItemSprinklerRenderer instance = new ItemSprinklerRenderer();

	private ItemSprinkler itemSp;

	@Override
	public boolean handleRenderType(ItemStack itemstack, ItemRenderType type){
		return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON  || type == ItemRenderType.INVENTORY  || type == ItemRenderType.ENTITY;
	}


	@SuppressWarnings("unused")
	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data){

		FluidTank fakeTank = readTank(item);
		FluidStack fluidStack = fakeTank.getFluid();

        FluidStack fluid = fakeTank.getFluid();

        if(fluid == null)
        	return;

        //Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationItemsTexture);

        //if(fluid != null){
        	int color = fluid.getFluid().getColor();

        	GL11.glPushMatrix();
        	GL11.glPushAttrib(8192);
        	GL11.glEnable(2884);
        	GL11.glDisable(2896);
        	GL11.glEnable(3042);
        	GL11.glBlendFunc(770, 771);

        	Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationItemsTexture);

        	float red = (float)(color >> 16 & 255) / 255F;
        	float green = (float)(color >> 8 & 255) / 255F;
        	float blue = (float)(color & 255) / 255F;
        	GL11.glColor4f(red, green, blue, 1.0F);

        	//GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        	GL11.glScalef(0.74F, 0.999F, 0.74F);

        	RenderBlocks renderBlocks = RenderBlocks.getInstance();
        	renderBlocks.setOverrideBlockTexture(fluid.getFluid().getStillIcon());
        	renderBlocks.renderBlockAsItem(Blocks.stone, 0, 1.0f);

        	renderBlocks.clearOverrideBlockTexture();

        	GL11.glPopAttrib();
        	GL11.glPopMatrix();
        //}else{
        //}
	}

	private static FluidTank readTank(ItemStack stack) {
		FluidTank tank = new FluidTank(TileTank.getTankCapacity());

		final NBTTagCompound itemTag = stack.getTagCompound();
		if (itemTag != null && itemTag.hasKey("tank")) {
			tank.readFromNBT(itemTag.getCompoundTag("tank"));
			return tank;
		}

		return tank;
	}


	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper){
		return false;
	}
}
