package cc.game.behaviors;

import cc.game.GameObject;
import cc.util.math.Vec;

/**
 * Makes the SpaceObject float freely in space, affected by gravity.
 *
 */

public class FreeFloatBehavior extends Behavior
{
	private static Behavior.Type type = Behavior.Type.MOVEMENT;

	private Vec acc = new Vec();

	private static FreeFloatBehavior instance = new FreeFloatBehavior();

	/**
	 * Calculates gravity force exercised on this GameObject by all other
	 * GameObjects. Then accelerate the object with this force.
	 */
	@Override
    public void perform( GameObject controlled, double dT )
	{
		getSimulation().calcGravitation( controlled, acc );
		controlled.getPhysModel().accelerate( acc );
	}

	@Override
    public Behavior.Type getType() {
    	return type;
    }

	public static FreeFloatBehavior make()
	{
		return instance;
	}
}
