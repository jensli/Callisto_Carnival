package cc.game.behaviors;

import cc.game.GameObject;

/**
 * Makes the SpaceObject float freely in space, affected by gravity.
 *
 */

public class FreeFloatBehavior extends Behavior
{
	private static Behavior.Type type = Behavior.Type.MOVEMENT;

	private static FreeFloatBehavior instance = new FreeFloatBehavior();
	/**
	 * Calculates gravity force exercised on this GameObject by all other
	 * GameObjects. Then accelerate the object with this force.
	 */
	@Override
    public void perform( GameObject controlled, double dT )
	{
		controlled.getPhysModel().accelerate( getSimulation().calcGravitation( controlled ) );
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
