package greglife.util;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.GregLife;
import greglife.util.effect.FXDisney;
import greglife.util.effect.FXTime;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.world.World;

public class FXutil {

	public static Random rand = new Random();

	@SideOnly(Side.CLIENT)
	public static void makeShiny(World world, double x, double y, double z, int type, int color, int num, boolean add){
		EffectRenderer renderer = GregLife.proxy.getClientInstance().effectRenderer;

		double offset = 0.0D;
		if (add) {
			offset += 0.5D;
		}
		for (int i = 0; i < num; i++)
		{
			FXDisney fx = new FXDisney(world, x + offset + world.rand.nextDouble() - world.rand.nextDouble(), y + world.rand.nextDouble() - world.rand.nextDouble(), z + offset + world.rand.nextDouble() - world.rand.nextDouble(), color, type);
			renderer.addEffect(fx);
		}
	}

	@SideOnly(Side.CLIENT)
	  public static void makeTimeEffects(World world, double x, double y, double z, int type, int num, int area)
	  {
	    EffectRenderer renderer = GregLife.proxy.getClientInstance().effectRenderer;
	    if (area > 0) {
	      for (int i = -area; i < area + 1; i++) {
	        for (int j = -area; j < area + 1; j++) {
	          for (int k = -area; k < area + 1; k++) {
	            for (int l = 0; l < num; l++)
	            {
	              FXTime fx = new FXTime(world, x + i + world.rand.nextDouble() - world.rand.nextDouble(), y + j + world.rand.nextDouble() - world.rand.nextDouble(), z + k + world.rand.nextDouble() - world.rand.nextDouble(), type);
	              renderer.addEffect(fx);
	            }
	          }
	        }
	      }
	    } else {
	      for (int i = 0; i < num; i++)
	      {
	        FXTime fx = new FXTime(world, x + world.rand.nextDouble() - world.rand.nextDouble(), y + world.rand.nextDouble() - world.rand.nextDouble(), z + world.rand.nextDouble() - world.rand.nextDouble(), type);
	        renderer.addEffect(fx);
	      }
	    }
	  }
}
