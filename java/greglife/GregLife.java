package greglife;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import greglife.handler.GLTickHandler;
import greglife.handler.GuiHandler;
import greglife.handler.TimeHandler;
import greglife.recipe.GLRecipe;
import greglife.util.IUpgradeRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = GregLife.MOD_ID, version = GregLife.VERSION)
public class GregLife {

	@Mod.Instance("GregLife")
	public static GregLife Instance;
	public static final String MOD_ID = "GregLife";
	public static final String VERSION = "Alpha-6.1";

	@SidedProxy(clientSide = "greglife.ClientProxy", serverSide = "greglife.CommonProxy")
	public static CommonProxy proxy;

	public static final String networkChannelName = "GLifeNC";
	public static SimpleNetworkWrapper network;

	public static IUpgradeRegistry Upgrade_Registry = new UpgradeRegistry();

	public static boolean debug = false;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());

		try{
			config.load();
		}catch (Exception e){}finally{
			config.save();
		}

		proxy.preInit(event);
    	FMLCommonHandler.instance().bus().register(new GLEvent());

    	MinecraftForge.EVENT_BUS.register(new TimeHandler());
    	MinecraftForge.EVENT_BUS.register(new GLTickHandler());

	}

	@SuppressWarnings("static-access")
	@EventHandler
	public void Init(FMLPreInitializationEvent e){

		GLContent.instance.addContents();
		GLContent.instance.load();
		GLContent.instance.registerFluid();

		GLRecipe.instance.loadRecipe();

		proxy.registerRenderers();
		proxy.registerTileEntity();

		NetworkRegistry.INSTANCE.registerGuiHandler(this.Instance, new GuiHandler());
	}

	@EventHandler
	public void postInitialise(FMLPostInitializationEvent e){

	}

	public static CreativeTabs GLTab = new CreativeTabs("gl.GLTab"){

		public Item getTabIconItem(){
			return GLContent.itemWrench;
		}
	};


}
