package greglife.entity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;

public class EntityTimeItem
extends EntityItem
{
public boolean stopped;
public boolean slowed;
private int slowTime;
private static final int DELAY = 10;

public EntityTimeItem(World world)
{
  super(world);
}

public EntityTimeItem(EntityItem item)
{
  super(item.worldObj, item.posX, item.posY, item.posZ, item.getEntityItem());

  this.motionX = item.motionX;
  this.motionY = item.motionY;
  this.motionZ = item.motionZ;

  this.lifespan = item.lifespan;
  this.dimension = item.dimension;
}

public void setSpeed(int type)
{
  switch (type)
  {
  case 2:
    this.slowed = true;
    break;
  case 1:
    this.stopped = true;
  }
}

public void onUpdate()
{
  if (this.stopped) {
    return;
  }
  if (this.slowed)
  {
    this.slowTime -= 1;
    if (this.slowTime > 0) {
      return;
    }
    this.slowTime = 10;
  }
  super.onUpdate();
}
}
