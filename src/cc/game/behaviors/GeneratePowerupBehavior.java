package cc.game.behaviors;

import cc.event.game.CreateEvent;
import cc.event2.EventGlobals;
import cc.event2.EventGroups;
import cc.game.GameFactory;
import cc.game.GameObject;
import cc.game.PowerupType;
import cc.game.objects.Powerup;
import cc.util.Random;


/**
 * Execues action on random intervals, passing it the controlled
 * object. A freq of 0.1 makes this happend every few seconds.
 */
public class GeneratePowerupBehavior extends Behavior
{


	private double freq;
	private PowerupType powerupType;

	public GeneratePowerupBehavior( double freq, PowerupType powerupType )
	{
	    this.freq = freq;
	    this.powerupType = powerupType;
    }

	@Override
    public void perform( GameObject controlled, double dT )
	{
		if ( Random.getGameRandom().nextDouble()  <= dT*freq ) {
			Powerup p = GameFactory.get().createPowerup( controlled, powerupType );
			EventGlobals.getHandler().post( EventGroups.CREATE, new CreateEvent( p ) );
		}
    }


}
