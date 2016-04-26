package greglife.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.GLContent;
import greglife.GregLife;
import greglife.util.ItemNBTHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.oredict.OreDictionary;

public class TileGregLifeInfuser extends TileEntityInventory implements IFluidHandler{

	public TileEntityEnchantmentTable dummyTable;
	public static String publicName = "infuser";
	public FluidTank liquidForceTank = new FluidTank(GLContent.liquidGregLife, 0, 10000);

	public TileGregLifeInfuser() {
		super();
		int size = 11;
		this.items = new ItemStack[size];
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		liquidForceTank.writeToNBT(nbtTagCompound);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		liquidForceTank.readFromNBT(nbtTagCompound);
	}

	@Override
	public void validate() {
		super.validate();
		dummyTable = new TileEntityEnchantmentTable();
		dummyTable.setWorldObj(this.getWorldObj());
		dummyTable.xCoord = this.xCoord;
		dummyTable.yCoord = this.yCoord;
		dummyTable.zCoord = this.zCoord;
		dummyTable.blockMetadata = Integer.MIN_VALUE;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (dummyTable != null)
			dummyTable.updateEntity();
		if (getStackInSlot(0) != null) {
			if (getStackInSlot(0).getItem() == GLContent.itemUpgradeBook) {
				if (!ItemNBTHelper.hasTag(getStackInSlot(0), "tier")) {
					ItemNBTHelper.setInteger(getStackInSlot(0), "tier", 0);
					ItemNBTHelper.setInteger(getStackInSlot(0), "xp", 0);
				}
			}
		}
		if (getStackInSlot(1) != null) {
			if (getStackInSlot(1).getItem() == GLContent.itemLiquidGLBucket) {
				if (liquidForceTank.fill(new FluidStack(GLContent.liquidGregLife, 1000), false) == 1000) {
					liquidForceTank.fill(new FluidStack(GLContent.liquidGregLife, 1000), true);
					setInventorySlotContents(1, new ItemStack(Items.bucket));
				}
			} else if (isForceGem(getStackInSlot(1))) {
				if (liquidForceTank.fill(new FluidStack(GLContent.liquidGregLife, 1000), false) == 1000) {
					liquidForceTank.fill(new FluidStack(GLContent.liquidGregLife, 1000), true);
					ItemStack stack = getStackInSlot(1).copy();
					stack.stackSize--;
					setInventorySlotContents(1, stack.stackSize == 0 ? null : stack);
					NBTTagCompound tag = new NBTTagCompound();
					writeToNBT(tag);
//					CollectiveFramework.NETWORK.sendToServer(new TileEntityUpdatePacket(worldObj, xCoord, yCoord, zCoord, tag));
				}
			}
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 3, tag);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.func_148857_g());
	}

	public int getTier() {
		if (getStackInSlot(0) != null)
			if (getStackInSlot(0).getItem() == GLContent.itemUpgradeBook)
				return ItemNBTHelper.getInteger(getStackInSlot(0), "tier", 0);
		return -1;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		switch (slot) {
			case 0:
				return stack.getItem() == GLContent.itemUpgradeBook;
			case 1:
				return isForceGem(stack);
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
				return GregLife.Upgrade_Registry.getRegisteredItems().contains(stack.getItem());
			case 10:
				return GregLife.Upgrade_Registry.getTools().contains(stack.getItem());
		}
		return true;
	}

	private boolean isForceGem(ItemStack stack) {
		for (int id : OreDictionary.getOreIDs(stack)) {
			if (OreDictionary.getOreName(id).equals("gemForce"))
				return true;
		}
		return stack.getItem() == GLContent.itemLiquidGLBucket;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if (resource.getFluid() == GLContent.liquidGregLife)
			return liquidForceTank.fill(resource, doFill);
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return liquidForceTank.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return liquidForceTank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return fluid == GLContent.liquidGregLife;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return fluid == GLContent.liquidGregLife;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[]{liquidForceTank.getInfo()};
	}

	@Override
	public int getSize(){
		return 11;
	}
}
