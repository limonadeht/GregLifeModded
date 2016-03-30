package greglife.handler;

import cpw.mods.fml.common.network.IGuiHandler;
import greglife.gui.ContainerBurningGenerator;
import greglife.gui.GuiBurningGenerator;
import greglife.tileentity.TileBurningGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);

		if(tile instanceof TileBurningGenerator){
			return new ContainerBurningGenerator(player.inventory, (TileBurningGenerator)tile);
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);

		if(tile instanceof TileBurningGenerator){
			return new GuiBurningGenerator(player.inventory, (TileBurningGenerator)tile);
		}

		return null;
	}
}
