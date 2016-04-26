package greglife.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

public class PacketNBT
extends GLPacket
{
	protected NBTTagCompound comp;

	public PacketNBT() {}

	public PacketNBT(int id)
	{
		super(id);
	}

	public PacketNBT(int id, NBTTagCompound nbttagcompound)
	{
		super(id);
		this.comp = nbttagcompound;
	}

	public void writeData(DataOutputStream data)
			throws IOException
	{
		byte[] compressed = CompressedStreamTools.compress(this.comp);
		data.writeShort(compressed.length);
		data.write(compressed);
	}

	public void readData(DataInputStream data)
			throws IOException
	{
		short length = data.readShort();
		byte[] compressed = new byte[length];
		data.readFully(compressed);
		this.comp = CompressedStreamTools.read(data);//compressed
	}

	public NBTTagCompound getTagCompound()
	{
		return this.comp;
	}
}