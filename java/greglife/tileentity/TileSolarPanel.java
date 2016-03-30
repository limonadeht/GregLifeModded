package greglife.tileentity;

import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;
import greglife.energy.EnergyStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

public class TileSolarPanel extends TileEntityBase implements IEnergyHandler{

	private EnergyStorage energyStorage;
	private int energyGeneration;

	public TileSolarPanel(){
		this(0,0,0);
	}

	public TileSolarPanel(int energyGeneration, int energyTransfer, int energyCapacity){
		this.energyGeneration = energyGeneration;
		this.energyStorage = new EnergyStorage(energyCapacity, energyTransfer);
	}

	public void updateEntity(){
		if(!this.worldObj.isRemote){
			generateEnergy();
			if(this.getEnergyStored() > 0){
				transferEnergy();
			}
		}
	}

	private int getEnergyGeneration()
	  {
	    float multiplicator = 1.5F;
	    float displacement = 1.2F;
	    float celestialAngleRadians = this.worldObj.getCelestialAngleRadians(1.0F);
	    if (celestialAngleRadians > 3.141592653589793D) {
	      celestialAngleRadians = 6.283184F - celestialAngleRadians;
	    }
	    int sunGeneration = Math.round(this.energyGeneration * multiplicator * MathHelper.cos(celestialAngleRadians / displacement));
	    if (this.worldObj.canBlockSeeTheSky(this.xCoord, this.yCoord + 1, this.zCoord)) {
	      if (sunGeneration > 0)
	      {
	        if (this.worldObj.isRaining()) {
	          return 0;
	        }
	        if (this.worldObj.isThundering()) {
	          return 0;
	        }
	        return Math.min(this.energyGeneration, sunGeneration);
	      }
	    }
	    return 0;
	  }

	protected void generateEnergy()
	  {
	    int energyGeneration = getEnergyGeneration();
	    if (energyGeneration > 0) {
	      this.energyStorage.receiveEnergy(energyGeneration, false);
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
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	  {
	    this.energyGeneration = nbt.getInteger("EnergyGeneration");
	    this.energyStorage.readFromNBT(nbt);
	  }

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	  {
	    nbt.setInteger("EnergyGeneration", this.energyGeneration);
	    this.energyStorage.writeToNBT(nbt);
	  }

	  public boolean canConnectEnergy(ForgeDirection from)
	  {
	    return true;
	  }

	  public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
	  {
	    return 0;
	  }

	  public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
	  {
	    return this.energyStorage.extractEnergy(this.energyStorage.getMaxExtract(), simulate);
	  }

	  public int getEnergyStored(ForgeDirection from)
	  {
	    return this.energyStorage.getEnergyStored();
	  }

	  public int getMaxEnergyStored(ForgeDirection from)
	  {
	    return this.energyStorage.getMaxEnergyStored();
	  }

	  public void setEnergyStored(int energyStored)
	  {
	    this.energyStorage.setEnergyStored(energyStored);
	  }

	  public int getEnergyStored()
	  {
	    return getEnergyStored(ForgeDirection.DOWN);
	  }

	  public int getMaxEnergyStored()
	  {
	    return getMaxEnergyStored(ForgeDirection.DOWN);
	  }

	  public static class Advanced extends TileSolarPanel{

		  public Advanced(){
			  this(0,0,0);
		  }

		  public Advanced(int energyGeneration, int energyTransfer, int energyCapacity){
			  super(energyGeneration, energyTransfer, energyCapacity);
		  }
	  }

	  public static class GregLife extends TileSolarPanel{

		  public GregLife(){
			  this(0,0,0);
		  }

		  public GregLife(int energyGeneration, int energyTransfer, int energyCapacity){
			  super(energyGeneration, energyTransfer, energyCapacity);
		  }
	  }
}
