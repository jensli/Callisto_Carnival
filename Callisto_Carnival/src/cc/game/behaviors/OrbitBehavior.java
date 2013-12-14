// Serialisation is NOT working!!!!!!

package cc.game.behaviors;

import cc.game.GameObject;
import cc.util.EllipseOrbit;
import cc.util.math.CyclicDouble;
import cc.util.math.Vec;

/**
 * Makes the GameObject revolve around another GameObject.
 */
public class OrbitBehavior extends Behavior
{
	private double
		speed;

	private CyclicDouble cyclePos;
	private GameObject motherObject;

	private EllipseOrbit orbit;

	private boolean isFaceMother;

	private static Behavior.Type type = Behavior.Type.MOVEMENT;

	/**
	 * Create a circular orbit
	 */
	public OrbitBehavior( GameObject mother, double revolvTime,
			double distance, double startingAngle )
	{
		this( mother, revolvTime, distance, distance, startingAngle, new Vec( 1, 0 ), false );
	}

	/**
	 * Create an elliptical orbit with perihelionDist as the long dist
	 * and alphelionDist as the short dist and dir as the orientation of
	 * the ellipsis (must be unit)
	 */
	public OrbitBehavior(
			GameObject mother,
			double revolvTime,
			double perihelionDist,
			double aphelionDist,
			double startingAngle,
			Vec dir,
			boolean isFaceMother )
	{
		orbit = new EllipseOrbit(
				mother.getPos(),
				dir,
				perihelionDist,
				aphelionDist );

		this.isFaceMother = isFaceMother;

		this.motherObject = mother;
		this.speed = 10/revolvTime;

		cyclePos = new CyclicDouble( startingAngle / (2*Math.PI), 1.0 );
	}

	@Override
    public void perform( GameObject controlled, double dT )
	{
		cyclePos.inc( speed*dT );

		orbit.setCenter( motherObject.getPos() );
//		Vec deltaV = orbit.calcVector( cyclePos.value() );

		orbit.setProgress( cyclePos.value() );

		Vec deltaV = controlled.getPhysModel().getVel();
		deltaV.set( orbit.getPos() );
		deltaV.sub( controlled.getPos() );
		deltaV.scale( 1/dT );

//		controlled.getPhysModel().setVelocity( deltaV );

		if ( isFaceMother ) {
			Vec newForward = controlled.getPhysModel().getForward();
			newForward.setSub( controlled.getPos(), motherObject.getPos() );
			newForward.normalize();

//			Vec newForward = VecMath.sub( controlled.getPos(), motherObject.getPos() );
//			newForward.normalize();
//			controlled.getPhysModel().setForward( newForward );
		}
	}

	@Override
    public Behavior.Type getType() {
    	return type;
    }

}
