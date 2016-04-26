package greglife.tileentity;

import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;
import greglife.energy.EnergyStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileCreativeCapacitor extends TileEntityBase implements IEnergyHandler{

	private EnergyStorage energyStorage;
	private int energyCapacity;

	public TileCreativeCapacitor(int energyCapacity, int energyTransfer){
		this.energyCapacity = energyCapacity;
		this.energyStorage = new EnergyStorage(energyCapacity, energyTransfer);
	}

	@Override
	public void updateEntity(){
		if(this.getEnergyStored() < this.energyCapacity){
			this.setEnergyStored(Integer.MAX_VALUE);
		}
		if(this.getEnergyStored() > 0){
			this.transferEnergy();
		}
	}

	public void transferEnergy()
	{
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
		{
			TileEntity tileEntity = getWorldObj().getTileEntity(this.xCoord + direction.offsetX, this.yCoord + direction.offsetY, this.zCoord + direction.offsetZ);
			if (!(tileEntity instanceof TileSolarPanel)) {
				if ((tileEntity instanceof IEnergyReceiver)) {
					if (this.energyStorage.getEnergyStored() > 0)
					{
						IEnergyReceiver receiver = (IEnergyReceiver)tileEntity;
						this.energyStorage.sendEnergy(receiver, direction.getOpposite());
					}
				}
			}
		}
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket){
		this.energyStorage.readFromNBT(nbt);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket){
		this.energyStorage.writeToNBT(nbt);
	}

	public boolean canConnectEnergy(ForgeDirection from){
		return true;
	}

	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate){
		return 0;
	}

	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate){
		return this.energyStorage.extractEnergy(this.energyStorage.getMaxExtract(), simulate);
	}

	public int getEnergyStored(ForgeDirection from){
		return this.energyStorage.getEnergyStored();
	}

	public int getMaxEnergyStored(ForgeDirection from){
		return this.energyStorage.getMaxEnergyStored();
	}

	public void setEnergyStored(int energyStored){
		this.energyStorage.setEnergyStored(energyStored);
	}

	public int getEnergyStored(){
		return getEnergyStored(ForgeDirection.DOWN);
	}

	public int getMaxEnergyStored(){
		return getMaxEnergyStored(ForgeDirection.DOWN);
	}

	public NBTTagCompound getItemNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		energyStorage.writeToNBT(nbt);
		return nbt;
	}
}
