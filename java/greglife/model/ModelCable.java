package greglife.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class ModelCable extends ModelBase{

	public static ModelCable instance = new ModelCable();

  //fields
    ModelRenderer Middle;
    ModelRenderer Up;
    ModelRenderer Down;
    ModelRenderer Left;
    ModelRenderer Right;
    ModelRenderer Front;
    ModelRenderer Back;

  public ModelCable()
  {
    textureWidth = 128;
    textureHeight = 128;

      Middle = new ModelRenderer(this, 0, 0);
      Middle.addBox(0F, 0F, 0F, 8, 8, 8);
      Middle.setRotationPoint(-4F, 12F, -4F);
      Middle.setTextureSize(128, 128);
      Middle.mirror = true;
      setRotation(Middle, 0F, 0F, 0F);
      Up = new ModelRenderer(this, 0, 17);
      Up.addBox(0F, 0F, 0F, 8, 4, 8);
      Up.setRotationPoint(-4F, 8F, -4F);
      Up.setTextureSize(128, 128);
      Up.mirror = true;
      setRotation(Up, 0F, 0F, 0F);
      Down = new ModelRenderer(this, 0, 17);
      Down.addBox(0F, 0F, 0F, 8, 4, 8);
      Down.setRotationPoint(-4F, 20F, -4F);
      Down.setTextureSize(128, 128);
      Down.mirror = true;
      setRotation(Down, 0F, 0F, 0F);
      Left = new ModelRenderer(this, 0, 30);
      Left.addBox(0F, 0F, 0F, 4, 8, 8);
      Left.setRotationPoint(4F, 12F, -4F);
      Left.setTextureSize(128, 128);
      Left.mirror = true;
      setRotation(Left, 0F, 0F, 0F);
      Right = new ModelRenderer(this, 0, 30);
      Right.addBox(0F, 0F, 0F, 4, 8, 8);
      Right.setRotationPoint(-8F, 12F, -4F);
      Right.setTextureSize(128, 128);
      Right.mirror = true;
      setRotation(Right, 0F, 0F, 0F);
      Front = new ModelRenderer(this, 0, 47);
      Front.addBox(0F, 0F, 0F, 8, 8, 4);
      Front.setRotationPoint(-4F, 12F, -8F);
      Front.setTextureSize(128, 128);
      Front.mirror = true;
      setRotation(Front, 0F, 0F, 0F);
      Back = new ModelRenderer(this, 0, 47);
      Back.addBox(0F, 0F, 0F, 8, 8, 4);
      Back.setRotationPoint(-4F, 12F, 4F);
      Back.setTextureSize(128, 128);
      Back.mirror = true;
      setRotation(Back, 0F, 0F, 0F);
  }

  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Middle.render(f5);
    Up.render(f5);
    Down.render(f5);
    Left.render(f5);
    Right.render(f5);
    Front.render(f5);
    Back.render(f5);
  }

  public void renderMiddle(){
	  Middle.render(0.0625F);
  }

  public void renderUp(){
	  Up.render(0.0625F);
  }

  public void renderDown(){
	  Down.render(0.0625F);
  }

  public void renderLeft(){
	  Left.render(0.0625F);
  }

  public void renderRight(){
	  Right.render(0.0625F);
  }

  public void renderFront(){
	  Front.render(0.0625F);
  }

  public void renderBack(){
	  Back.render(0.0625F);
  }

  public void renderAll(){
	  Middle.render(0.0625F);
	  Up.render(0.0625F);
	  Down.render(0.0625F);
	  Left.render(0.0625F);
	  Right.render(0.0625F);
	  Front.render(0.0625F);
	  Back.render(0.0625F);
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
