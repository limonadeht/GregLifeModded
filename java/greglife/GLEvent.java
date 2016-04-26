package greglife;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class GLEvent {

	@SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent e) {
		EntityPlayer player = e.player;

		player.addChatComponentMessage(new ChatComponentText("==================================================="));
		player.addChatComponentMessage(new ChatComponentText("= §9Greg-Life-Mod §6is Updated! Version:Alpha-6.0"));
		//player.addChatComponentMessage(new ChatComponentText("= §4Author§7: §6柑橘類 §5(§eLemon1232§5)§r"));
		//player.addChatComponentMessage(new ChatComponentText("= §4Server Owner§7: §6ホノン(ティア) §5(§eHonon§5)§r"));
		player.addChatComponentMessage(new ChatComponentText("="));
		player.addChatComponentMessage(new ChatComponentText("= §bAdded§7: §dCreative Energy Capacitor§r"));
		player.addChatComponentMessage(new ChatComponentText("= §bAdded§7: §9Lava Creator§r"));
		player.addChatComponentMessage(new ChatComponentText("= §bAdded§7: §9Fluid Cable§r"));
		player.addChatComponentMessage(new ChatComponentText("= §bAdded§7: §9Electrokassi §5[§aBotania§5]§r"));
		player.addChatComponentMessage(new ChatComponentText("= §bAdded§7: §9GregLife Endoflame §5[§aBotania§5]§r"));
		player.addChatComponentMessage(new ChatComponentText("= §bAdded§7: §9GregLife Pure Daisy §5[§aBotania§5]§r"));
		player.addChatComponentMessage(new ChatComponentText("="));
		player.addChatComponentMessage(new ChatComponentText("= §1Working§7: §9Chicken Capturer§r"));
		player.addChatComponentMessage(new ChatComponentText("= §1Working§7: §9Stone Creator§r"));
		player.addChatComponentMessage(new ChatComponentText("="));
		player.addChatComponentMessage(new ChatComponentText("= §eSpecialThanks§7: §6SpitefulFox §5[§aAvaritia§5]§r"));
		player.addChatComponentMessage(new ChatComponentText("=                      §6Vazkii §5[§aBotania§5]§r"));
		player.addChatComponentMessage(new ChatComponentText("====================================================="));

	}
}
