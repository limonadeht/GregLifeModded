package greglife.entity;

import java.lang.reflect.Field;

import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;

public class SpawnerReflector
extends TileEntityMobSpawner
{
public static Field logicField;

public static void init()
{
  try
  {
    SpawnerReflector reflect = new SpawnerReflector();

    Field[] fields = reflect.getClass().getSuperclass().getDeclaredFields();

    fields[0].setAccessible(true);
    logicField = fields[0];
  }
  catch (Exception e)
  {
    //DebugUtils.printError(e);
	  System.out.println("[GregLifeMod/Err] Time Torch Error. Please Report to mod author");
  }
}

public static void setLogic(TileEntityMobSpawner tile, MobSpawnerBaseLogic logic)
{
  try
  {
    if (logicField != null) {
      logicField.set(tile, logic);
    }
  }
  catch (Exception e)
  {
    //DebugUtils.printError(e);
	  System.out.println("[GregLifeMod/Err] Time Torch Error. Please Report to mod author");
  }
}
}
