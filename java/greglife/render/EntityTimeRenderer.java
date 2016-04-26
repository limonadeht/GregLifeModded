package greglife.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class EntityTimeRenderer extends RenderLiving
{
	  private static final ResourceLocation texture = new ResourceLocation("greglife", "time.png");

	  public EntityTimeRenderer(ModelBase model, float f)
	  {
	    super(model, f);
	  }

	  protected ResourceLocation getEntityTexture(Entity entity)
	  {
	    return texture;
	  }
	}

