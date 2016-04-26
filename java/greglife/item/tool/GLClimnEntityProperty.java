package greglife.item.tool;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.GLContent;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class GLClimnEntityProperty
	implements IExtendedEntityProperties
	{
	  public EntityPlayer player;
	  public int climbdirection;
	  public boolean climbing;
	  public boolean climb_side;
	  public int climb_count = 0;
	  public boolean climb_through = false;
	  public float climb_move;
	  public boolean moving = false;
	  static String propertyName = "SeitiModClimbProperty";

	  public GLClimnEntityProperty(EntityPlayer entity)
	  {
	    this.player = entity;
	    this.climbdirection = 0;
	    this.climbing = false;
	    this.climb_side = false;
	    this.climb_move = 0.0F;
	  }

	  public static GLClimnEntityProperty get(Entity entity)
	  {
	    return (GLClimnEntityProperty)entity.getExtendedProperties(propertyName);
	  }

	  public static void register(EntityPlayer entity)
	  {
	    if (get(entity) == null) {
	      entity.registerExtendedProperties(propertyName, new GLClimnEntityProperty(entity));
	    }
	  }

	  public void saveNBTData(NBTTagCompound compound) {}

	  public void loadNBTData(NBTTagCompound compound) {}

	  public void init(Entity entity, World world) {}

	  @SideOnly(Side.CLIENT)
	  public void startClimb()
	  {
	    this.climbing = true;
	    this.climbdirection = (MathHelper.floor_double(this.player.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3);
	    this.climb_side = true;
	    this.moving = false;
	    if ((this.player instanceof EntityPlayerSP))
	    {
	      EntityPlayerSP playersp = (EntityPlayerSP)this.player;
	      playersp.movementInput = new GLClimbMovementInput(playersp.movementInput);
	    }
	  }

	  public void startClimbServer()
	  {
	    this.climbing = true;
	    this.climb_side = true;
	    this.moving = false;
	    this.climbdirection = (MathHelper.floor_double(this.player.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3);
	  }

	  @SideOnly(Side.CLIENT)
	  public void startClimbTop()
	  {
	    this.climbing = true;
	    this.moving = false;
	    this.climbdirection = (MathHelper.floor_double(this.player.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3);
	    FMLLog.info("" + MathHelper.floor_double(this.player.rotationYaw * 4.0F / 360.0F + 0.5D), new Object[0]);
	    this.climb_side = false;
	    if ((this.player instanceof EntityPlayerSP))
	    {
	      EntityPlayerSP playersp = (EntityPlayerSP)this.player;
	      playersp.movementInput = new GLClimbMovementInput(playersp.movementInput);
	    }
	  }

	  public void startClimbTopServer()
	  {
	    this.climbing = true;
	    this.climbdirection = (MathHelper.floor_double(this.player.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3);
	    this.climb_side = false;
	    this.moving = false;
	  }

	  @SideOnly(Side.CLIENT)
	  public void endClimb()
	  {
	    this.climbing = false;
	    this.climbdirection = -1;
	    this.climb_through = false;
	    this.climb_move = 0.0F;
	    this.climb_count = 0;
	    this.moving = false;
	    if ((this.player instanceof EntityPlayerSP)) {
	      ((EntityPlayerSP)this.player).movementInput = new MovementInputFromOptions(Minecraft.getMinecraft().gameSettings);
	    }
	  }

	  public void endClimbServer()
	  {
	    this.climbing = false;
	    this.climbdirection = -1;
	    this.climb_through = false;
	    this.climb_move = 0.0F;
	    this.climb_count = 0;
	    this.moving = false;
	  }

	  public boolean updateClimb()
	  {
	    if (this.climb_side)
	    {
	      if ((this.climbdirection >= 0) && (this.climbdirection <= 3))
	      {
	        int offx = net.minecraft.util.Direction.offsetX[this.climbdirection];
	        int offz = net.minecraft.util.Direction.offsetZ[this.climbdirection];
	        float move = 0.0F;
	        float movey = 0.0F;
	        this.player.motionX = 0.0D;
	        this.player.motionY = 0.0D;
	        this.player.motionZ = 0.0D;
	        if (Minecraft.getMinecraft().gameSettings.keyBindForward.isPressed()) {
	          movey = (float)(movey + 0.1D);
	        }
	        if (Minecraft.getMinecraft().gameSettings.keyBindBack.isPressed()) {
	          movey = (float)(movey - 0.1D);
	        }
	        if (Minecraft.getMinecraft().gameSettings.keyBindRight.isPressed()) {
	          move = (float)(move - 0.1D);
	        }
	        if (Minecraft.getMinecraft().gameSettings.keyBindLeft.isPressed()) {
	          move = (float)(move + 0.1D);
	        }
	        if ((this.climbdirection == 1) || (this.climbdirection == 3)) {
	          move *= -1.0F;
	        }
	        if ((Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed()) && (!this.player.isSwingInProgress) && (!this.climb_through))
	        {
	          if (move != 0.0F)
	          {
	            move *= 10.0F;
	            this.player.motionY = 0.4D;
	            this.player.motionX = (offz * move * 0.2D);
	            this.player.motionZ = (offx * move * 0.2D);
	            this.player.rotationYaw = (this.climbdirection * 90);
	          }
	          else if (this.player.isSneaking())
	          {
	            this.player.motionY = 0.4D;
	            offx = net.minecraft.util.Direction.offsetX[net.minecraft.util.Direction.rotateOpposite[this.climbdirection]];
	            offz = net.minecraft.util.Direction.offsetZ[net.minecraft.util.Direction.rotateOpposite[this.climbdirection]];
	            this.player.motionX = (offx * 0.2D);
	            this.player.motionZ = (offz * 0.2D);
	            this.player.rotationYaw = (net.minecraft.util.Direction.rotateOpposite[this.climbdirection] * 90);
	          }
	          else
	          {
	            this.player.motionY = 0.6D;
	          }
	          return true;
	        }
	        if ((this.player.getHeldItem() == null) || (this.player.getHeldItem().getItem() != GLContent.itemCosmosPick) || (this.player.isSwingInProgress))
	        {
	          move = 0.0F;
	          movey = 0.0F;
	        }
	        if (checkConditionOK(offz * move, movey, offx * move))
	        {
	          this.player.motionX = (offz * move);
	          this.player.motionY = movey;
	          this.player.motionZ = (offx * move);
	        }
	        else if (checkConditionOK(offz * move, 0.0F, offx * move))
	        {
	          this.player.motionX = (offz * move);
	          this.player.motionZ = (offx * move);
	        }
	        else if ((movey != 0.0F) && (checkConditionOK(0.0F, movey, 0.0F)))
	        {
	          this.player.motionY = movey;
	        }
	        if (move != 0.0F) {
	          FMLLog.info("offx: " + offx + "offz: " + offz + "move: " + move + "climbdirection: " + this.climbdirection, new Object[0]);
	        }
	        return false;
	      }
	    }
	    else
	    {
	      float move = 0.0F;
	      float movey = 0.0F;
	      if (Minecraft.getMinecraft().gameSettings.keyBindForward.isPressed()) {
	        movey = (float)(movey + 0.3D);
	      }
	      if (Minecraft.getMinecraft().gameSettings.keyBindBack.isPressed()) {
	        movey = (float)(movey - 0.3D);
	      }
	      if (Minecraft.getMinecraft().gameSettings.keyBindRight.isPressed()) {
	        move = (float)(move - 0.3D);
	      }
	      if (Minecraft.getMinecraft().gameSettings.keyBindLeft.isPressed()) {
	        move = (float)(move + 0.3D);
	      }
	      if ((this.player.getHeldItem() == null) || (this.player.getHeldItem().getItem() != GLContent.itemCosmosPick) || (this.player.isSwingInProgress))
	      {
	        move = 0.0F;
	        movey = 0.0F;
	      }
	      this.player.moveFlying(move, movey, 0.2F);
	      this.player.motionY = 0.0D;
	      if (!checkConditionOK((float)this.player.motionX, (float)this.player.motionY, (float)this.player.motionZ))
	      {
	        this.player.motionX = 0.0D;
	        this.player.motionZ = 0.0D;
	      }
	      else
	      {
	        this.player.motionX *= 0.5D;
	        this.player.motionZ *= 0.5D;
	      }
	    }
	    return false;
	  }

	  public boolean checkConditionOK(float f, float movey, float g)
	  {
		  if (this.climb_side)
		  {
			  int offx2 = net.minecraft.util.Direction.offsetX[this.climbdirection];
			  int offz2 = net.minecraft.util.Direction.offsetZ[this.climbdirection];
			  Material mat = this.player.worldObj.getBlock(MathHelper.floor_double(this.player.posX + f) + offx2, MathHelper.floor_double(this.player.posY + this.player.eyeHeight / 2.0F + movey), MathHelper.floor_double(this.player.posZ + g) + offz2).getMaterial();
			  return mat.isSolid();
		  }
		  Material mat = this.player.worldObj.getBlock(MathHelper.floor_double(this.player.posX + f), MathHelper.floor_double(this.player.posY + movey) + 1, MathHelper.floor_double(this.player.posZ + g)).getMaterial();
		  return mat.isSolid();
	  }
}