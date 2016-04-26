package greglife;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import greglife.block.BlockAdvancedNeutron;
import greglife.block.BlockBurningGenerator;
import greglife.block.BlockChikenCapturer;
import greglife.block.BlockCreativeCapacitor;
import greglife.block.BlockEggCollector;
import greglife.block.BlockElectricFurnace;
import greglife.block.BlockEnergyCable;
import greglife.block.BlockFluidCable;
import greglife.block.BlockGregLifeInfuser;
import greglife.block.BlockLavaCreator;
import greglife.block.BlockSolarPanel;
import greglife.block.BlockSprinkler;
import greglife.block.BlockStoneCreator;
import greglife.block.BlockStorage;
import greglife.block.BlockTank;
import greglife.block.BlockTimeTorch;
import greglife.block.basic.BlockGregLifeLeave;
import greglife.block.basic.BlockGregLifeLog;
import greglife.block.basic.BlockGregLifeOre;
import greglife.block.basic.BlockGregLifePlanks;
import greglife.block.basic.BlockGregLifeSapling;
import greglife.block.fluid.BlockLiquidGregLife;
import greglife.entity.EntityFrozenItem;
import greglife.entity.EntityTime;
import greglife.entity.EntityTimeItem;
import greglife.item.ItemAdvancedNeutron;
import greglife.item.ItemBase;
import greglife.item.ItemBurningGenerator;
import greglife.item.ItemChiken;
import greglife.item.ItemCosmosBag;
import greglife.item.ItemEggCollector;
import greglife.item.ItemElectricFurnace;
import greglife.item.ItemEnergyCable;
import greglife.item.ItemLavaCreator;
import greglife.item.ItemLiquidGLBucket;
import greglife.item.ItemSolarPanel;
import greglife.item.ItemSprinkler;
import greglife.item.ItemStoneCreator;
import greglife.item.ItemTank;
import greglife.item.ItemUpgradeBook;
import greglife.item.ItemUpgradeEnergy;
import greglife.item.tool.ItemCosmosPick;
import greglife.item.tool.ItemGregLifePick;
import greglife.item.tool.ItemGregLifePickaxe;
import greglife.item.tool.ItemWallPassingChisel;
import greglife.item.tool.ItemWrench;
import greglife.tileentity.SubTileElectroKassi;
import greglife.tileentity.SubTileGregLifeEndoflame;
import greglife.tileentity.SubTileGregLifePureDaisy;
import greglife.tileentity.TileAdvancedNeutron;
import greglife.tileentity.TileBurningGenerator;
import greglife.tileentity.TileChikenCapturer;
import greglife.tileentity.TileCreativeCapacitor;
import greglife.tileentity.TileEggCollector;
import greglife.tileentity.TileElectricFurnace;
import greglife.tileentity.TileEnergyCable;
import greglife.tileentity.TileFluidCable;
import greglife.tileentity.TileGregLifeInfuser;
import greglife.tileentity.TileLavaCreator;
import greglife.tileentity.TileSolarPanel;
import greglife.tileentity.TileSprinkler;
import greglife.tileentity.TileStoneCreator;
import greglife.tileentity.TileStorage;
import greglife.tileentity.TileTank;
import greglife.tileentity.TileTimeTorch;
import greglife.util.GLSignature;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import vazkii.botania.api.BotaniaAPI;

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
	public static Block blockLavaCreator;
	public static Block blockCreativeCapacitor;
	public static Block blockFluidCable;
	public static Block blockStoneCreator;
	public static Block blockSprinkler;
	public static Block blockTimeTorch;
	public static Block blockGregLifeInfuser;

	public static Block blockGregLifeOre;
	public static Block blockGregLifeLeave;
	public static Block blockGregLifeLog;
	public static Block blockGregLifeSapling;
	public static Block blockGregLifePlanks;

	public static Item itemWrench;
	public static Item itemWrenchCharged;
	public static Item itemUpgradeEnergy;
	public static Item itemChiken;
	public static Item itemWPchisel;
	public static Item itemCosmosBag;
	public static Item itemCosmosPick;
	public static Item itemGregLifePick;
	public static Item itemGregLifeCapacitor;
	public static Item itemGregLifeGem;
	public static Item itemGregLifeIngot;
	public static Item itemGregLifeNugget;
	public static Item itemGregLifePickaxe;
	public static Item itemUpgradeBook;
	public static Item itemLiquidGLBucket;

	public static Block blockliquidGregLife;
	public static Item itemResources;

	public static Fluid liquidGregLife = new Fluid("liquidForce").setLuminosity(15).setDensity(700).setGaseous(false).setTemperature(240).setViscosity(700);

	private GLContent(){};

	@SuppressWarnings("static-access")
	public void load(){

		GameRegistry.registerItem(itemWrench, "gl.itemWrench");
		GameRegistry.registerItem(itemWrenchCharged, "gl.itemWrench.charged");
		GameRegistry.registerItem(itemUpgradeEnergy, "gl.itemUpgradeEnergy");
		GameRegistry.registerItem(itemChiken, "gl.itemChiken");
		GameRegistry.registerItem(itemWPchisel, "gl.itemWPchisel");
		GameRegistry.registerItem(itemCosmosBag, "gl.itemCosmosBag");
		//GameRegistry.registerItem(itemCosmosPick, "gl.itemCosmosPick");
		//GameRegistry.registerItem(itemGregLifePick, "gl.itemGLPick");
		//GameRegistry.registerItem(itemGregLifeCapacitor, "gl.itemCapacitor");
		GameRegistry.registerItem(itemGregLifePickaxe, "gl.itemPick");
		GameRegistry.registerItem(itemUpgradeBook, "gl.itemUpgradeBook");
		GameRegistry.registerItem(itemLiquidGLBucket, "gl.itemLiquidGLBucket");

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
		GameRegistry.registerBlock(blockLavaCreator, ItemLavaCreator.class, "gl.blockMachine.14");
		GameRegistry.registerBlock(blockCreativeCapacitor, "gl.blockMachine.15");
		GameRegistry.registerBlock(blockFluidCable, "gl.blockMachine.16");
		GameRegistry.registerBlock(blockStoneCreator, ItemStoneCreator.class, "gl.blockMachine.17");
		GameRegistry.registerBlock(blockSprinkler, ItemSprinkler.class, "gl.blockMachine.18");
		GameRegistry.registerBlock(blockTimeTorch, "gl.blockSpecial");
		GameRegistry.registerBlock(blockGregLifeInfuser, "gl.blockInfuser");

		GameRegistry.registerBlock(blockGregLifeOre, "gl.blockOres");
		GameRegistry.registerBlock(blockGregLifeLeave, "gl.blockLeave");
		GameRegistry.registerBlock(blockGregLifeLog, "gl.blockLog");
		GameRegistry.registerBlock(blockGregLifeSapling, "gl.blockSapling");
		GameRegistry.registerBlock(blockGregLifePlanks, "gl.blockPlanks");

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
		GameRegistry.registerTileEntity(TileLavaCreator.class, "gl.blockMachine.14");
		GameRegistry.registerTileEntity(TileCreativeCapacitor.class, "gl.blockMachine.15");
		GameRegistry.registerTileEntity(TileFluidCable.class, "gl.blockMachine.16");
		GameRegistry.registerTileEntity(TileStoneCreator.class, "gl.blockMachine.17");
		GameRegistry.registerTileEntity(TileSprinkler.class, "gl.blockMachine.18");
		GameRegistry.registerTileEntity(TileTimeTorch.class, "gl.blockSpecial");
		GameRegistry.registerTileEntity(TileGregLifeInfuser.class, "gl.blockInfuser");

		EntityRegistry.registerGlobalEntityID(EntityTime.class, "gl.entityTime", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.instance().registerModEntity(EntityTime.class, "gl.entityTime", 1, GregLife.MOD_ID, 128, 1, true);
		EntityRegistry.registerGlobalEntityID(EntityTimeItem.class, "gl.entityTimeItem", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.instance().registerModEntity(EntityTimeItem.class, "gl.entityTimeItem", 2, GregLife.MOD_ID, 128, 1, true);
		EntityRegistry.registerGlobalEntityID(EntityFrozenItem.class, "gl.entityFrozenItem", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.instance().registerModEntity(EntityFrozenItem.class, "gl.entityFrozenItem", 3, GregLife.Instance, 128, 1, true);
		//RenderingRegistry.registerEntityRenderingHandler(EntityTime.class, new EntityTimeRenderer(new ModelPig(), 0.25F));

		BotaniaAPI.registerSubTile("electroKassi", SubTileElectroKassi.class);
		BotaniaAPI.registerSubTileSignature(SubTileElectroKassi.class, new GLSignature("electroKassi"));
		BotaniaAPI.addSubTileToCreativeMenu("electroKassi");

		BotaniaAPI.registerSubTile("greglifeEndoFlame", SubTileGregLifeEndoflame.class);
		BotaniaAPI.registerSubTileSignature(SubTileGregLifeEndoflame.class, new GLSignature("greglifeEndoFlame"));
		BotaniaAPI.addSubTileToCreativeMenu("greglifeEndoFlame");

		BotaniaAPI.registerSubTile("greglifePureDaisy", SubTileGregLifePureDaisy.class);
		BotaniaAPI.registerSubTileSignature(SubTileGregLifePureDaisy.class, new GLSignature("greglifePureDaisy"));
		BotaniaAPI.addSubTileToCreativeMenu("greglifePureDaisy");
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
		blockLavaCreator = new BlockLavaCreator("gl.blockMachine.14", 10000000);
		blockCreativeCapacitor = new BlockCreativeCapacitor("gl.blockMachine.15");
		blockFluidCable = new BlockFluidCable("gl.blockMachine.16");
		blockStoneCreator = new BlockStoneCreator("gl.blockMachine.17");
		blockSprinkler = new BlockSprinkler("gl.blockMachine.18");
		blockTimeTorch = new BlockTimeTorch("gl.blockSpecial");
		blockGregLifeInfuser = new BlockGregLifeInfuser("gl.blockInfuser");

		blockGregLifeOre = new BlockGregLifeOre("gl.blockOres");
		blockGregLifeLeave = new BlockGregLifeLeave("gl.blockLeave").setCreativeTab(GregLife.GLTab);
		blockGregLifeLog = new BlockGregLifeLog("gl.blockLog").setCreativeTab(GregLife.GLTab);
		blockGregLifeSapling = new BlockGregLifeSapling("gl.blockSapling").setCreativeTab(GregLife.GLTab);
		blockGregLifePlanks = new BlockGregLifePlanks("gl.blockPlanks").setCreativeTab(GregLife.GLTab);

		itemWrench = new ItemWrench();
		itemWrenchCharged = new ItemWrench.Charged();
		itemUpgradeEnergy = new ItemUpgradeEnergy("gl.itemUpgradeEnergy");
		itemChiken = new ItemChiken("gl.itemChiken");
		itemWPchisel = new ItemWallPassingChisel("gl.itemWPchisel");
		itemCosmosBag = new ItemCosmosBag("gl.itemCosmosBag");
		itemCosmosPick = new ItemCosmosPick("gl.itemCosmosPick");
		itemGregLifePick = new ItemGregLifePick("gl.itemGLPick");
		//itemGregLifeCapacitor = new ItemGregLifeCapacitor("gl.itemCapacitor");
		itemGregLifeGem = new ItemBase("gl.resource.1", 64).setUnlocalizedName("gl.itemGem").setCreativeTab(GregLife.GLTab);
		itemGregLifeIngot = new ItemBase("gl.resource.2", 64).setUnlocalizedName("gl.itemIngot").setCreativeTab(GregLife.GLTab);
		itemGregLifeNugget = new ItemBase("gl.resource.3", 64).setUnlocalizedName("gl.itemNugget").setCreativeTab(GregLife.GLTab);
		itemGregLifePickaxe = new ItemGregLifePickaxe("gl.itemPick");
		itemUpgradeBook = new ItemUpgradeBook("gl.itemUpgradeBook");
		itemLiquidGLBucket = new ItemLiquidGLBucket(liquidGregLife);

		itemResources = new ItemBase("itemResources", 64,
				"solarCore.t1", "solarCore.t2", "solarCore.t3",
				"generatorCore"
				);
	}

	static void registerFluid(){
		FluidRegistry.registerFluid(liquidGregLife);
		blockliquidGregLife = new BlockLiquidGregLife(liquidGregLife, Material.water).setBlockName("gl.blockFluidGL").setCreativeTab(GregLife.GLTab);
		liquidGregLife = liquidGregLife.setBlock(blockliquidGregLife).setUnlocalizedName("gl.blockLiquidGL");
		GameRegistry.registerBlock(blockliquidGregLife, "gl.blockLiquidGL");
	}
}
