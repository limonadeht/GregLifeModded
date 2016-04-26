package greglife.tileentity;

import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.util.TankType;
import greglife.util.Utils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;

public class TileFluidCable extends TileEntityBase implements IFluidHandler{

	static ConcurrentHashMap<ChunkCoordinates, Set<DirectionalFluidOutput>> indirectConnections = new ConcurrentHashMap<ChunkCoordinates, Set<DirectionalFluidOutput>>();

	public int tankCapacity = 1000;
	public TankType tank = new TankType(tankCapacity);

	public TileFluidCable(){
	}

	@Override
	public void updateEntity(){
		if (!worldObj.isRemote)
			indirectConnections.clear();
	}

	public static Set<DirectionalFluidOutput> getConnectedFluidHandlers(ChunkCoordinates node, World world)
	{
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
				if(!closedList.contains(next) && (te instanceof TileFluidCable || te instanceof TileTank))
				{
					if(te instanceof TileFluidCable)
						closedList.add(next);
					FluidTankInfo[] tankInfo;
					for(int i=0; i<6; i++)
					{
						ForgeDirection fd = ForgeDirection.getOrientation(i);
						if(world.blockExists(next.posX+fd.offsetX,next.posY+fd.offsetY,next.posZ+fd.offsetZ))
						{
							TileEntity te2 = world.getTileEntity(next.posX+fd.offsetX,next.posY+fd.offsetY,next.posZ+fd.offsetZ);
							if(te2 instanceof TileFluidCable)
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

	@SideOnly(Side.CLIENT)
	public IIcon getFluidIcon(){
		Fluid fluid = this.tank.getFluidType();
    	return fluid != null ? fluid.getIcon() : null;
    }

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket){
		this.tank = new TankType(tankCapacity);
		if(nbt.hasKey("tank")){
		    this.tank.readFromNBT(nbt.getCompoundTag("tank"));
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket){
		NBTTagCompound tank = new NBTTagCompound();
		this.tank.writeToNBT(tank);
		nbt.setTag("tank", tank);
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
		/*if(resource == null){
			return null;
		}
		if(tank.getFluidType() == resource.getFluid()){
			return tank.drain(resource.amount, doDrain);
		}
		return null;*/
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain){
		//return this.tank.drain(maxDrain, doDrain);
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill){
		if(resource==null || from==null || from==ForgeDirection.UNKNOWN || worldObj.isRemote)
			return 0;

		int canAccept = resource.amount;
		if(canAccept<=0)
			return 0;

		ArrayList<DirectionalFluidOutput> outputList = new ArrayList(getConnectedFluidHandlers(new ChunkCoordinates(xCoord,yCoord,zCoord), worldObj));
		if(outputList.size()<1)
			return 0;
		ChunkCoordinates ccFrom = new ChunkCoordinates(xCoord+from.offsetX,yCoord+from.offsetY,zCoord+from.offsetZ);
		int sum = 0;
		HashMap<DirectionalFluidOutput,Integer> sorting = new HashMap<DirectionalFluidOutput,Integer>();
		for(DirectionalFluidOutput output : outputList)
		{
			ChunkCoordinates cc = Utils.toCC(output.output);
			if(!cc.equals(ccFrom) && worldObj.blockExists(cc.posX,cc.posY,cc.posZ) && output.output.canFill(output.direction.getOpposite(), resource.getFluid()))
			{
				int limit = (resource.tag!=null&&resource.tag.hasKey("pressurized"))||canOutputPressurized(output.output, false)?1000: 50;
				int tileSpecificAcceptedFluid = Math.min(limit, canAccept);
				int temp = output.output.fill(output.direction.getOpposite(), Utils.copyFluidStackWithAmount(resource, tileSpecificAcceptedFluid,true), false);
				if(temp>0)
				{
					sorting.put(output, temp);
					sum += temp;
				}
			}
		}
		if(sum>0)
		{
			int f = 0;
			for(DirectionalFluidOutput output : sorting.keySet())
			{
				int amount = sorting.get(output);
				int r = output.output.fill(output.direction.getOpposite(), Utils.copyFluidStackWithAmount(resource, amount, true), doFill);
				if(r>50)
					canOutputPressurized(output.output, true);
				f += r;
				canAccept -= r;
				if(canAccept<=0)
					break;
			}
			return f;
		}
		return 0;
	}

	boolean canOutputPressurized(IFluidHandler output, boolean consumePower)
	{
		/*if(output instanceof Tile)
		{
			int accelPower = Config.getInt("pump_consumption_accelerate");
			if(((TileEntityFluidPump)output).energyStorage.extractEnergy(accelPower, true)>=accelPower)
			{
				if(consumePower)
					((TileEntityFluidPump)output).energyStorage.extractEnergy(accelPower, false);
				return true;
			}
		}*/
		return false;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return fluid != null && this.tank.isEmpty() && this.tank.getFluidType() == null;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return false;
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
