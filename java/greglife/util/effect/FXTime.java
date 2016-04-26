package greglife.util.effect;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import greglife.GregLife;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class FXTime
extends EntityFX
{
public static final int TYPE_FALL = 0;
public static final int TYPE_CHANGE = 1;
public static final int TYPE_BREAK = 2;
public static final int TYPE_FREEZE = 3;
public static final int TYPE_FREEZE_ENTITY = 4;
private int iconIndex = 22;
private int changeTime;
private int type;
private Color color;

public FXTime(World world, double x, double y, double z, double vx, double vy, double vz)
{
  super(world, x, y, z, vx, vy, vz);
}

@SuppressWarnings("static-access")
public FXTime(World world, double x, double y, double z, int type)
{
  super(world, x, y, z);

  this.color = new Color(3574754);
  this.particleRed = this.color.getRed();
  this.particleGreen = this.color.getGreen();
  this.particleBlue = this.color.getBlue();

  setSize(0.02F, 0.02F);

  this.changeTime = 0;
  this.noClip = true;
  this.type = type;
  float velModifier;
  switch (type)
  {
  case 0:
    this.motionX = (this.motionZ = 0.0D);
    this.motionY = -0.025D;
    this.particleMaxAge = ((int)(85.0D * (world.rand.nextFloat() * 0.2D + 0.8999999761581421D)));
    break;
  case 1:
    velModifier = 0.15F;
    this.motionX = ((GregLife.proxy.rand.nextFloat() * 2.0F - 1.0F) * velModifier);
    this.motionY = ((GregLife.proxy.rand.nextFloat() * 2.0F - 1.0F) * velModifier);
    this.motionZ = ((GregLife.proxy.rand.nextFloat() * 2.0F - 1.0F) * velModifier);
    this.particleMaxAge = ((int)(15.0D * (world.rand.nextFloat() * 0.2D + 0.8999999761581421D)));
    break;
  case 2:
    velModifier = 0.1F;
    this.motionX = ((GregLife.proxy.rand.nextFloat() * 2.0F - 1.0F) * velModifier);
    this.motionY = velModifier;
    this.motionZ = ((GregLife.proxy.rand.nextFloat() * 2.0F - 1.0F) * velModifier);
    this.particleMaxAge = ((int)(15.0D * (world.rand.nextFloat() * 0.2D + 0.8999999761581421D)));
    this.particleGravity = 0.5F;
    break;
  case 4:
    this.particleAlpha = 0.5F;
  case 3:
    velModifier = 0.01F;
    this.motionX = ((GregLife.proxy.rand.nextFloat() * 2.0F - 1.0F) * velModifier);
    this.motionY = velModifier;
    this.motionZ = ((GregLife.proxy.rand.nextFloat() * 2.0F - 1.0F) * velModifier);
    this.particleMaxAge = ((int)(15.0D * (world.rand.nextFloat() * 0.2D + 0.8999999761581421D)));
    this.particleGravity = 0.0F;
  }
  this.particleMaxAge = ((int)(this.particleMaxAge * 2.0F));
  this.particleScale *= 1.25F;
  this.particleGravity *= 0.5F;
  this.particleAlpha *= 0.3F;
}

public void onUpdate()
{
  super.onUpdate();

  this.rotationPitch += 0.01F;
  switch (this.type)
  {
  case 3:
    if (this.particleAge > this.particleMaxAge / 2) {
      this.particleAlpha -= 0.075F;
    }
    break;
  }
}

public void renderParticle(Tessellator tessy, float par2, float par3, float par4, float par5, float par6, float par7)
{
  tessy.draw();
  GL11.glPushMatrix();

  GL11.glDepthMask(false);
  GL11.glEnable(3042);

  GL11.glBlendFunc(770, 1);

  GregLife.proxy.bindGLTexture("darticles.png");
  GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);

  float var8 = this.iconIndex % 8 / 8.0F;
  float var9 = var8 + 0.124875F;
  float var10 = this.iconIndex / 8 / 8.0F;
  float var11 = var10 + 0.124875F;
  float var12 = 0.1F * this.particleScale;
  float var13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * par2 - EntityFX.interpPosX);
  float var14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * par2 - EntityFX.interpPosY);
  float var15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * par2 - EntityFX.interpPosZ);

  tessy.startDrawingQuads();
  tessy.setBrightness(240);

  tessy.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
  tessy.addVertexWithUV(var13 - par3 * var12 - par6 * var12, var14 - par4 * var12, var15 - par5 * var12 - par7 * var12, var9, var11);

  tessy.addVertexWithUV(var13 - par3 * var12 + par6 * var12, var14 + par4 * var12, var15 - par5 * var12 + par7 * var12, var9, var10);

  tessy.addVertexWithUV(var13 + par3 * var12 + par6 * var12, var14 + par4 * var12, var15 + par5 * var12 + par7 * var12, var8, var10);

  tessy.addVertexWithUV(var13 + par3 * var12 - par6 * var12, var14 - par4 * var12, var15 + par5 * var12 - par7 * var12, var8, var11);

  tessy.draw();

  GL11.glDisable(3042);
  GL11.glDepthMask(true);
  GL11.glPopMatrix();

  GregLife.proxy.bindTexture("textures/particle/particles.png");
  tessy.startDrawingQuads();
}
}
