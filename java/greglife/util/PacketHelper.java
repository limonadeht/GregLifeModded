package greglife.util;

import greglife.entity.EntityTime;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class PacketHelper
{
	public static final double PACKET_DISTANCE = 80.0D;

	public static void decreasePlayerMagic(EntityPlayer player, int amount)
	{
		try{
			//if(Proxies.common.isSimulating(player.worldObj)){
				NBTTagCompound comp = new NBTTagCompound();
				comp.setString("name", player.getDisplayName());
				comp.setInteger("amount", amount);

				//PacketDispatcher.sendPacketToPlayer(new PacketNBT(63, comp).getPacket(), (Player)player);
			//}
		}
		catch (Exception e)
		{
			//DebugUtils.printError(e);
		}
	}

	public static void restorePlayerMagic(EntityPlayer player, int tier)
	{
		restorePlayerMagic(player, tier, 0);
	}

	public static void restorePlayerMagic(EntityPlayer player, int tier, int time)
	{
		try{
			//if(Proxies.common.isSimulating(player.worldObj)){
				NBTTagCompound dartTag = EntityTime.getDartData(player);
				dartTag.removeTag("magicDepleted");
				if (tier >= 4) {
					dartTag.setInteger("freeMagic", time == 0 ? /*Config.chateauTime*/ 20 : time * 20);
				}
				NBTTagCompound comp = new NBTTagCompound();
				comp.setString("name", player.getDisplayName());
				comp.setInteger("tier", tier);
				comp.setInteger("time", time);

				//PacketDispatcher.sendPacketToPlayer(new PacketNBT(64, comp).getPacket(), (Player)player);
			//}
		}
		catch (Exception e) {}
	}

	public static void sendToAllBut(int dim, int id, Packet packet)
	{
		World world = DimensionManager.getWorld(dim);
		if ((world == null) || (packet == null)) {}
	}

	public static void sendCureFXToClients(Entity entity, int amount)
	{
		if ((entity != null) /*&& (Proxies.common.isSimulating(entity.worldObj))*/)
		{
			NBTTagCompound fxComp = new NBTTagCompound();
			fxComp.setInteger("type", 2);
			fxComp.setInteger("color", 4960792);
			fxComp.setInteger("amount", amount);
			fxComp.setDouble("x", entity.posX);
			fxComp.setDouble("y", entity.posY + 0.5D * entity.height);
			fxComp.setDouble("z", entity.posZ);
			fxComp.setInteger("subType", 2);

			//PacketDispatcher.sendPacketToAllAround(entity.posX, entity.posY, entity.posZ, 80.0D, entity.dimension, new PacketNBT(54, fxComp).getPacket());
		}
	}

	public static void sendChargeFXToClients(World world, double x, double y, double z, int amount)
	{
		//if(Proxies.common.isSimulating(world)){
			NBTTagCompound fxComp = new NBTTagCompound();
			fxComp.setInteger("type", 4);
			fxComp.setInteger("color", 16777215);
			fxComp.setInteger("amount", amount);
			fxComp.setDouble("x", x);
			fxComp.setDouble("y", y);
			fxComp.setDouble("z", z);
			fxComp.setInteger("subType", 1);

			//PacketDispatcher.sendPacketToAllAround(x, y, z, 80.0D, world.provider.dimensionId, new PacketNBT(54, fxComp).getPacket());
		//}
	}

	/*public static void sendIceFXToClients(World world, double x, double y, double z, int type, int amount, int area)
	{
		if (Proxies.common.isSimulating(world))
		{
			NBTTagCompound fxComp = new NBTTagCompound();
			fxComp.setInteger("type", 6);

			fxComp.setInteger("area", area);
			fxComp.setInteger("amount", amount);
			fxComp.setDouble("x", x);
			fxComp.setDouble("y", y);
			fxComp.setDouble("z", z);
			fxComp.setInteger("subType", type);

			PacketDispatcher.sendPacketToAllAround(x, y, z, 80.0D, world.provider.dimensionId, new PacketNBT(54, fxComp)
					.getPacket());
		}
	}

	public static void sendTimeFXToClients(World world, double x, double y, double z, int type, int amount, int area)
	{
		if (Proxies.common.isSimulating(world))
		{
			NBTTagCompound fxComp = new NBTTagCompound();
			fxComp.setInteger("type", 8);

			fxComp.setInteger("area", area);
			fxComp.setInteger("amount", amount);
			fxComp.setDouble("x", x);
			fxComp.setDouble("y", y);
			fxComp.setDouble("z", z);
			fxComp.setInteger("subType", type);

			PacketDispatcher.sendPacketToAllAround(x, y, z, 80.0D, world.provider.dimensionId, new PacketNBT(54, fxComp)
					.getPacket());
		}
	}

	public static void sendEnderFXToClients(Entity entity, int amount)
	{
		if ((entity != null) && (Proxies.common.isSimulating(entity.worldObj)))
		{
			NBTTagCompound fxComp = new NBTTagCompound();
			fxComp.setInteger("type", 3);

			fxComp.setInteger("amount", amount);
			fxComp.setDouble("x", entity.posX);
			fxComp.setDouble("y", entity.posY + 0.5D * entity.height);
			fxComp.setDouble("z", entity.posZ);

			PacketDispatcher.sendPacketToAllAround(entity.posX, entity.posY, entity.posZ, 80.0D, entity.dimension, new PacketNBT(54, fxComp)
					.getPacket());
		}
	}*/

	public static void sendHeatFXToClients(Entity entity, int amount, int area)
	{
		if (entity != null) {
			sendHeatFXToClients(entity.worldObj, entity.posX, entity.posY + 0.5D * entity.height, entity.posZ, amount, area);
		}
	}

	public static void sendHeatFXToClients(World world, double x, double y, double z, int amount, int area)
	{
		if ((world != null) /*&& (Proxies.common.isSimulating(world))*/)
		{
			NBTTagCompound fxComp = new NBTTagCompound();
			fxComp.setInteger("type", 5);

			fxComp.setInteger("amount", amount);
			fxComp.setInteger("area", area);
			fxComp.setDouble("x", x);
			fxComp.setDouble("y", y);
			fxComp.setDouble("z", z);

			//PacketDispatcher.sendPacketToAllAround(x, y, z, 80.0D, world.provider.dimensionId, new PacketNBT(54, fxComp).getPacket());
		}
	}

	public static void sendChangeFXToClients(Entity entity, int amount)
	{
		if (entity == null) {
			return;
		}
		sendChangeFXToClients(entity.worldObj, entity.posX, entity.posY + entity.height / 2.0F, entity.posZ, 1, amount, 0);
	}

	public static void sendChangeFXToClients(World world, double x, double y, double z, int subType, int amount, int area)
	{
		if ((world != null) /*&& (Proxies.common.isSimulating(world))*/)
		{
			NBTTagCompound fxComp = new NBTTagCompound();
			fxComp.setInteger("type", 1);
			fxComp.setInteger("color", 16777215);
			fxComp.setInteger("amount", amount);
			fxComp.setInteger("area", area);
			fxComp.setDouble("x", x);
			fxComp.setDouble("y", y);
			fxComp.setDouble("z", z);
			fxComp.setInteger("subType", subType);

			//PacketDispatcher.sendPacketToAllAround(x, y, z, 80.0D, world.provider.dimensionId, new PacketNBT(54, fxComp).getPacket());
		}
	}

	public static void sendRandomFireworkToClients(World world, double x, double y, double z, int amount)
	{
		if ((world != null))
		{
			NBTTagCompound fxComp = new NBTTagCompound();
			fxComp.setInteger("type", 7);
			fxComp.setInteger("color", 16777215);
			fxComp.setInteger("amount", amount);

			fxComp.setDouble("x", x);
			fxComp.setDouble("y", y);
			fxComp.setDouble("z", z);

			//FMLNetworkEvent.sendPacketToAllAround(x, y, z, 80.0D, world.provider.dimensionId, new PacketNBT(54, fxComp).getPacket());
			//PacketHandler.INSTANCE.sendToAllAround(new MessageInstruments(x, y, z, world.provider.dimensionId), new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, 64.0 ));
		}
	}
}
