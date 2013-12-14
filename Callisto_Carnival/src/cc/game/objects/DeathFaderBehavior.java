package cc.game.objects;

import cc.event.game.KillEvent;
import cc.event2.EventGlobals;
import cc.event2.EventGroups;
import cc.game.GameObject;
import cc.game.behaviors.Behavior;
import cc.util.Color;

public class DeathFaderBehavior extends Behavior
{
	private double fadeSpeed = 0.02;

	public DeathFaderBehavior( double fadeSpeed ) {
	    this.fadeSpeed = fadeSpeed;
    }

	@Override
	public void perform( GameObject controlled, double dT )
	{
		Color c = controlled.getGraphicalModel().getColor();

		c.a -= fadeSpeed*dT;

		if ( c.a <= 0.0 ) {
			EventGlobals.getHandler().post( EventGroups.KILL, new KillEvent( controlled.getID() ) );
		}
	}
}
