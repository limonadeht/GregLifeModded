package greglife.tileentity;

import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import greglife.energy.EnergyStorage;
import greglife.util.TankType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;

public class TileLavaCreator extends TileEntityBase implements IEnergyHandler, IFluidHandler{

	private EnergyStorage energyStorage;
	private int energyCapacity;

	public int tankCapacity = Integer.MAX_VALUE;
	public TankType tank = new TankType(tankCapacity);

	static ConcurrentHashMap<ChunkCoordinates, Set<DirectionalFluidOutput>> indirectConnections = new ConcurrentHashMap<ChunkCoordinates, Set<DirectionalFluidOutput>>();

	public TileLavaCreator(){
		this(0,0);
	}

	public TileLavaCreator(int energyCapacity, int energyTransfer){
		this.energyCapacity = energyCapacity;
		this.energyStorage = new EnergyStorage(energyCapacity, energyTransfer);
	}

	@Override
	public void updateEntity(){
		tank.setFluid(new FluidStack(FluidRegistry.LAVA, this.tank.getFluidAmount()));

		if(this.getEnergyStored() > 10000 && this.getTank().getFluid().amount < Integer.MAX_VALUE){
			this.setEnergyStored(this.getEnergyStored() - 10000);
			this.getTank().getFluid().amount += 100;
		}
		this.markDirty();

		if(!worldObj.isRemote){
			indirectConnections.clear();
		}
	}

	public static Set<DirectionalFluidOutput> getConnectedFluidHandlers(ChunkCoordinates node, World world){
		if(indirectConnections.containsKey(node))
			return indirectConnections.get(node);

		ArrayList<ChunkCoordinates> openList = new ArrayList();
		ArrayList<ChunkCoordinates> closedList = new ArrayList();
		Set<DirectionalFluidOutput> fluidHandlers = Collections.newSetFromMap(new ConcurrentHashMap<DirectionalFluidOutput, Boolean>());
		openList.add(node);
		while(!openList.isEmpty() && closedList.size()<1024)
		{
			ChunkCoordinates next = openList.get(0);
			if(world.blockExists(next.posX,next.posY,next.posZ))
			{
				TileEntity te = world.getTileEntity(next.posX,next.posY,next.posZ);
				if(!closedList.contains(next) && (te instanceof IFluidHandler || te instanceof TileTank))
				{
					if(te instanceof IFluidHandler || te instanceof TileTank)
						closedList.add(next);
					FluidTankInfo[] tankInfo;
					for(int i=0; i<6; i++)
					{
						ForgeDirection fd = ForgeDirection.getOrientation(i);
						if(world.blockExists(next.posX+fd.offsetX,next.posY+fd.offsetY,next.posZ+fd.offsetZ))
						{
							TileEntity te2 = world.getTileEntity(next.posX+fd.offsetX,next.posY+fd.offsetY,next.posZ+fd.offsetZ);
							if(te2 instanceof IFluidHandler)
								openList.add(new ChunkCoordinates(next.posX+fd.offsetX,next.posY+fd.offsetY,next.posZ+fd.offsetZ));
							else if(te2 instanceof IFluidHandler)
							{
								tankInfo = ((IFluidHandler) te2).getTankInfo(fd.getOpposite());
								if(tankInfo!=null && tankInfo.length>0)
								{
									IFluidHandler handler = (IFluidHandler) te2;
									fluidHandlers.add(new DirectionalFluidOutput(handler, fd));
								}
							}
						}
					}
				}
			}
			openList.remove(0);
		}
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
		{
			if(!indirectConnections.containsKey(node))
			{
				indirectConnections.put(node, newSetFromMap(new ConcurrentHashMap<DirectionalFluidOutput, Boolean>()));
				indirectConnections.get(node).addAll(fluidHandlers);
			}
		}
		return fluidHandlers;
	}

	boolean canOutputPressurized(IFluidHandler output, boolean consumePower){
		return false;
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket){
		this.energyStorage.readFromNBT(nbt);

		this.tank = new TankType(tankCapacity);
		if(nbt.hasKey("tank")){
		    this.tank.readFromNBT(nbt.getCompoundTag("tank"));
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket){
		this.energyStorage.writeToNBT(nbt);

		NBTTagCompound tank = new NBTTagCompound();
		this.tank.writeToNBT(tank);
		nbt.setTag("tank", tank);
	}

	public boolean canConnectEnergy(ForgeDirection from){
		return true;
	}

	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate){
		return this.energyStorage.receiveEnergy(maxReceive, simulate);
	}

	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate){
		return 0;
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

	public static TankType loadTank(NBTTagCompound nbtRoot) {
		int tankType = nbtRoot.getInteger("tankType");
		tankType = MathHelper.clamp_int(tankType, 0, 1);
		TankType ret;

		ret = new TankType(16000);

		if(nbtRoot.hasKey("tank")) {
			FluidStack fl = FluidStack.loadFluidStackFromNBT((NBTTagCompound) nbtRoot.getTag("tank"));
			ret.setFluid(fl);
		}else{
			ret.setFluid(null);
		}
		return ret;
	}

	public NBTTagCompound getItemNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		tank.writeToNBT(nbt);
		return nbt;
	}

	public IFluidTank getTank() {
		return tank;
	}

	public void onBlockPlacedBy(EntityLivingBase placer, ItemStack stack) {
		NBTTagCompound itemTag = stack.getTagCompound();

		if (itemTag != null && itemTag.hasKey("tank")) {
			tank.readFromNBT(itemTag.getCompoundTag("tank"));
		}
	}

	/*IFluidHandler*/

	public static int getTankCapacity() {
		return FluidContainerRegistry.BUCKET_VOLUME * 1;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain){
		if(resource == null){
			return null;
		}
		if(tank.getFluidType() == resource.getFluid()){
			return tank.drain(resource.amount, doDrain);
		}
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain){
		return this.tank.drain(maxDrain, doDrain);
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill){
		if (resource == null || resource.getFluid() == null){
			return 0;
		}

		FluidStack current = this.tank.getFluid();
		FluidStack resourceCopy = resource.copy();
		if (current != null && current.amount > 0 && !current.isFluidEqual(resourceCopy)){
			return 0;
		}

		int i = 0;
		int used = this.tank.fill(resourceCopy, doFill);
		resourceCopy.amount -= used;
		i += used;

		return i;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return fluid != null && this.tank.isEmpty() && this.tank.getFluidType() == null;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[]{tank.getInfo()};
	}

	public static class DirectionalFluidOutput
	{
		IFluidHandler output;
		ForgeDirection direction;

		public DirectionalFluidOutput(IFluidHandler output, ForgeDirection direction)
		{
			this.output = output;
			this.direction = direction;
		}
	}
}
