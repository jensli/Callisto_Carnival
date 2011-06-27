package cc.game.collision;


import java.util.ArrayList;
import java.util.Collection;

import cc.game.GameObject;
import cc.game.objects.Ship;

public class BurningCollider extends DefaultCollider
{
	private Collection<GameObject> burningList = new ArrayList<GameObject>();

	private double
		realRadius,
		burnDamage = 0.3;

//	@Override
//    public void collideMe( GameObject me, GameObject other )
//    {
//
//    }

	public BurningCollider( double burnDamage, double realRadius )
    {
	    this.burnDamage = burnDamage;
	    this.realRadius = realRadius;
    }

	@Override
	public void collideDefault( GameObject other, GameObject me )
	{
		double distance = me.getPos().distance(
				other.getPos() );

		if ( distance <= other.getRadius() + realRadius ) {
			super.collideDefault( other, me );
		}
    }

	@Override
    public void collideActor( Ship other, GameObject me )
    {
		double distance = me.getPos().distance(
				other.getPos() );

		if ( distance > other.getRadius() + realRadius ) {

			// To giv burning targets to the particle system,
			// emptied every update by particle system.
			burningList.add( other );

			other.changeLife( -burnDamage *
					( me.getRadius()/distance - 1 ) );

    	} else {

    		this.collideDefault( other, me );
		}

    }


	public Collection<GameObject> getBurningList() {
    	return burningList;
    }


}


