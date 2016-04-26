package greglife.tileentity;

import java.util.Random;

import cofh.api.energy.IEnergyHandler;
import greglife.energy.EnergyStorage;
import greglife.util.TankType;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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

public class TileSprinkler extends TileEntityBase implements IEnergyHandler, IFluidHandler{

	private EnergyStorage energyStorage;
	private int energyCapacity = 100000;
	private int energyTransfer = Integer.MAX_VALUE;
	private int energyPerGrowth = 2000;

	public int tankCapacity = 16000;
	public TankType tank = new TankType(tankCapacity);

	private final Random random = new Random();
	private int power = this.random.nextInt(1000) + 700;

	private int updateTick;

	public TileSprinkler(){
		this.energyStorage = new EnergyStorage(this.energyCapacity, this.energyTransfer);
	}

	public int getPower(){
		return this.power;
	}

	@Override
	public void updateEntity(){
		this.setEnergyStored(this.getEnergyStored() - 10);

		updateTick++;
		if(this.updateTick >= 15 && this.tank.getFluid().amount > 0){
			this.growing(worldObj, xCoord, yCoord, zCoord);
			this.particleWithGrow(worldObj, xCoord, yCoord, zCoord, random);
			this.updateTick = 0;
		}

		if(this.tank.getFluid() == null){
			this.tank.setFluid(new FluidStack(FluidRegistry.WATER, 0));
		}
	}

	public void growing(World world, int x, int y, int z){
		if(this.getEnergyStored() > 0 && !world.isRemote){
			for(int xx = x - 4; xx <= x + 4; xx++){
				for(int yy = y - 4; yy <= y + 4; yy++){
					for(int zz = z - 4; zz <= z + 4; zz++){
						Block block = world.getBlock(xx, yy, zz);
						if(world.getBlock(xx, yy, zz).getMaterial() == Material.plants){
							block.updateTick(world, xx, yy, zz, random);
							this.setEnergyStored(this.getEnergyStored() - this.energyPerGrowth);
							this.tank.setAmount(this.tank.getFluid().amount - 100);
						}
					}
				}
			}
		}
		if(world.isRemote){
			return;
		}
	}

	public void particleWithGrow(World world, int x, int y, int z, Random random){
		float f1 = (float)x + 0.5F;
		float f2 = (float)y + 1.1F;
		float f3 = (float)z + 0.5F;
		float f4 = random.nextFloat() * 0.6F -0.3F;
		float f5 = random.nextFloat() * -0.6F - -0.3F;

		world.spawnParticle("reddust", (double)(f1+f4), (double)f2, (double)(f3+f5), 0.0D, 0.0D, 0.0D);
		world.spawnParticle("reddust", (double)(f1+f4), (double)f2, (double)(f3+f5), 0.0D, 0.0D, 0.0D);
		world.spawnParticle("reddust", (double)(f1+f4), (double)f2, (double)(f3+f5), 0.0D, 0.0D, 0.0D);
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
}
