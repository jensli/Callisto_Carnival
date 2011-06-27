package cc.game.behaviors;

import cc.game.GameObject;
import cc.util.Color;
import cc.util.Util;

public class FadeInBehavior extends Behavior
{
	private double fadeSpeed = 0.02;

	public FadeInBehavior( double fadeSpeed )
	{
	    this.fadeSpeed = fadeSpeed;
    }

	@Override
	public void perform( GameObject controlled, double dT )
	{
		Color c = controlled.getGraphicalModel().getColor();

		c.a += fadeSpeed*dT;

		c.a = (float) Util.clampHigh( c.a, 1.0 );

		if ( c.a == 1.0 ) {
			setFinished( true );
		}
	}
}

