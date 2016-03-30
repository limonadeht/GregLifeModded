package greglife.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.util.TankType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;

public class TileTank extends TileEntityBase implements IFluidHandler{

	public int tankCapacity = 16000;
	public TankType tank = new TankType(tankCapacity);

	public TileTank(){
		this(16000);
	}

	public TileTank(int tankCapacity){
		this.tankCapacity = tankCapacity;
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

	public static class Advanced extends TileTank{

		public Advanced(){
			this(50000000);
		}

		public Advanced(int tankCapacity){
			super(tankCapacity);
		}
	}

	public static class GregLife extends TileTank{

		public GregLife(){
			this(100000000);
		}

		public GregLife(int tankCapacity){
			super(tankCapacity);
		}
	}
}
