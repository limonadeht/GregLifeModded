package greglife.handler;

import cpw.mods.fml.common.network.IGuiHandler;
import greglife.gui.ContainerAdvancedNeutron;
import greglife.gui.ContainerBurningGenerator;
import greglife.gui.ContainerElectricFurnace;
import greglife.gui.GuiAdvancedNeutron;
import greglife.gui.GuiBurningGenerator;
import greglife.gui.GuiElectricFurnace;
import greglife.tileentity.TileAdvancedNeutron;
import greglife.tileentity.TileBurningGenerator;
import greglife.tileentity.TileElectricFurnace;
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
		if(tile instanceof TileAdvancedNeutron){
			return new ContainerAdvancedNeutron(player.inventory, (TileAdvancedNeutron)tile);
		}
		if(tile instanceof TileElectricFurnace){
			return new ContainerElectricFurnace(player.inventory, (TileElectricFurnace)tile);
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);

		if(tile instanceof TileBurningGenerator){
			return new GuiBurningGenerator(player.inventory, (TileBurningGenerator)tile);
		}
		if(tile instanceof TileAdvancedNeutron){
			return new GuiAdvancedNeutron(player.inventory, (TileAdvancedNeutron)tile);
		}
		if(tile instanceof TileElectricFurnace){
			return new GuiElectricFurnace(player.inventory, (TileElectricFurnace)tile);
		}

		return null;
	}
}
