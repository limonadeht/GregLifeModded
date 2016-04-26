package greglife.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public class GLTextHelper {

	private static final EnumChatFormatting[] fabulousness = new EnumChatFormatting[] {EnumChatFormatting.RED, EnumChatFormatting.GOLD, EnumChatFormatting.YELLOW, EnumChatFormatting.GREEN, EnumChatFormatting.AQUA, EnumChatFormatting.BLUE, EnumChatFormatting.LIGHT_PURPLE};
	public static String makeFabulous(String input) {
		return ludicrousFormatting(input, fabulousness, 80.0, 1, 1);
	}

	private static final EnumChatFormatting[] sanic = new EnumChatFormatting[] {EnumChatFormatting.BLUE, EnumChatFormatting.BLUE, EnumChatFormatting.BLUE, EnumChatFormatting.BLUE, EnumChatFormatting.WHITE, EnumChatFormatting.BLUE, EnumChatFormatting.WHITE, EnumChatFormatting.WHITE, EnumChatFormatting.BLUE, EnumChatFormatting.WHITE, EnumChatFormatting.WHITE, EnumChatFormatting.BLUE, EnumChatFormatting.RED, EnumChatFormatting.WHITE, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY};
	public static String makeSANIC(String input) {
		return ludicrousFormatting(input, sanic, 50.0, 2,1);
	}

	public static String ludicrousFormatting(String input, EnumChatFormatting[] colours, double delay, int step, int posstep) {
		StringBuilder sb = new StringBuilder(input.length()*3);
		if (delay <= 0) {
			delay = 0.001;
		}

		int offset = (int) Math.floor(Minecraft.getSystemTime() / delay) % colours.length;

		for (int i=0; i<input.length(); i++) {
			char c = input.charAt(i);

			int col = ((i * posstep) + colours.length - offset) % colours.length;

			sb.append(colours[col].toString());
			sb.append(c);
		}

		return sb.toString();
	}
}
