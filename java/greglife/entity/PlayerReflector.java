package greglife.entity;

import java.lang.reflect.Field;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class PlayerReflector
extends EntityPlayer
{
public static Field move;

public PlayerReflector(World world)
{
  super(world /*"GregLifeDummyPlayer"*/, null);
}

public static void loadAccess(World world)
{
  PlayerReflector player = new PlayerReflector(world);
  try
  {
    Field[] fields = player.getClass().getSuperclass().getDeclaredFields();
    fields[34].setAccessible(true);
    move = fields[34];
  }
  catch (Exception e)
  {
    e.printStackTrace();
  }
}

public static void modifySpeed(EntityPlayer player, float speed)
{
  if (move == null) {
    return;
  }
  try
  {
    move.setFloat(player, speed);
  }
  catch (Exception e)
  {
    e.printStackTrace();
  }
}

public boolean canCommandSenderUseCommand(int i, String s)
{
  return false;
}

@Override
public void addChatMessage(IChatComponent p_145747_1_){}

/*public void func_70006_a(ChatComponentText chatmessagecomponent) {}*/

public ChunkCoordinates getPlayerCoordinates(){
	return null;
}

/*public ChunkCoordinates func_82114_b()
{
  return null;
}*/
}
