package cc.game.collision;

import cc.game.GameObject;
import cc.game.behaviors.StationBehavior;
import cc.game.objects.Ship;
import cc.game.objects.SpaceStation;
import cc.util.math.Vec;


public class StationCollider extends DefaultCollider
{
	private StationBehavior stationBehavior;

//	@Override public void collideMe( GameObject me, GameObject other ) {
//		other.getCollider().collidePlanet( me, other );
//    }

	public StationCollider( StationBehavior stationBehavior )
	{
		this.stationBehavior = stationBehavior;
	}


	@Override
	public void collideActor( Ship other, GameObject me )
	{
		if ( stationBehavior.isDocked( other ) ) {
			return;
		}

		SpaceStation ss = ( SpaceStation ) me;

		Vec dVel = new Vec();
		dVel.setSub( me.getPhysModel().getVel(), other.getPhysModel().getVel() );

		if ( dVel.length() < stationBehavior.getDockSpeed() ) {
			stationBehavior.dock( other, ss );
		} else {
			this.collideDefault( other, me );
		}

    }




}
