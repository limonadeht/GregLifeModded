package greglife;

import java.util.Random;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import greglife.network.ButtonPacket;
import net.minecraft.client.Minecraft;

public class CommonProxy {

	public static Random rand = new Random();

	public void preInit(FMLPreInitializationEvent event){
		initializeNetwork();
	}

	public void initializeNetwork(){
		GregLife.network = NetworkRegistry.INSTANCE.newSimpleChannel(GregLife.networkChannelName);
		GregLife.network.registerMessage(ButtonPacket.Handler.class, ButtonPacket.class, 0, Side.SERVER);
	}


	public boolean isShiftKeyDown() {
		return false;
	}

	public boolean isSpaceKeyDown() {
		return false;
	}

	public void registerRenderers(){

	}

	public void registerTileEntity(){
/*		GameRegistry.registerTileEntity(TileSolarPanel.class, "gl.blockMachine.1");
*/	}

	public void bindGLTexture(String textureFile) {}

	public void bindTexture(String textureFile) {}

	public Minecraft getClientInstance(){
		return FMLClientHandler.instance().getClient();
	}
}
