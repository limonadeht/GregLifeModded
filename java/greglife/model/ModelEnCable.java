package greglife.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class ModelEnCable extends ModelBase
{

	public static ModelEnCable instance = new ModelEnCable();

	//fields
    ModelRenderer Middle;
    ModelRenderer Left;
    ModelRenderer Right;
    ModelRenderer Down;
    ModelRenderer Up;
    ModelRenderer Front;
    ModelRenderer Back;
    ModelRenderer MiddleBig;
    ModelRenderer LeftCone;
    ModelRenderer RightCone;
    ModelRenderer DownCone;
    ModelRenderer UpCone;
    ModelRenderer FrontCone;
    ModelRenderer BackCone;

  public ModelEnCable()
  {
    textureWidth = 128;
    textureHeight = 128;

      Middle = new ModelRenderer(this, 0, 0);
      Middle.addBox(0F, 0F, 0F, 2, 2, 2);
      Middle.setRotationPoint(-1F, 15F, -1F);
      Middle.setTextureSize(128, 128);
      Middle.mirror = true;
      setRotation(Middle, 0F, 0F, 0F);
      Left = new ModelRenderer(this, 0, 7);
      Left.addBox(0F, 0F, 0F, 7, 2, 2);
      Left.setRotationPoint(1F, 15F, -1F);
      Left.setTextureSize(128, 128);
      Left.mirror = true;
      setRotation(Left, 0F, 0F, 0F);
      Right = new ModelRenderer(this, 0, 7);
      Right.addBox(0F, 0F, 0F, 7, 2, 2);
      Right.setRotationPoint(-8F, 15F, -1F);
      Right.setTextureSize(128, 128);
      Right.mirror = true;
      setRotation(Right, 0F, 0F, 0F);
      Down = new ModelRenderer(this, 0, 13);
      Down.addBox(0F, 0F, 0F, 2, 7, 2);
      Down.setRotationPoint(-1F, 17F, -1F);
      Down.setTextureSize(128, 128);
      Down.mirror = true;
      setRotation(Down, 0F, 0F, 0F);
      Up = new ModelRenderer(this, 9, 13);
      Up.addBox(0F, 0F, 0F, 2, 7, 2);
      Up.setRotationPoint(-1F, 8F, -1F);
      Up.setTextureSize(128, 128);
      Up.mirror = true;
      setRotation(Up, 0F, 0F, 0F);
      Front = new ModelRenderer(this, 0, 25);
      Front.addBox(0F, 0F, 0F, 2, 2, 7);
      Front.setRotationPoint(-1F, 15F, -8F);
      Front.setTextureSize(128, 128);
      Front.mirror = true;
      setRotation(Front, 0F, 0F, 0F);
      Back = new ModelRenderer(this, 0, 25);
      Back.addBox(0F, 0F, 0F, 2, 2, 7);
      Back.setRotationPoint(-1F, 15F, 1F);
      Back.setTextureSize(128, 128);
      Back.mirror = true;
      setRotation(Back, 0F, 0F, 0F);
      MiddleBig = new ModelRenderer(this, 20, 19);
      MiddleBig.addBox(0F, 0F, 0F, 4, 4, 4);
      MiddleBig.setRotationPoint(-2F, 14F, -2F);
      MiddleBig.setTextureSize(128, 128);
      MiddleBig.mirror = true;
      setRotation(MiddleBig, 0F, 0F, 0F);
      LeftCone = new ModelRenderer(this, 38, 0);
      LeftCone.addBox(0F, 0F, 0F, 1, 4, 4);
      LeftCone.setRotationPoint(7F, 14F, -2F);
      LeftCone.setTextureSize(128, 128);
      LeftCone.mirror = true;
      setRotation(LeftCone, 0F, 0F, 0F);
      RightCone = new ModelRenderer(this, 38, 0);
      RightCone.addBox(0F, 0F, 0F, 1, 4, 4);
      RightCone.setRotationPoint(-8F, 14F, -2F);
      RightCone.setTextureSize(128, 128);
      RightCone.mirror = true;
      setRotation(RightCone, 0F, 0F, 0F);
      DownCone = new ModelRenderer(this, 37, 10);
      DownCone.addBox(0F, 0F, 0F, 4, 1, 4);
      DownCone.setRotationPoint(-2F, 23F, -2F);
      DownCone.setTextureSize(128, 128);
      DownCone.mirror = true;
      setRotation(DownCone, 0F, 0F, 0F);
      UpCone = new ModelRenderer(this, 37, 10);
      UpCone.addBox(0F, 0F, 0F, 4, 1, 4);
      UpCone.setRotationPoint(-2F, 8F, -2F);
      UpCone.setTextureSize(128, 128);
      UpCone.mirror = true;
      setRotation(UpCone, 0F, 0F, 0F);
      FrontCone = new ModelRenderer(this, 50, 0);
      FrontCone.addBox(0F, 0F, 0F, 4, 4, 1);
      FrontCone.setRotationPoint(-2F, 14F, -8F);
      FrontCone.setTextureSize(128, 128);
      FrontCone.mirror = true;
      setRotation(FrontCone, 0F, 0F, 0F);
      BackCone = new ModelRenderer(this, 50, 0);
      BackCone.addBox(0F, 0F, 0F, 4, 4, 1);
      BackCone.setRotationPoint(-2F, 14F, 7F);
      BackCone.setTextureSize(128, 128);
      BackCone.mirror = true;
      setRotation(BackCone, 0F, 0F, 0F);
  }

  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Middle.render(f5);
    Left.render(f5);
    Right.render(f5);
    Down.render(f5);
    Up.render(f5);
    Front.render(f5);
    Back.render(f5);
    MiddleBig.render(f5);
    LeftCone.render(f5);
    RightCone.render(f5);
    DownCone.render(f5);
    UpCone.render(f5);
    FrontCone.render(f5);
    BackCone.render(f5);
  }

  float a = 0.0625F;

  public void renderMiddle(){
	  Middle.render(a);
  }

  public void renderMiddleBig(){
	  MiddleBig.render(a);
  }

  public void renderLeft(){
	  Left.render(a);
  }

  public void renderLeftConnector(){
	  LeftCone.render(a);
  }

  public void renderRight(){
	  Right.render(a);
  }

  public void renderRightConnector(){
	  RightCone.render(a);
  }

  public void renderDown(){
	  Down.render(a);
  }

  public void renderDownConnector(){
	  DownCone.render(a);
  }

  public void renderUp(){
	  Up.render(a);
  }

  public void renderUpConnector(){
	  UpCone.render(a);
  }

  public void renderFront(){
	  Front.render(a);
  }

  public void renderFrontConnector(){
	  FrontCone.render(a);
  }

  public void renderBack(){
	  Back.render(a);
  }

  public void renderBackConnector(){
	  BackCone.render(a);
  }

  public void renderALL(){
	  float f5 = 0.0625F;

	  Middle.render(f5);
	  Left.render(f5);
	  Right.render(f5);
	  Down.render(f5);
	  Up.render(f5);
	  Front.render(f5);
	  Back.render(f5);
	  MiddleBig.render(f5);
	  LeftCone.render(f5);
	  RightCone.render(f5);
	  DownCone.render(f5);
	  UpCone.render(f5);
	  FrontCone.render(f5);
	  BackCone.render(f5);
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