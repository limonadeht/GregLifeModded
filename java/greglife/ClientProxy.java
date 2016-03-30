package greglife;

import org.lwjgl.input.Keyboard;

public class ClientProxy extends CommonProxy{

	@Override
	public boolean isShiftKeyDown() {
		return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
	}

	@Override
	public boolean isSpaceKeyDown() {
		return Keyboard.isKeyDown(Keyboard.KEY_SPACE);
	}

	public void registerTileEntity(){
	}

	@Override
	public void registerRenderers(){

	}
}
