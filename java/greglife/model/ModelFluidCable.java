package greglife.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class ModelFluidCable extends ModelBase
{

	public static ModelFluidCable instance = new ModelFluidCable();

  //fields
    ModelRenderer Down;
    ModelRenderer Middle;
    ModelRenderer Up;
    ModelRenderer Left;
    ModelRenderer Right;
    ModelRenderer Front;
    ModelRenderer Back;

  public ModelFluidCable()
  {
    textureWidth = 128;
    textureHeight = 128;

      Down = new ModelRenderer(this, 0, 0);
      Down.addBox(0F, 0F, 0F, 2, 7, 2);
      Down.setRotationPoint(-1F, 17F, -1F);
      Down.setTextureSize(128, 128);
      Down.mirror = true;
      setRotation(Down, 0F, 0F, 0F);
      Middle = new ModelRenderer(this, 0, 11);
      Middle.addBox(0F, 0F, 0F, 2, 2, 2);
      Middle.setRotationPoint(-1F, 15F, -1F);
      Middle.setTextureSize(128, 128);
      Middle.mirror = true;
      setRotation(Middle, 0F, 0F, 0F);
      Up = new ModelRenderer(this, 0, 0);
      Up.addBox(0F, 0F, 0F, 2, 7, 2);
      Up.setRotationPoint(-1F, 8F, -1F);
      Up.setTextureSize(128, 128);
      Up.mirror = true;
      setRotation(Up, 0F, 0F, 0F);
      Left = new ModelRenderer(this, 9, 0);
      Left.addBox(0F, 0F, 0F, 7, 2, 2);
      Left.setRotationPoint(1F, 15F, -1F);
      Left.setTextureSize(128, 128);
      Left.mirror = true;
      setRotation(Left, 0F, 0F, 0F);
      Right = new ModelRenderer(this, 9, 0);
      Right.addBox(0F, 0F, 0F, 7, 2, 2);
      Right.setRotationPoint(-8F, 15F, -1F);
      Right.setTextureSize(128, 128);
      Right.mirror = true;
      setRotation(Right, 0F, 0F, 0F);
      Front = new ModelRenderer(this, 0, 20);
      Front.addBox(0F, 0F, 0F, 2, 2, 7);
      Front.setRotationPoint(-1F, 15F, -8F);
      Front.setTextureSize(128, 128);
      Front.mirror = true;
      setRotation(Front, 0F, 0F, 0F);
      Back = new ModelRenderer(this, 0, 20);
      Back.addBox(0F, 0F, 0F, 2, 2, 7);
      Back.setRotationPoint(-1F, 15F, 1F);
      Back.setTextureSize(128, 128);
      Back.mirror = true;
      setRotation(Back, 0F, 0F, 0F);
  }

  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Down.render(f5);
    Middle.render(f5);
    Up.render(f5);
    Left.render(f5);
    Right.render(f5);
    Front.render(f5);
    Back.render(f5);
  }

  float a = 0.0625F;

  public void renderAll(){
	  float f5 = a;
	  Down.render(f5);
	  Middle.render(f5);
	  Up.render(f5);
	  Left.render(f5);
	  Right.render(f5);
	  Front.render(f5);
	  Back.render(f5);
  }

  public void renderDown(){
	  Down.render(a);
  }

  public void renderUp(){
	  Up.render(a);
  }

  public void renderLeft(){
	  Left.render(a);
  }

  public void renderRight(){
	  Right.render(a);
  }

  public void renderFront(){
	  Front.render(a);
  }

  public void renderBack(){
	  Back.render(a);
  }

  public void renderMiddle(){
	  Middle.render(a);
  }

  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }

  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, EntityPlayer player)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, player);
  }

}
