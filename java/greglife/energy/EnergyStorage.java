package greglife.energy;

import cofh.api.energy.IEnergyReceiver;
import cofh.api.energy.IEnergyStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class EnergyStorage implements IEnergyStorage{

	protected int energy;
	protected int energyCapacity;
	protected int transferReceive;
	protected int transferExtract;

	public EnergyStorage(int energyCapacity)
	  {
	    this(energyCapacity, energyCapacity, energyCapacity);
	  }

	  public EnergyStorage(int energyCapacity, int transfer)
	  {
	    this(energyCapacity, transfer, transfer);
	  }

	  public EnergyStorage(int energyCapacity, int transferReceive, int transferExtract)
	  {
	    this.energyCapacity = energyCapacity;
	    this.transferReceive = transferReceive;
	    this.transferExtract = transferExtract;
	  }

	  public int getMaxReceive()
	  {
	    return Math.min(this.energyCapacity - this.energy, this.transferReceive);
	  }

	  public int receiveEnergy(int transferReceive, boolean simulate)
	  {
	    int energyReceived = Math.min(getMaxReceive(), Math.max(transferReceive, 0));
	    if (!simulate) {
	      this.energy += energyReceived;
	    }
	    return energyReceived;
	  }

	  public int getMaxExtract()
	  {
	    return Math.min(this.energy, this.transferExtract);
	  }

	  public int extractEnergy(int transferExtract, boolean simulate)
	  {
	    int energyExtracted = Math.min(getMaxExtract(), Math.max(transferExtract, 0));
	    if (!simulate) {
	      this.energy -= energyExtracted;
	    }
	    return energyExtracted;
	  }

	  public int sendEnergy(IEnergyReceiver energyReceiver, ForgeDirection from)
	  {
	    return extractEnergy(energyReceiver.receiveEnergy(from, getMaxExtract(), false), false);
	  }

	  public void readFromNBT(NBTTagCompound nbtTagCompound)
	  {
	    setMaxEnergyStored(nbtTagCompound.getInteger("Capacity"));
	    setEnergyStored(nbtTagCompound.getInteger("Energy"));
	    setEnergyTransferReceive(nbtTagCompound.getInteger("EnergyTransferReceive"));
	    setEnergyTransferExtract(nbtTagCompound.getInteger("EnergyTransferExtract"));
	  }

	  public void writeToNBT(NBTTagCompound nbtTagCompound)
	  {
	    nbtTagCompound.setInteger("Capacity", getMaxEnergyStored());
	    nbtTagCompound.setInteger("Energy", getEnergyStored());
	    nbtTagCompound.setInteger("EnergyTransferReceive", getEnergyTransferReceive());
	    nbtTagCompound.setInteger("EnergyTransferExtract", getEnergyTransferExtract());
	  }

	  public int getEnergyTransferReceive()
	  {
	    return this.transferReceive;
	  }

	  public void setEnergyTransferReceive(int transferReceive)
	  {
	    this.transferReceive = transferReceive;
	  }

	  public int getEnergyTransferExtract()
	  {
	    return this.transferExtract;
	  }

	  public void setEnergyTransferExtract(int transferExtract)
	  {
	    this.transferExtract = transferExtract;
	  }

	  public int getEnergyStored()
	  {
	    return this.energy;
	  }

	  public void setEnergyStored(int energyStored)
	  {
	    this.energy = energyStored;
	    if (this.energy > this.energyCapacity) {
	      this.energy = this.energyCapacity;
	    } else if (this.energy < 0) {
	      this.energy = 0;
	    }
	  }

	  public int getMaxEnergyStored()
	  {
	    return this.energyCapacity;
	  }

	  public void setMaxEnergyStored(int maxEnergyStored)
	  {
	    this.energyCapacity = maxEnergyStored;
	    if (this.energy > this.energyCapacity) {
	      this.energy = this.energyCapacity;
	    }
	  }
}
