package cc.game.behaviors;

import cc.event.Event;
import cc.event.game.FireEvent;
import cc.event.game.ThrustEvent;
import cc.game.GameObject;
import cc.game.objects.Ship;
import cc.game.objects.SpaceStation;
import cc.game.weapon.Weapon;
import cc.util.math.Vec;

/**
 * The behavior put on a ship wich is docked at a space station. Moves the ship with
 * the station and heals and rearms it. Listens to FireEvent, then it releases the ship.
 *
 *  Extents ControlledBehavior to make it fit in the ships field for ControlledBehavior.
 *
 *  Put on the ship by the StationBehavior on a SpaceStation.
 */

public class DockedBehavior extends ControlledBehavior
{
//	private static Behavior.Type type = Behavior.Type.DOCKED;

	// Replaced by this behavior, put back when the ship undocks
	private ControlledBehavior oldControlledBehavior;

	// Other behavior the ship might have. Put back when ship undocks.
	private BehaviorGroup bg;

	// Put back when ship undocks.
	private Behavior oldMovementBehavior;

	private double healRate = 10,
		refuelRate = 0.1,
		rearmRate = 0.1;

	// Station the ship is docked to.
	private SpaceStation station;
	private final Vec dPos = new Vec(),
		oldForward = new Vec();

	private double stationStartAngle;

	private State state = null;

	public DockedBehavior()
	{
	}

	public void dock( Ship ship, SpaceStation station )
	{
		this.station = station;

		dPos.setSub( ship.getPos(), station.getPos() );

		oldForward.set( ship.getForward() );
		stationStartAngle = station.getForward().angle();
		oldControlledBehavior = ship.getControlledBehavior();
		oldMovementBehavior = ship.getMovementBehavior();

		ship.getPhysModel().setVelocity( new Vec( 0, 0 ) );
		ship.receiveEvent( new ThrustEvent( false ) );
		ship.getPhysModel().setRotation( 0.0 );

		ship.setControlledBehavior( this );
		bg = ship.getExtraBehaviors();

		setState( new Docked() );
	}

	private void setState( State state ) {
		this.state = state;
	}

	@Override
	public void perform( GameObject controlled, double dT ) {
		state.perform( controlled, dT );
	}
	@Override
	public void receiveEvent( Event event ) {
		event.dispatch( this );
	}

	@Override
	public void receiveFireEvent( FireEvent event ) {
		this.setState( new UndockRequested() );
	}

	private abstract class State {
		public abstract void perform( GameObject controlled, double dT );
	}

	private class Docked extends State
	{
		@Override
		public void perform( GameObject controlled, double dT )
		{
			// Move and rotate ship after station
			final double rot = station.getPhysModel().getForward().angle() - stationStartAngle;
			Vec v = dPos.copy();
			v.rotate( rot );

			v.add( station.getPhysModel().getPos() );
			controlled.getPhysModel().setPos( v );
			controlled.getPhysModel().getVel().set( 0, 0 );

			v.set( oldForward );
			v.rotateNormalized( rot );
			controlled.getPhysModel().setForward( v );

			// Heal, refuel and rearm
			controlled.changeLifeClamped( dT*healRate );

			oldControlledBehavior.getWeapon().changeAmmo(
					rearmRate*oldControlledBehavior.getWeapon().getMaxAmmo()*dT );

			oldControlledBehavior.changeFuelLevel(
					refuelRate*oldControlledBehavior.getMaxFuel()*dT );
        }
	}

	private class UndockRequested extends State
	{
		@Override
		public void perform( GameObject controlled, double dT )
		{
			Vec push = controlled.getPos().copy();
			push.sub( station.getPos() );
			push.normalize();
			push.scale( 200 );
			push.add( station.getPhysModel().getVel() );
			controlled.getPhysModel().setVelocity( push );
			controlled.getPhysModel().addRotatio( 10.0 );


			// TODO: Ugly, Ugly cast
			((Ship )controlled).setExtraBehaviors( bg );
			controlled.setMovementBehavior( oldMovementBehavior );

			setState( new Undocking() );
		}
	}

	private class Undocking extends State {
		@Override
		public void perform( GameObject controlled, double dT )
		{
			Vec dPos = controlled.getPos().copy();
			dPos.sub( station.getPos() );

			final double collisionDist = (controlled.getPhysModel().getRadius() + station.getPhysModel().getRadius() )*1.2;

			if ( dPos.length() > collisionDist) {
				setFinished( true );
				((Ship) controlled).setControlledBehavior( oldControlledBehavior );
				station.getStationBehavior().undock( controlled );
			}

		}
	}

	@Override
    public double getFuelLevel() {
	    return oldControlledBehavior.getFuelLevel();
    }
	@Override
    public double getMaxFuel() {
	    return oldControlledBehavior.getMaxFuel();
    }
	@Override
    public Weapon getWeapon() {
	    return oldControlledBehavior.getWeapon();
    }




}
