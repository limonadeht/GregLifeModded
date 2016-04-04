package greglife;

import cpw.mods.fml.common.registry.GameRegistry;
import greglife.block.BlockAdvancedNeutron;
import greglife.block.BlockBurningGenerator;
import greglife.block.BlockChikenCapturer;
import greglife.block.BlockEggCollector;
import greglife.block.BlockElectricFurnace;
import greglife.block.BlockEnergyCable;
import greglife.block.BlockSolarPanel;
import greglife.block.BlockStorage;
import greglife.block.BlockTank;
import greglife.item.ItemAdvancedNeutron;
import greglife.item.ItemBase;
import greglife.item.ItemBurningGenerator;
import greglife.item.ItemChiken;
import greglife.item.ItemEggCollector;
import greglife.item.ItemElectricFurnace;
import greglife.item.ItemEnergyCable;
import greglife.item.ItemSolarPanel;
import greglife.item.ItemTank;
import greglife.item.ItemUpgradeEnergy;
import greglife.item.ItemWrench;
import greglife.tileentity.TileAdvancedNeutron;
import greglife.tileentity.TileBurningGenerator;
import greglife.tileentity.TileChikenCapturer;
import greglife.tileentity.TileEggCollector;
import greglife.tileentity.TileElectricFurnace;
import greglife.tileentity.TileEnergyCable;
import greglife.tileentity.TileSolarPanel;
import greglife.tileentity.TileStorage;
import greglife.tileentity.TileTank;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class GLContent {

	public static GLContent instance = new GLContent();

	public static Block blockSolarPanel;
	public static Block blockSolarPanelAdvanced;
	public static Block blockSolarPanelGregLife;
	public static Block blockTank;
	public static Block blockTankAdvanced;
	public static Block blockTankGregLife;
	public static Block blockBurningGenerator;
	public static Block blockAdvancedNeutron;
	public static Block blockElectricFurnace;
	public static Block blockEggCollector;
	public static Block blockChikenCapturer;
	public static Block blockEnergyCable;
	public static Block blockStorage;

	public static Item itemWrench;
	public static Item itemWrenchCharged;
	public static Item itemUpgradeEnergy;
	public static Item itemChiken;

	public static Item itemResources;

	private GLContent(){};

	public void load(){

		GameRegistry.registerItem(itemWrench, "gl.itemWrench");
		GameRegistry.registerItem(itemWrenchCharged, "gl.itemWrench.charged");
		GameRegistry.registerItem(itemUpgradeEnergy, "gl.itemUpgradeEnergy");
		GameRegistry.registerItem(itemChiken, "gl.itemChiken");

		GameRegistry.registerBlock(blockSolarPanel, ItemSolarPanel.class, "gl.blockMachine.1");
		GameRegistry.registerBlock(blockSolarPanelAdvanced, ItemSolarPanel.class, "gl.blockMachine.2");
		GameRegistry.registerBlock(blockSolarPanelGregLife, ItemSolarPanel.class, "gl.blockMachine.3");
		GameRegistry.registerBlock(blockTank, ItemTank.class, "gl.blockMachine.4");
		GameRegistry.registerBlock(blockTankAdvanced, ItemTank.class, "gl.blockMachine.6");
		GameRegistry.registerBlock(blockTankGregLife, ItemTank.class, "gl.blockMachine.7");
		GameRegistry.registerBlock(blockBurningGenerator, ItemBurningGenerator.class, "gl.blockMachine.5");
		GameRegistry.registerBlock(blockAdvancedNeutron, ItemAdvancedNeutron.class, "gl.blockMachine.8");
		GameRegistry.registerBlock(blockElectricFurnace, ItemElectricFurnace.class, "gl.blockMachine.9");
		GameRegistry.registerBlock(blockEggCollector, ItemEggCollector.class, "gl.blockMachine.10");
		GameRegistry.registerBlock(blockChikenCapturer, "gl.blockMachine.11");
		GameRegistry.registerBlock(blockEnergyCable, ItemEnergyCable.class, "gl.blockMachine.12");
		GameRegistry.registerBlock(blockStorage, "gl.blockMachine.13");

		GameRegistry.registerTileEntity(TileSolarPanel.class, "gl.tile.blockMachine.1");
		GameRegistry.registerTileEntity(TileSolarPanel.Advanced.class, "gl.blockMachine.2");
		GameRegistry.registerTileEntity(TileSolarPanel.GregLife.class, "gl.blockMachine.3");
		GameRegistry.registerTileEntity(TileTank.class, "gl.blockMachine.4");
		GameRegistry.registerTileEntity(TileTank.Advanced.class, "gl.blockMachine.6");
		GameRegistry.registerTileEntity(TileTank.GregLife.class, "gl.blockMachine.7");
		GameRegistry.registerTileEntity(TileBurningGenerator.class, "gl.blockMachine.5");
		GameRegistry.registerTileEntity(TileAdvancedNeutron.class, "gl.blockMachine.8");
		GameRegistry.registerTileEntity(TileElectricFurnace.class, "gl.blockMachine.9");
		GameRegistry.registerTileEntity(TileEggCollector.class, "gl.blockMachine.10");
		GameRegistry.registerTileEntity(TileChikenCapturer.class, "gl.blockMachine.11");
		GameRegistry.registerTileEntity(TileEnergyCable.class, "gl.blockMachine.12");
		GameRegistry.registerTileEntity(TileStorage.class, "gl.blockMachine.13");
	}

	static void addContents(){

		blockSolarPanel = new BlockSolarPanel("gl.blockMachine.1", 512);
		blockSolarPanelAdvanced = new BlockSolarPanel.Advanced("gl.blockMachine.2", 1024);
		blockSolarPanelGregLife = new BlockSolarPanel.GregLife("gl.blockMachine.3", 100000);
		blockTank = new BlockTank("gl.blockMachine.4",  16000);
		blockTankAdvanced = new BlockTank.Advanced("gl.blockMachine.6", 50000000);
		blockTankGregLife = new BlockTank.GregLife("gl.blockMachine.7", 100000000);
		blockBurningGenerator = new BlockBurningGenerator("gl.blockMachine.5", 300);
		blockAdvancedNeutron = new BlockAdvancedNeutron("gl.blockMachine.8");
		blockElectricFurnace = new BlockElectricFurnace("gl.blockMachine.9", 10000000, false);
		blockEggCollector = new BlockEggCollector("gl.blockMachine.10");
		blockChikenCapturer = new BlockChikenCapturer("gl.blockMachine.11", 1000000);
		blockEnergyCable = new BlockEnergyCable("gl.blockMachine.12");
		blockStorage = new BlockStorage("gl.blockMachine.13");

		itemWrench = new ItemWrench();
		itemWrenchCharged = new ItemWrench.Charged();
		itemUpgradeEnergy = new ItemUpgradeEnergy("gl.itemUpgradeEnergy");
		itemChiken = new ItemChiken("gl.itemChiken");

		itemResources = new ItemBase("itemResources", 64,
				"solarCore.t1", "solarCore.t2", "solarCore.t3",
				"generatorCore");
	}
}
