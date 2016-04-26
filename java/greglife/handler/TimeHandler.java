package greglife.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import greglife.entity.CreeperReflector;
import greglife.entity.EntityTime;
import greglife.util.IBaneable;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

public class TimeHandler {

	@SubscribeEvent
	  public void canUpdate(EntityEvent.CanUpdate e)
	  {
	    try
	    {
	      if (e.entity == null) {
	        return;
	      }
	      NBTTagCompound dartTag = EntityTime.getDartData(e.entity);
	      if (dartTag.getInteger("timeImmune") > 0) {
	        return;
	      }
	      if (dartTag.hasKey("time"))
	      {
	        int type = dartTag.getInteger("time");
	        int time = dartTag.getInteger("timeTime");
	        switch (type)
	        {
	        case 0:
	        default:
	          break;
	        case 2:
	          if (time % 8 != 0) {
	            e.canUpdate = false;
	          }
	          break;
	        case 1:
	          e.canUpdate = false;
	          break;
	        case 3:
	          e.canUpdate = true;
	          break;
	        case 4:
	          e.canUpdate = true;
	        }
	      }
	      if (dartTag.hasKey("frozen")) {
	        e.canUpdate = false;
	      }
	    }
	    catch (Exception ex)
	    {
	      //DebugUtils.printError(ex);
			  System.out.println("[GregLifeMod/Err] Time Torch Error. Please Report to mod author");
	    }
	  }

	  @SubscribeEvent
	  public void updateEntity(LivingEvent.LivingUpdateEvent e)
	  {
	    try
	    {
	      if (e.entity == null) {
	        return;
	      }
	      NBTTagCompound dartTag = EntityTime.getDartData(e.entity);
	      if (dartTag.getInteger("timeImmune") > 0) {
	        return;
	      }
	      if ((dartTag.hasKey("time")) && (!dartTag.getBoolean("updateCalling")))
	      {
	        int type = dartTag.getInteger("time");
	        int time = dartTag.getInteger("timeTime");

	        time--;
	        switch (type)
	        {
	        case 0:
	        case 2:
	        default:
	          if (time % 8 != 0)
	          {
	            e.entity.motionX = 0.0D;
	            e.entity.motionY = 0.0D;
	            e.entity.motionZ = 0.0D;
	            e.entity.posX = e.entity.prevPosX;
	            e.entity.posY = e.entity.prevPosY;
	            e.entity.posZ = e.entity.prevPosZ;
	            e.entity.rotationPitch = e.entity.prevRotationPitch;
	            e.entity.rotationYaw = e.entity.prevRotationYaw;
	            e.setCanceled(true);

	            e.entity.hurtResistantTime -= 1;
	          }
	          break;
	        case 1:
	          e.entity.motionX = 0.0D;
	          e.entity.motionY = 0.0D;
	          e.entity.motionZ = 0.0D;
	          e.entity.posX = e.entity.prevPosX;
	          e.entity.posY = e.entity.prevPosY;
	          e.entity.posZ = e.entity.prevPosZ;
	          e.entity.rotationPitch = e.entity.prevRotationPitch;
	          e.entity.rotationYaw = e.entity.prevRotationYaw;

	          e.setCanceled(true);
	          e.entity.hurtResistantTime -= 1;
	          if ((e.entity instanceof EntityCreeper)) {
	            CreeperReflector.neutralizeCreeper((EntityCreeper)e.entity);
	          }
	          if ((e.entity instanceof IBaneable)) {
	            ((IBaneable)e.entity).setBaned();
	          }
	          break;
	        case 3:
	          dartTag.setBoolean("updateCalling", true);
	          for (int i = 0; i < 3; i++) {
	            e.entity.onUpdate();
	          }
	          dartTag.removeTag("updateCalling");
	          break;
	        case 4:
	          dartTag.setBoolean("updateCalling", true);
	          for (int i = 0; i < 11; i++) {
	            e.entity.onUpdate();
	          }
	          dartTag.removeTag("updateCalling");
	        }
	        //if (((e.entity instanceof EntityBeeSwarm)) && ((type == 3) || (type == 4))) {
	        //  ((EntityBeeSwarm)e.entity).lifeTime += (type == 3 ? 3 : 11);
	        //}
	        if (time > 0)
	        {
	          dartTag.setInteger("timeTime", time);
	        }
	        else
	        {
	          dartTag.removeTag("timeTime");
	          dartTag.removeTag("time");
	        }
	      }
	    }
	    catch (Exception ex)
	    {
	      //DebugUtils.printError(ex);
			  System.out.println("[GregLifeMod/Err] Time Torch Error. Please Report to mod author");
	    }
	  }
}
