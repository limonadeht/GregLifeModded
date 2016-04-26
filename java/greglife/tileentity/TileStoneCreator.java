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

public class TileStoneCreator extends TileEntityBase implements IFluidHandler {

	public int tankCapacity = 16000;
	public TankType waterTank = new TankType(tankCapacity);
	public TankType lavaTank = new TankType(tankCapacity);

	public TileStoneCreator(){
	}

	@Override
    public void updateEntity(){
		//this.waterTank.setFluid(new FluidStack(FluidRegistry.WATER, this.waterTank.isEmpty() ? this.waterTank.getFluidAmount() : 0));
		//this.lavaTank.setFluid(new FluidStack(FluidRegistry.LAVA, this.lavaTank.isEmpty() ? this.lavaTank.getFluidAmount() : 0));
	}

	@SideOnly(Side.CLIENT)
	public IIcon getFluidIcon(){
		Fluid fluid = this.waterTank.getFluidType();
    	return fluid != null ? fluid.getIcon() : null;
    }

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket){
		this.waterTank = new TankType(tankCapacity);
		this.lavaTank = new TankType(tankCapacity);

		if(nbt.hasKey("WaterTank")){
		    this.waterTank.readFromNBT(nbt.getCompoundTag("WaterTank"));
		}
		if(nbt.hasKey("LavaTank")){
			this.lavaTank.readFromNBT(nbt.getCompoundTag("LavaTank"));
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket){
		NBTTagCompound tankTag = new NBTTagCompound();

		this.waterTank.writeToNBT(tankTag);
		this.lavaTank.writeToNBT(tankTag);

		nbt.setTag("WaterTank", tankTag);
		nbt.setTag("LavaTank", tankTag);
	}

	public static TankType loadTank(NBTTagCompound nbtRoot) {
		int tankType = nbtRoot.getInteger("tankType");
		tankType = MathHelper.clamp_int(tankType, 0, 1);
		TankType ret;

		ret = new TankType(16000);

		if(nbtRoot.hasKey("WaterTank")) {
			FluidStack water = FluidStack.loadFluidStackFromNBT((NBTTagCompound) nbtRoot.getTag("WaterTank"));
			ret.setFluid(water);
		}else{
			ret.setFluid(null);
		}

		if(nbtRoot.hasKey("LavaTank")) {
			FluidStack lava = FluidStack.loadFluidStackFromNBT((NBTTagCompound) nbtRoot.getTag("LavaTank"));
			ret.setFluid(lava);
		}else{
			ret.setFluid(null);
		}

		return ret;
	}

	public NBTTagCompound getItemNBT(){
		NBTTagCompound tank = new NBTTagCompound();
		this.waterTank.writeToNBT(tank);
		this.lavaTank.writeToNBT(tank);
		return tank;
	}

	public IFluidTank getTank(int type){
		if(type == 0){
			return this.waterTank;
		}
		if(type == 1){
			return this.lavaTank;
		}
		return null;
	}

	public void onBlockPlacedBy(EntityLivingBase placer, ItemStack stack) {
		NBTTagCompound itemTag = stack.getTagCompound();

		if(itemTag != null && itemTag.hasKey("WaterTank")){
			this.waterTank.readFromNBT(itemTag.getCompoundTag("WaterTank"));
		}

		if(itemTag != null && itemTag.hasKey("LavaTank")){
			this.waterTank.readFromNBT(itemTag.getCompoundTag("LavaTank"));
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
		if(this.waterTank.getFluidType() == resource.getFluid()){
			return this.waterTank.drain(resource.amount, doDrain);
		}
		if(this.lavaTank.getFluidType() == resource.getFluid()){
			return this.lavaTank.drain(resource.amount, doDrain);
		}
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain){
		return null;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill){

		 if(resource!=null)
			{
				int fill = 0;
				if(resource.isFluidEqual(this.waterTank.getFluid()))
					fill = this.waterTank.fill(resource, doFill);
				else if(resource.isFluidEqual(this.lavaTank.getFluid()))
					fill = this.lavaTank.fill(resource, doFill);
				else if(this.waterTank.getFluidAmount()<=0 && this.lavaTank.getFluidAmount()<=0)
					fill = this.waterTank.fill(resource, doFill);
				else
				{
					if(this.waterTank.getFluidAmount()>0)
						fill = this.waterTank.fill(resource, doFill);
					else if(this.lavaTank.getFluidAmount()>0)
						fill = this.waterTank.fill(resource, doFill);
				}
				markDirty();
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				return fill;
			}
			return 0;
		/*if (resource == null || resource.getFluid() == null){
			return 0;
		}

		FluidStack currentW = this.waterTank.getFluid();
		FluidStack currentL = this.lavaTank.getFluid();
		FluidStack resourceCopy = resource.copy();
		if (currentW != null && currentW.amount > 0 && !currentW.isFluidEqual(resourceCopy)){
			return 0;
		}
		if(currentL != null && currentL.amount > 0 && !currentL.isFluidEqual(resourceCopy)){
			return 0;
		}

		int i = 0;
		int usedW = this.waterTank.fill(resourceCopy, doFill);
		int usedL = this.lavaTank.fill(resourceCopy, doFill);
		resourceCopy.amount -= usedW;
		resourceCopy.amount -= usedL;
		i += usedW;
		i += usedL;

		return i;*/
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return fluid != null && this.waterTank.isEmpty() && this.waterTank.getFluidType() == null && this.lavaTank.isEmpty() && this.lavaTank.getFluidType() == null;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[]{this.waterTank.getInfo(), this.lavaTank.getInfo()};
	}
}
