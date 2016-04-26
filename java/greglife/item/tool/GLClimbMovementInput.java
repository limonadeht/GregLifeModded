package greglife.item.tool;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.MovementInput;

@SideOnly(Side.CLIENT)
public class GLClimbMovementInput
	extends MovementInput
	{
	  public MovementInput originalInput;

	  public GLClimbMovementInput(MovementInput input)
	  {
	    this.originalInput = input;
	  }

	  public void updatePlayerMoveState()
	  {
	    this.originalInput.updatePlayerMoveState();
	    this.jump = this.originalInput.jump;
	    this.sneak = this.originalInput.sneak;
	  }
}
