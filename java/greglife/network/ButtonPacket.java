package greglife.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import greglife.gui.ContainerElectricFurnace;
import greglife.tileentity.TileElectricFurnace;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;

public class ButtonPacket implements IMessage{

	public static final byte ID_ELECTRICFURNACE0 = 0;
	public static final byte ID_ELECTRICFURNACE1 = 1;

	byte buttonId = 0;
	boolean state = false;

	public ButtonPacket(byte buttonId, boolean state) {
		this.buttonId = buttonId;
		this.state = state;
	}

	@Override
	public void fromBytes(ByteBuf bytes){
		this.buttonId = bytes.readByte();
		this.state = bytes.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf bytes){
		bytes.writeByte(buttonId);
		bytes.writeBoolean(state);
	}

	public static class Handler implements IMessageHandler<ButtonPacket, IMessage> {

		@Override
		public IMessage onMessage(ButtonPacket message, MessageContext ctx) {
			switch (message.buttonId) {
				case ID_ELECTRICFURNACE0:
				{
					Container container = ctx.getServerHandler().playerEntity.openContainer;
					if (container != null && container instanceof ContainerElectricFurnace){
						TileElectricFurnace tileEF = ((ContainerElectricFurnace) container).getTileEF();
						tileEF.reciveButtonEvent(message.buttonId);
					}
				}
				case ID_ELECTRICFURNACE1:
				{
					Container container = ctx.getServerHandler().playerEntity.openContainer;
					if (container != null && container instanceof ContainerElectricFurnace){
						TileElectricFurnace tileEF = ((ContainerElectricFurnace) container).getTileEF();
						tileEF.reciveButtonEvent(message.buttonId);
					}
					break;
				}
				default:
					break;
			}
			return null;
		}
	}
}
