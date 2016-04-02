package greglife;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import greglife.handler.GuiHandler;
import greglife.recipe.GLRecipe;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = GregLife.MOD_ID, version = GregLife.VERSION)
public class GregLife {

	@Mod.Instance("GregLife")
	public static GregLife Instance;
	public static final String MOD_ID = "GregLife";
	public static final String VERSION = "Alpha-1.0";

	@SidedProxy(clientSide = "greglife.ClientProxy", serverSide = "greglife.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());

		try{
			config.load();
		}catch (Exception e){}finally{
			config.save();
		}
	}

	@SuppressWarnings("static-access")
	@EventHandler
	public void Init(FMLPreInitializationEvent e){

		GLContent.instance.addContents();
		GLContent.instance.load();

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
