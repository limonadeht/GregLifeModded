package greglife.entity;

import greglife.GregLife;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityFrozenItem
extends EntityLiving
/*implements IBottleRenderable*/
{
protected ItemStack item;
public int frozenTime;
public int savedSpan;
public float storedRotation;
protected double storedMotionX;
protected double storedMotionY;
protected double storedMotionZ;
private int timeout;
public NBTTagCompound arrow;
public NBTTagCompound dartArrow;

@SuppressWarnings("static-access")
public EntityFrozenItem(World world)
{
  super(world);

  this.motionX = 0.0D;
  this.motionY = 0.0D;
  this.motionZ = 0.0D;

  this.storedRotation = (GregLife.proxy.rand.nextFloat() * 2.0F);

  float size = 0.1F;
  this.ySize = size;
  setSize(size, size);

  this.boundingBox.maxX = size;
  this.boundingBox.maxY = size;
  this.boundingBox.maxZ = size;
}

public EntityFrozenItem(World world, Entity item, int time)
{
  this(world);

  this.posX = item.posX;
  this.posY = item.posY;
  this.posZ = item.posZ;

  this.storedMotionX = item.motionX;
  this.storedMotionY = item.motionY;
  this.storedMotionZ = item.motionZ;

  this.frozenTime = time;
  if ((item instanceof EntityItem))
  {
    EntityItem entityItem = (EntityItem)item;
    this.savedSpan = entityItem.lifespan;
    setEntityItem(entityItem.getEntityItem());
  }
  /*else if ((item instanceof EntityDartArrow))
  {
    this.dartArrow = new NBTTagCompound();
    item.writeToNBT(this.dartArrow);
    setEntityItem(new ItemStack(DartItem.forceArrow));
  }*/
  else if ((item instanceof EntityArrow))
  {
    this.arrow = new NBTTagCompound();
    item.writeToNBT(this.arrow);
    setEntityItem(new ItemStack(Items.arrow));
  }
}

public void setTime(int time)
{
  this.frozenTime = time;
}

protected void entityInit()
{
  super.entityInit();
}

public void onUpdate()
{
	this.timeout -= 1;
	//if(!Proxies.common.isSimulating(this.worldObj)) {
		if (this.timeout <= 0)
		{
			this.timeout = 40;
			if (this.item == null) {
				sendDescriptionPacket();
			}
		}
	//}
  this.motionX = 0.0D;
  this.motionY = 0.0D;
  this.motionZ = 0.0D;

  this.frozenTime -= 1;
  if (this.frozenTime <= 0) {
	  if (/*(Proxies.common.isSimulating(this.worldObj)) &&*/ (this.item != null))
	  {
		  this.worldObj.removeEntity(this);
		  setDead();
		  try
		  {
			  /*if (this.dartArrow != null)
			  {
				  EntityDartArrow newArrow = new EntityDartArrow(this.worldObj);
				  newArrow.readFromNBT(this.dartArrow);
				  this.worldObj.spawnEntityInWorld(newArrow);
			  }
			  else*/ if (this.arrow != null)
			  {
				  EntityArrow newArrow = new EntityArrow(this.worldObj);
				  newArrow.readFromNBT(this.arrow);
				  this.worldObj.spawnEntityInWorld(newArrow);
			  }
			  else
			  {
				  EntityItem newItem = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, this.item.copy());

				  newItem.lifespan = this.savedSpan;
				  newItem.ticksExisted = 10;

				  newItem.prevPosX = newItem.posX;
				  newItem.prevPosY = newItem.posY;
				  newItem.prevPosZ = newItem.posZ;
				  newItem.ticksExisted = 1;

				  newItem.motionX = this.storedMotionX;
				  newItem.motionY = this.storedMotionY;
				  newItem.motionZ = this.storedMotionZ;

				  NBTTagCompound dartTag = EntityTime.getDartData(newItem);
				  dartTag.setInteger("timeImmune", 2);

				  this.worldObj.spawnEntityInWorld(newItem);
			  }
			  this.item = null;
		  }
		  catch (Exception e)
		  {
			  //DebugUtils.printError(e);
			  System.out.println("[GregLifeMod/Err] Time Torch Error. Please Report to mod author");
		  }
	  }
  }
}

public void readEntityFromNBT(NBTTagCompound comp)
{
  if (comp.hasKey("item")) {
    try
    {
      this.item = ItemStack.loadItemStackFromNBT(comp.getCompoundTag("item"));

      this.frozenTime = comp.getInteger("frozenTime");
      this.savedSpan = comp.getInteger("savedSpan");

      this.storedMotionX = comp.getDouble("storedMotionX");
      this.storedMotionY = comp.getDouble("storedMotionY");
      this.storedMotionZ = comp.getDouble("storedMotionZ");
      if (comp.hasKey("dartArrow")) {
        this.dartArrow = comp.getCompoundTag("dartArrow");
      }
      if (comp.hasKey("arrow")) {
        this.arrow = comp.getCompoundTag("arrow");
      }
    }
    catch (Exception e)
    {
      //DebugUtils.printError(e);
		  System.out.println("[GregLifeMod/Err] Time Torch Error. Please Report to mod author");
    }
  }
}

public void writeEntityToNBT(NBTTagCompound comp)
{
  if (this.item != null) {
    try
    {
      NBTTagCompound item = new NBTTagCompound();
      this.item.writeToNBT(item);

      comp.setTag("item", item);

      comp.setInteger("frozenTime", this.frozenTime);
      comp.setInteger("savedSpan", this.savedSpan);

      comp.setDouble("storedMotionX", this.storedMotionX);
      comp.setDouble("storedMotionY", this.storedMotionY);
      comp.setDouble("storedMotionZ", this.storedMotionZ);
      if (this.dartArrow != null) {
        comp.setTag("dartArrow", this.dartArrow);
      }
      if (this.arrow != null) {
        comp.setTag("arrow", this.arrow);
      }
    }
    catch (Exception e)
    {
      //DebugUtils.printError(e);
		  System.out.println("[GregLifeMod/Err] Time Torch Error. Please Report to mod author");
    }
  }
}

public ItemStack getEntityItem()
{
  return this.item;
}

public void setEntityItem(ItemStack stack)
{
	this.item = stack;
	//if (Proxies.common.isSimulating(this.worldObj)){
		if (this.item != null) {
			if ((this.arrow != null) || (this.dartArrow != null))
			{
				if (!this.item.hasTagCompound()) {
					this.item.setTagCompound(new NBTTagCompound());
				}
				if (this.dartArrow != null) {
					this.item.getTagCompound().setTag("storedDartArrow", this.dartArrow);
				} else if (this.arrow != null) {
					this.item.getTagCompound().setTag("storedArrow", this.arrow);
				}
			}
		}
		sendDescriptionPacket();
	//}
	/*else*/ if (this.item.hasTagCompound())
	{
		NBTTagCompound comp = this.item.getTagCompound();
		if (comp.hasKey("storedDartArrow")) {
			this.dartArrow = comp.getCompoundTag("storedDartArrow");
		} else if (comp.hasKey("storedArrow")) {
			this.arrow = comp.getCompoundTag("storedArrow");
		}
	}
}

public void sendDescriptionPacket()
{
	//if(){
		if (this.item != null)
		{
			NBTTagCompound comp = this.item.writeToNBT(new NBTTagCompound());
			if (comp != null)
			{
				comp.setInteger("bottleID", this.deathTime);

				//PacketDispatcher.sendPacketToAllInDimension(new PacketNBT(49, comp).getPacket(), this.dimension); //TODO Packets
			}
		}
	//}
	else
	{
		NBTTagCompound comp = new NBTTagCompound();
		comp.setInteger("dim", this.dimension);
		comp.setInteger("bottleID", this.deathTime);

		//PacketDispatcher.sendPacketToServer(new PacketNBT(50, comp).getPacket()); //TODO Packets
	}
}

protected boolean interact(EntityPlayer player)
{
  customInteract(player);

  return true;
}

private void customInteract(EntityPlayer player)
{
  try
  {
    player.swingItem();
    if (/*(Proxies.common.isSimulating(this.worldObj)) &&*/ (player != null) && (this.item != null))
    {
      EntityItem dropped = new EntityItem(this.worldObj, player.posX, player.posY, player.posZ, this.item);
      dropped.motionX = 0.0D;
      dropped.motionY = 0.0D;
      dropped.motionZ = 0.0D;
      dropped.fallDistance = 0;

      NBTTagCompound dartTag = EntityTime.getDartData(dropped);
      dartTag.setInteger("timeImmune", 2);
      if ((this.arrow != null) || (this.dartArrow != null)) {
        dropped.getEntityItem().setTagCompound(null);
      }
      this.item = null;
      setDead();

      this.worldObj.spawnEntityInWorld(dropped);
      this.worldObj.removeEntity(this);
    }
  }
  catch (Exception e)
  {
    //DebugUtils.printError(e);
	  System.out.println("[GregLifeMod/Err] Time Torch Error. Please Report to mod author");
  }
}

public boolean hitByEntity(Entity entity)
{
  if ((entity instanceof EntityPlayer)) {
    customInteract((EntityPlayer)entity);
  }
  return true;
}

public AxisAlignedBB getBoundingBox()
{
  try
  {
    if ((this.item != null) && (((this.item.getItem() instanceof ItemBlock)) || (this.dartArrow != null) || (this.arrow != null))) {
      return this.boundingBox;
    }
  }
  catch (Exception e)
  {
    //DebugUtils.printError(e);
	  System.out.println("[GregLifeMod/Err] Time Torch Error. Please Report to mod author");
  }
  return null;
}

public boolean isEntityInvulnerable()
{
  return true;
}

public boolean canBePushed()
{
  return false;
}
}
