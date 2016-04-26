package greglife.entity;

import java.lang.reflect.Field;

import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.world.World;

public class CreeperReflector
extends EntityCreeper
{
public static Field fuse;
public static Field radius;

public CreeperReflector(World world)
{
  super(world);
}

public static void loadAccess(World world)
{
  CreeperReflector creep = new CreeperReflector(world);
  try
  {
    Field[] fields = creep.getClass().getSuperclass().getDeclaredFields();

    fields[1].setAccessible(true);
    fields[3].setAccessible(true);

    fuse = fields[1];
    radius = fields[3];
  }
  catch (Exception e)
  {
    e.printStackTrace();
  }
}

public static void neutralizeCreeper(EntityCreeper creeper)
{
  if ((fuse == null) || (creeper == null)) {
    return;
  }
  try
  {
    fuse.setInt(creeper, 10);
    radius.setInt(creeper, 0);
  }
  catch (Exception e)
  {
    e.printStackTrace();
  }
}
}