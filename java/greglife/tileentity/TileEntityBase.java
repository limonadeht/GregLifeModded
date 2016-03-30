package greglife.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityBase extends TileEntity{

	public void readFromNBT(NBTTagCompound nbtTagCompound){
		super.readFromNBT(nbtTagCompound);
		readCustomNBT(nbtTagCompound, false);
	}
	public abstract void readCustomNBT(NBTTagCompound nbt, boolean descPacket);

	public void writeToNBT(NBTTagCompound nbtTagCompound){
		super.writeToNBT(nbtTagCompound);
		writeCustomNBT(nbtTagCompound, false);
		}
	public abstract void writeCustomNBT(NBTTagCompound nbt, boolean descPacket);

	public Packet getDescriptionPacket(){
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		this.writeCustomNBT(nbttagcompound, true);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 3, nbttagcompound);
	}

	public void onDataPacket(NetworkManager networkManager, S35PacketUpdateTileEntity packet){
		this.readCustomNBT(packet.func_148857_g(), true);
	}
}
