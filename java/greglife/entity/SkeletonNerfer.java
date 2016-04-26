package greglife.entity;

import java.lang.reflect.Field;

import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.world.World;

public class SkeletonNerfer
extends EntitySkeleton
{
public static Field arrowAI;

public SkeletonNerfer(World world)
{
  super(world);
}

public static void loadAccess(World world)
{
  SkeletonNerfer skelly = new SkeletonNerfer(world);
  try
  {
    Field[] fields = skelly.getClass().getSuperclass().getDeclaredFields();

    fields[0].setAccessible(true);
    arrowAI = fields[0];
  }
  catch (Exception e)
  {
    e.printStackTrace();
  }
}

public static void nerfSkelly(EntitySkeleton skelly)
{
  if ((arrowAI == null) || (skelly == null)) {
    return;
  }
  try
  {
    EntityAIArrowAttack ai = (EntityAIArrowAttack)arrowAI.get(skelly);
    ai.resetTask();
  }
  catch (Exception e)
  {
    e.printStackTrace();
  }
}
}
