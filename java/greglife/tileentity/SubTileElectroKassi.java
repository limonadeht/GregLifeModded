package greglife.tileentity;

import cofh.api.energy.IEnergyHandler;
import greglife.energy.EnergyStorage;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileGenerating;

public class SubTileElectroKassi extends SubTileGenerating implements IEnergyHandler{

	private static final int RANGE = 1;

	private EnergyStorage energyStorage;
	private final int energyCapacity = 50000;
	private final int energyTransfer = Integer.MAX_VALUE;

	public SubTileElectroKassi(){
		this.energyStorage = new EnergyStorage(energyCapacity, energyTransfer);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		int mana = 500;
		int usePower = 25000;

		if(getMaxMana() - this.mana >= mana && !supertile.getWorldObj().isRemote && ticksExisted % 80 == 0) {
			for(int i = 0; i < RANGE * 2 + 1; i++)
				for(int j = 0; j < RANGE * 2 + 1; j++)
					for(int k = 0; k < RANGE * 2 + 1; k++) {
						int x = supertile.xCoord + i - RANGE;
						int y = supertile.yCoord + j - RANGE;
						int z = supertile.zCoord + k - RANGE;
						Block block = supertile.getWorldObj().getBlock(x, y, z);
						TileEntity tile = supertile.getWorldObj().getTileEntity(x, y, z);
						if(tile instanceof IEnergyHandler) {
							//int meta = supertile.getWorldObj().getBlockMetadata(x, y, z) + 1;
							//if(meta == 6)
							//	supertile.getWorldObj().setBlockToAir(x, y, z);
							//else supertile.getWorldObj().setBlockMetadataWithNotify(x, y, z, meta, 1 | 2);
							if(((IEnergyHandler) tile).getEnergyStored(ForgeDirection.UNKNOWN) > 25000){
								((IEnergyHandler) tile).extractEnergy(null, usePower, false);
							}
							supertile.getWorldObj().playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block));
							supertile.getWorldObj().playSoundEffect(supertile.xCoord, supertile.yCoord, supertile.zCoord, "random.eat", 1F, 0.5F + (float) Math.random() * 0.5F);
							this.mana += mana;
							sync();
							return;
						}
					}
		}

		/*if(this.getMaxMana() - this.mana >= mana && !this.supertile.getWorldObj().isRemote && this.ticksExisted % 80 == 0){
			if(this.getEnergyStored() + usePower < this.getMaxEnergyStored()){
				this.extractEnergy(ForgeDirection.UNKNOWN, usePower, false);
				this.mana += mana;
				sync();
				return;
			}
		}*/
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);
		this.energyStorage.readFromNBT(cmp);
	}

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);
		this.energyStorage.writeToNBT(cmp);
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toChunkCoordinates(), RANGE);
	}

	@Override
	public LexiconEntry getEntry() {
		return null;
	}

	@Override
	public int getColor() {
		return 0x935D28;
	}

	@Override
	public int getMaxMana() {
		return 9001;
	}

	@Override
    public IIcon getIcon(){
        return BotaniaAPI.getSignatureForName("electroKassi").getIconForStack(null);
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
}
