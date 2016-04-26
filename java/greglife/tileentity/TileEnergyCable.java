package greglife.tileentity;

import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;
import greglife.energy.EnergyStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEnergyCable extends TileEntityBase implements IEnergyHandler{

	private ForgeDirection lastRecevedDirection = ForgeDirection.UNKNOWN;

	private EnergyStorage energyStorage;
	private int energyCapacity = Integer.MAX_VALUE;

	public TileEnergyCable(){
		this.energyStorage = new EnergyStorage(energyCapacity, Integer.MAX_VALUE);
	}

	@Override
	public void updateEntity(){
		if(!this.worldObj.isRemote){
			if(this.getEnergyStored() > 0){
				this.transferEnergy();
			}
		}

		//this.disttributeEnergyToSurrounding(worldObj, xCoord, yCoord, zCoord, lastRecevedDirection, energyStorage);
		//this.markDirty();
	}

	public void transferEnergy()
	{
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
		{
			TileEntity tileEntity = getWorldObj().getTileEntity(this.xCoord + direction.offsetX, this.yCoord + direction.offsetY, this.zCoord + direction.offsetZ);
			if (!(tileEntity instanceof TileEnergyCable)) {
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

	public void disttributeEnergyToSurrounding(World world, int x, int y, int z, ForgeDirection lastDerection, EnergyStorage energyStorage)
	{
		distributeEnergyToSurroundingWithLoss(world, x, y, z, lastDerection, energyStorage, 0);
	}

	public void disttributeEnergyToSurrounding(World world, int x, int y, int z, EnergyStorage energyStorage)
	{
		disttributeEnergyToSurrounding(world, x, y, z, ForgeDirection.UNKNOWN, energyStorage);
	}

	@SuppressWarnings("unused")
	public void distributeEnergyToSurroundingWithLoss(World world, int x, int y, int z, ForgeDirection lastDirection, EnergyStorage energyStorage, int loss)
	{
		int sides = 0;
		boolean sidesCanOutput[] = new boolean[6];
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
		{
			if (world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ) instanceof IEnergyHandler)
			{
				IEnergyHandler energyTileNextTolt = (IEnergyHandler) world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
				IEnergyHandler thisEnergyTile = (IEnergyHandler) world.getTileEntity(x, y, z);
				ForgeDirection invertedSide = ForgeDirection.VALID_DIRECTIONS[ForgeDirection.OPPOSITES[direction.ordinal()]];
				if(thisEnergyTile.canConnectEnergy(invertedSide) && canAddEnergyOnSide(invertedSide) && direction != lastDirection)
				{
					sidesCanOutput[direction.ordinal()] = true;
					sides++;
				}
			}
		}

		for(ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
		{
			if (sidesCanOutput[direction.ordinal()] && direction != lastDirection)
			{
				IEnergyHandler energytile = (IEnergyHandler) world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
				//if(energytile.getEnergyStored(lastDirection) / sides >= 0)
				//{
				if(energytile.canConnectEnergy(lastDirection))
				{
					//energytile.getEnergyBar().addEnergy(energytile.getEnergyTransferRate() / sides - loss);
					/*int energyTileLastEnergy = energytile.getEnergyStored(direction);
					energyTileLastEnergy += (sides - loss);*/
					energyStorage.setEnergyStored(this.getEnergyStored() - (sides));

					//energyBar.removeEnergy(energytile.getEnergyTransferRate() / sides);

					//energyTileLastEnergy -= sides;
					energyStorage.setEnergyStored(this.getEnergyStored() - (sides));
				}
				else
				{
					//int remaining = energytile.getEnergyBar().addEnergyWithRemaining(energytile.getEnergyTransferRate() / sides - loss);
					//energyBar.removeEnergy(energytile.getEnergyTransferRate() / sides - remaining);
					/*int lastEnergy = energytile.getEnergyStored(direction);
					lastEnergy -= (sides - loss);*/
					energyStorage.setEnergyStored(this.getEnergyStored() - (sides));
				}
				SetLeastRecivedDirection(ForgeDirection.VALID_DIRECTIONS[ForgeDirection.OPPOSITES[direction.ordinal()]]);
				//}
			}
		}
	}

	@SuppressWarnings("static-access")
	public boolean canAddEnergyOnSide(ForgeDirection direction){
		return direction == direction.UNKNOWN;
	}

	public void SetLeastRecivedDirection(ForgeDirection direction){
		lastRecevedDirection = direction;
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
		return this.energyStorage.receiveEnergy(maxReceive, simulate);
	}

	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate){
		return this.energyStorage.extractEnergy(maxExtract, simulate);
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
}
