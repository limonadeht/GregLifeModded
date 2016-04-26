package greglife.entity;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;

public class CustomEntityLogic
extends MobSpawnerBaseLogic
{
private static final int HYPER_TIME = 30;
public TileEntityMobSpawner mobSpawnerEntity;
private int hyperTime;

public CustomEntityLogic(TileEntityMobSpawner spawner, int type)
{
  this.mobSpawnerEntity = spawner;
  try
  {
	  setEntityName(spawner.func_145881_a().getEntityNameToSpawn());
  }
  catch (Exception e)
  {
    //DebugUtils.printError(e);
	  System.out.println("[GregLifeMod/Err] Time Torch Error. Please Report to mod author");
  }
  setHyper();
}

public void setHyper()
{
  this.hyperTime = 30;
}

public boolean canRun()
{
  return this.hyperTime > 0;
}

public void updateSpawner()
{
  super.updateSpawner();
  if (this.hyperTime > 0) {
    this.hyperTime -= 1;
  }
}

public void func_98267_a(int par1)
{
  this.mobSpawnerEntity.getWorldObj().addBlockEvent(this.mobSpawnerEntity.xCoord, this.mobSpawnerEntity.yCoord, this.mobSpawnerEntity.zCoord, Blocks.mob_spawner, par1, 0);
}

public World getSpawnerWorld()
{
  return this.mobSpawnerEntity.getWorldObj();
}

public int getSpawnerX()
{
  return this.mobSpawnerEntity.xCoord;
}

public int getSpawnerY()
{
  return this.mobSpawnerEntity.yCoord;
}

public int getSpawnerZ()
{
  return this.mobSpawnerEntity.zCoord;
}

public void setRandomEntity(WeightedRandomMinecart par1WeightedRandomMinecart)
{
  super.setRandomEntity(par1WeightedRandomMinecart);
  if (getSpawnerWorld() != null) {
    getSpawnerWorld().getBlock(this.mobSpawnerEntity.xCoord, this.mobSpawnerEntity.yCoord, this.mobSpawnerEntity.zCoord);
  }
}
}
