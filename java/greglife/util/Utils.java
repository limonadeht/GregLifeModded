package greglife.util;

import java.util.Random;

import org.lwjgl.input.Keyboard;

import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.ModAPIManager;
import greglife.GregLife;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class Utils {

	public static Random rand = new Random();

	@SuppressWarnings("static-access")
	public static void dropItem(ItemStack stack, World world, double x, double y, double z){
		if((stack != null) && (world != null) /*&& (Proxies.common.isSimulating(world))*/){
			float xRand = GregLife.proxy.rand.nextFloat() * 0.2F + 0.1F;
			float yRand = GregLife.proxy.rand.nextFloat() * 0.8F + 0.1F;
			float zRand = GregLife.proxy.rand.nextFloat() * 0.2F + 0.1F;

			while (stack.stackSize > 0){
				int randInt = GregLife.proxy.rand.nextInt(21) + 10;
				if(randInt > stack.stackSize){
					randInt = stack.stackSize;
				}
				stack.stackSize -= randInt;

				EntityItem droppedItem = new EntityItem(world, (float)x + xRand, (float)y + yRand, (float)z + zRand, new ItemStack(stack.getItem(), randInt, stack.getItemDamage()));
				if (stack.hasTagCompound()) {
					droppedItem.getEntityItem().setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
				}
				float modifier = 0.025F;
				droppedItem.motionX = ((float)GregLife.proxy.rand.nextGaussian() * modifier);
				droppedItem.motionY = ((float)GregLife.proxy.rand.nextGaussian() * modifier + 0.2F);
				droppedItem.motionZ = ((float)GregLife.proxy.rand.nextGaussian() * modifier);
				droppedItem.ticksExisted = 10;
				world.spawnEntityInWorld(droppedItem);
			}
		}
	}

	public static float randomPitch(){
		return rand.nextFloat() * 0.25F + 0.85F;
	}

	public static boolean hasUsableWrench(EntityPlayer player, int x, int y, int z)
	  {
	    ItemStack tool = player.getCurrentEquippedItem();
	    if ((ModAPIManager.INSTANCE.hasAPI("BuildCraftAPI|tools")) && ((tool.getItem() instanceof IToolWrench)) && (((IToolWrench)tool.getItem()).canWrench(player, x, y, z))) {
	      return true;
	    }
	    return false;
	  }

	  public static boolean isControlKeyDown()
	  {
	    return (Keyboard.isKeyDown(29)) || (Keyboard.isKeyDown(157));
	  }

	  public static String localize(String string)
	  {
	    return StatCollector.translateToLocal(string);
	  }

	  public static String shiftForDetails()
	  {
	    return EnumChatFormatting.GRAY + localize("info.fmr.shiftkey.hold") + " " + EnumChatFormatting.AQUA + EnumChatFormatting.ITALIC + localize("info.fmr.shiftkey") + " " + EnumChatFormatting.GRAY + localize("info.fmr.shiftkey.down");
	  }

	  public static String controlForDetails()
	  {
	    return EnumChatFormatting.GRAY + localize("info.fmr.controlkey.hold") + " " + EnumChatFormatting.AQUA + EnumChatFormatting.ITALIC + localize("info.fmr.controlkey") + " " + EnumChatFormatting.GRAY + localize("info.fmr.controlkey.down");
	  }

	  /*public static String addEnergyInfo(ItemStack itemStack)
	  {
	    IEnergyContainerItem energyItem = (IEnergyContainerItem)itemStack.getItem();
	    int energy = energyItem.getEnergyStored(itemStack);
	    int maxEnergy = energyItem.getMaxEnergyStored(itemStack);
	    if (energy > maxEnergy / 4 * 3) {
	      return String.format(EnumChatFormatting.DARK_GREEN + "%,d / %,d", new Object[] { Integer.valueOf(energy), Integer.valueOf(maxEnergy) });
	    }
	    if ((energy <= maxEnergy / 4 * 3) && (energy > maxEnergy / 4)) {
	      return String.format(EnumChatFormatting.YELLOW + "%,d / %,d", new Object[] { Integer.valueOf(energy), Integer.valueOf(maxEnergy) });
	    }
	    if ((energy <= maxEnergy / 4) && (energy > 0)) {
	      return String.format(EnumChatFormatting.RED + "%,d / %,d", new Object[] { Integer.valueOf(energy), Integer.valueOf(maxEnergy) });
	    }
	    if (energy == 0) {
	      return String.format("%,d / %,d", new Object[] { Integer.valueOf(energy), Integer.valueOf(maxEnergy) });
	    }
	    return "This is a bug, please report to the mod author.";
	  }*/

	  public static String addEnergyGenerationTooltip()
	  {
	    return EnumChatFormatting.WHITE + localize("info.fmr.solarpanel.generation") + EnumChatFormatting.WHITE + ":";
	  }

	  public static String addEnergyTransferTooltip()
	  {
	    return EnumChatFormatting.WHITE + localize("info.fmr.solarpanel.transfer") + EnumChatFormatting.WHITE + ":";
	  }

	  public static String addEnergyCapacityTooltip()
	  {
	    return EnumChatFormatting.WHITE + localize("info.fmr.solarpanel.capacity") + EnumChatFormatting.WHITE + ":";
	  }

	  public static String addRFtTooltip()
	  {
	    return EnumChatFormatting.DARK_RED + " RF/t";
	  }

	  public static String addRFTooltip()
	  {
	    return EnumChatFormatting.DARK_RED + " RF";
	  }

	  public static NBTTagCompound getItemTag(ItemStack stack) {
			if (stack.stackTagCompound == null) stack.stackTagCompound = new NBTTagCompound();
			return stack.stackTagCompound;
	  }

	  public static ChunkCoordinates toCC(Object object)
	  {
		  if(object instanceof ChunkCoordinates)
			  return (ChunkCoordinates)object;
		  if(object instanceof TileEntity)
			  return new ChunkCoordinates(((TileEntity)object).xCoord,((TileEntity)object).yCoord,((TileEntity)object).zCoord);
		  return null;
	  }

	  public static FluidStack copyFluidStackWithAmount(FluidStack stack, int amount, boolean stripPressure)
	  {
		  if(stack==null)
			  return null;
		  FluidStack fs = new FluidStack(stack, amount);
		  if(stripPressure && fs.tag!=null && fs.tag.hasKey("pressurized"))
		  {
			  fs.tag.removeTag("pressurized");
			  if(fs.tag.hasNoTags())
				  fs.tag = null;
		  }
		  return fs;
	  }

	  public static void wispFX(World world, double x, double y, double z, float r, float g, float b, float size) {
		  wispFX(world, x, y, z, r, g, b, size, 0F);
	  }

	  public static void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float gravity) {
		  wispFX(world, x, y, z, r, g, b, size, gravity, 1F);
	  }

	  public static void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float gravity, float maxAgeMul) {
		  wispFX(world, x, y, z, r, g, b, size, 0, -gravity, 0, maxAgeMul);
	  }

	  public static void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz) {
		  wispFX(world, x, y, z, r, g, b, size, motionx, motiony, motionz, 1F);
	  }

	  public static void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul) {
	  }
}
