package greglife.network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S3FPacketCustomPayload;

public class GLPacket {

	public static final int FX_TYPE_TIME = 8;

	protected int id;
	protected String channel = "GregLife";
	protected boolean isChunkDataPacket = false;

	public GLPacket() {}

	  public GLPacket(int id)
	  {
	    this.id = id;
	  }

	  public Packet getPacket()
	  {
	    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	    DataOutputStream data = new DataOutputStream(bytes);
	    try
	    {
	      data.writeByte(getID());
	      writeData(data);
	    }
	    catch (IOException e)
	    {
	      e.printStackTrace();
	    }
	    //Packet250CustomPayload packet = new Packet250CustomPayload();
	    S3FPacketCustomPayload packet = new S3FPacketCustomPayload();
	    //packet.generatePacket(null, FX_TYPE_TIME);
	    //packet. = this.channel;
	    //packet.func_149168_d() = bytes.toByteArray();
	    //packet.field_73628_b = packet.field_73629_c.length;
	    //packet.field_73287_r = this.isChunkDataPacket;
	    return packet;
	  }

	  public int getID()
	  {
	    return this.id;
	  }

	  /*protected ItemStack readItemStack(DataInputStream data)
	    throws IOException
	  {
	    ItemStack itemstack = null;
	    short itemID = data.readShort();
	    if (itemID >= 0)
	    {
	      byte stackSize = data.readByte();
	      short meta = data.readShort();
	      itemstack = new ItemStack(itemID, stackSize, meta);
	      if (( || (Item.field_77698_e[itemID].getShareTag())) {
	        itemstack.stackTagCompound = readNBTTagCompound(data);
	      }
	    }
	    return itemstack;
	  }*/

	  protected void writeItemStack(ItemStack itemstack, DataOutputStream data)
	    throws IOException
	  {
	    if (itemstack == null)
	    {
	      data.writeShort(-1);
	    }
	    else
	    {
	      data.writeShort(itemstack.animationsToGo);
	      data.writeByte(itemstack.stackSize);
	      data.writeShort(itemstack.getItemDamage());
	      if ((itemstack.getItem().isDamageable()) || (itemstack.getItem().getShareTag())) {
	        writeNBTTagCompound(itemstack.stackTagCompound, data);
	      }
	    }
	  }

	  protected NBTTagCompound readNBTTagCompound(DataInputStream data)
	    throws IOException
	  {
	    short length = data.readShort();
	    if (length < 0) {
	      return null;
	    }
	    byte[] compressed = new byte[length];
	    data.readFully(compressed);
	    return CompressedStreamTools.readCompressed(data);//compressed
	  }

	  protected void writeNBTTagCompound(NBTTagCompound nbttagcompound, DataOutputStream data)
	    throws IOException
	  {
	    if (nbttagcompound == null)
	    {
	      data.writeShort(-1);
	    }
	    else
	    {
	      byte[] compressed = CompressedStreamTools.compress(nbttagcompound);
	      data.writeShort((short)compressed.length);
	      data.write(compressed);
	    }
	  }

	  public void writeData(DataOutputStream data)
	    throws IOException
	  {}

	  public void readData(DataInputStream data)
	    throws IOException
	  {}
}
