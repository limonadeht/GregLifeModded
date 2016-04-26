package greglife.entity;

import java.lang.reflect.Field;

import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.world.World;

public class WitchBurner
extends EntityWitch
{
public static Field field_70724_aR;

public WitchBurner(World world)
{
  super(world);
}

public static void loadAccess(World world)
{
  WitchBurner witch = new WitchBurner(world);
  try
  {
    Field[] fields = witch.getClass().getSuperclass().getDeclaredFields();

    fields[3].setAccessible(true);
    field_70724_aR = fields[3];
  }
  catch (Exception e)
  {
    e.printStackTrace();
  }
}

public static void burnWitch(EntityWitch witch)
{
  if ((field_70724_aR == null) || (witch == null)) {
    return;
  }
  try
  {
    field_70724_aR.setInt(witch, 100);
  }
  catch (Exception e)
  {
    e.printStackTrace();
  }
}
}
