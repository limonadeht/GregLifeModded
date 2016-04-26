package greglife.handler;

import cpw.mods.fml.common.network.IGuiHandler;
import greglife.gui.ContainerAdvancedNeutron;
import greglife.gui.ContainerBurningGenerator;
import greglife.gui.ContainerChikenCapturer;
import greglife.gui.ContainerCosmosBag;
import greglife.gui.ContainerEggCollector;
import greglife.gui.ContainerElectricFurnace;
import greglife.gui.ContainerGregLifeInfuser;
import greglife.gui.ContainerStorage;
import greglife.gui.GuiAdvancedNeutron;
import greglife.gui.GuiBurningGenerator;
import greglife.gui.GuiChikenCapturer;
import greglife.gui.GuiCosmosBag;
import greglife.gui.GuiEggCollector;
import greglife.gui.GuiElectricFurnace;
import greglife.gui.GuiGregLifeInfuser;
import greglife.gui.GuiStorage;
import greglife.tileentity.TileAdvancedNeutron;
import greglife.tileentity.TileBurningGenerator;
import greglife.tileentity.TileChikenCapturer;
import greglife.tileentity.TileEggCollector;
import greglife.tileentity.TileElectricFurnace;
import greglife.tileentity.TileGregLifeInfuser;
import greglife.tileentity.TileStorage;
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
		if(tile instanceof TileEggCollector){
			return new ContainerEggCollector(player.inventory, (TileEggCollector)tile);
		}
		if(tile instanceof TileChikenCapturer){
			return new ContainerChikenCapturer(player.inventory, (TileChikenCapturer)tile);
		}
		if(tile instanceof TileStorage){
			return new ContainerStorage(player.inventory, (TileStorage)tile);
		}
		if(tile instanceof TileGregLifeInfuser){
			return new ContainerGregLifeInfuser((TileGregLifeInfuser)world.getTileEntity(x, y, z), player, 176, 200);
		}

		if(ID == 6){
			return new ContainerCosmosBag(player.inventory, world);
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
		if(tile instanceof TileEggCollector){
			return new GuiEggCollector(player.inventory, (TileEggCollector)tile);
		}
		if(tile instanceof TileChikenCapturer){
			return new GuiChikenCapturer(player.inventory, (TileChikenCapturer)tile);
		}
		if(tile instanceof TileStorage){
			return new GuiStorage(player.inventory, (TileStorage)tile);
		}
		if(tile instanceof TileGregLifeInfuser){
			return new GuiGregLifeInfuser(player, world, x, y ,z);
		}

		if(ID == 6){
			return new GuiCosmosBag(player.inventory, world);
		}

		return null;
	}
}
