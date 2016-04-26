package greglife.tileentity;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileGenerating;

public class SubTileGregLifeEndoflame extends SubTileGenerating{

	private static final String TAG_BURN_TIME = "burnTime";
	private static final int FUEL_CAP = 32000 * 3;
	private static final int RANGE = 3;

	int burnTime = 0;

	@SuppressWarnings("unchecked")
	@Override
	public void onUpdate() {
		super.onUpdate();

		if(linkedCollector != null) {
			if(burnTime == 0) {
				if(mana < getMaxMana()) {
					boolean didSomething = false;

					int slowdown = getSlowdownFactor();

					List<EntityItem> items = supertile.getWorldObj().getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(supertile.xCoord - RANGE, supertile.yCoord - RANGE, supertile.zCoord - RANGE, supertile.xCoord + RANGE + 1, supertile.yCoord + RANGE + 1, supertile.zCoord + RANGE + 1));
					for(EntityItem item : items) {
						if(item.age >= (59 + slowdown) && !item.isDead) {
							ItemStack stack = item.getEntityItem();
							if(stack.getItem().hasContainerItem(stack))
								continue;

							int burnTime = stack == null || stack.getItem() == Item.getItemFromBlock(Blocks.wooden_door) ? 0 : TileEntityFurnace.getItemBurnTime(stack);
							if(burnTime > 0 && stack.stackSize > 0) {
								this.burnTime = Math.min(FUEL_CAP, burnTime) / 2;

								if(!supertile.getWorldObj().isRemote) {
									stack.stackSize--;
									supertile.getWorldObj().playSoundEffect(supertile.xCoord, supertile.yCoord, supertile.zCoord, "botania:endoflame", 0.2F, 1F);

									if(stack.stackSize == 0)
										item.setDead();

									didSomething = true;
								} else {
						            item.worldObj.spawnParticle("largesmoke", item.posX, item.posY + 0.1, item.posZ, 0.0D, 0.0D, 0.0D);
						            item.worldObj.spawnParticle("flame", item.posX, item.posY, item.posZ, 0.0D, 0.0D, 0.0D);
								}


								break;
							}
						}
					}

					if(didSomething)
						sync();
				}
			} else {
				if(supertile.getWorldObj().rand.nextInt(10) == 0)
		            supertile.getWorldObj().spawnParticle("flame", supertile.xCoord + 0.4 + Math.random() * 0.2, supertile.yCoord + 0.65, supertile.zCoord + 0.4 + Math.random() * 0.2, 0.0D, 0.0D, 0.0D);

				//burnTime--;
				for(int b = 0; b > 4; b = 0){
					burnTime--;
				}
			}
		}
	}

	@Override
	public int getMaxMana() {
		return 300 * 50;
	}

	@Override
	public int getValueForPassiveGeneration() {
		return 30;
	}

	@Override
	public int getColor() {
		return 0x785000;
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
    public IIcon getIcon(){
        return BotaniaAPI.getSignatureForName("greglifeEndoFlame").getIconForStack(null);
    }

	@Override
	public void writeToPacketNBT(NBTTagCompound cmp) {
		super.writeToPacketNBT(cmp);

		cmp.setInteger(TAG_BURN_TIME, burnTime);
	}

	@Override
	public void readFromPacketNBT(NBTTagCompound cmp) {
		super.readFromPacketNBT(cmp);

		burnTime = cmp.getInteger(TAG_BURN_TIME);
	}

	@Override
	public boolean canGeneratePassively() {
		return burnTime > 0;
	}

	@Override
	public int getDelayBetweenPassiveGeneration() {
		return 2;
	}
}
