package greglife.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileGregLifeSapling extends TileEntity{

	public static final int MAX_CYCLES = 3;
	public static String publicName = "forceSapling";
	public int currentCycle = 0;

	@Override
	public boolean canUpdate() {
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		//currentCycle = nbt.getInteger("currentCycle");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		//nbt.setInteger("currentCycle", currentCycle);
	}
}
