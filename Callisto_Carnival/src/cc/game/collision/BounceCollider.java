package cc.game.collision;

import cc.game.GameObject;
import cc.game.objects.Ship;
import cc.util.math.Vec;

/**
 * decreses life of the collidee by damage and pushes it away
 */
public class BounceCollider extends DefaultCollider
{
	private static boolean rotationDir = true;

	private double
		rotationStrength = 20.0,
		pushStrength = 0.3;

	public BounceCollider( double damage )
    {
	    super( damage );
    }

//	@Override
//    public void collideMe( GameObject me, GameObject other )
//	{
//		other.getCollider().collideDefault( me, other );
//	}

	@Override
    public void collideActor( Ship other, GameObject me )
    {
		other.changeLife( -this.getDamage() );

		Vec punch = me.getPhysModel().getVel();
		punch.sub( other.getPhysModel().getVel() );
		punch.scale( pushStrength );

		other.getPhysModel().addVelocity( punch );

		other.getPhysModel().setRotation( rotationDir ? rotationStrength : -rotationStrength );
    }

}
