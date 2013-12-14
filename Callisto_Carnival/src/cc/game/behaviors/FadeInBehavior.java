package cc.game.behaviors;

import j.util.util.Util;
import cc.game.GameObject;
import cc.gui.models.GraphicalModel;
import cc.util.Color;
import cc.util.math.Vec;

public class FadeInBehavior extends GraphicalModel
{
	private double fadeSpeed = 0.8;

//	public FadeInBehavior( double fadeSpeed )
//	{
//	    this.fadeSpeed = fadeSpeed;
//    }

	public FadeInBehavior( GameObject object, double speed ) {
		super( object );
		// TODO Auto-generated constructor stub
	}


	@Override public void draw( Vec pos, Vec forward ) {
	}

	@Override public void update( double dT ) {
		Color c =  getObject().getGraphicalModel().getColor();

		c.a += fadeSpeed*dT;

		c.a = (float) Util.clampHigh( c.a, 1.0 );

		if ( c.a == 1.0 ) {
			setFinished( true );
		}
	}

	// Has converted from Behavior to GraphicalModel, remove this now?
	// Should this become a decorator of GraphicalModel?
	//	@Override
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

