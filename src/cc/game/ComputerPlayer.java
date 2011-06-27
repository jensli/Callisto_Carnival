package cc.game;



import java.util.Collection;

import cc.event.game.FireEvent;
import cc.event.game.RotateEvent;
import cc.event.game.ThrustEvent;
import cc.util.math.Vec;


/**
 * The game's computer controlled player. This contains the
 * basic rules of which the computer's follows in space.
 * Each AI player try to avoid great gravitation fields and
 * is attracted to small gravitation fields.
 *
 */
public class ComputerPlayer implements GameDeamon
{

	private final double
		steerPower = 0.015,
		thrustPower = 5.0,
		angleDiff = 0.2,

		maxVelosity = 500,
		medVelosity = 300,
		lowVelosity = 100,

		lowGrav = 50,
		highGrav = 100,

		maxTargetRange = 2000,

		inaccuracyAdd = 0.1,
		inaccuracyMul = 0.1;

	private final int
		navMode = 0,
		breakMode = 1,
		termMode = 2;

	private int mode = navMode;

	private GameObject target = null;
	private double inaccuracy = 0;

	Collection<Player> players;

	private Player player;

	protected Simulation simulation;

	private boolean rotateOn = false,
		thrustOn = false,
		fireOn = false;

	private boolean willFire = false,
		willRotate = false,
		willThrust = false,
		rotateClockwise = false;


	public ComputerPlayer(Simulation simulation, Collection<Player> players )
	{
		super( );
		this.simulation = simulation;
		this.players = players;
	}



	public ComputerPlayer( Simulation simulation, Collection<Player> players, Player player )
    {
	    this.players = players;
	    this.simulation = simulation;
	    this.player = player;
    }



	public void update( Simulation sim, double dT )
	{
		this.simulation = sim;
		this.update( dT );
    }


	public void update( double dT )
	{
//		super.update( dT );

		if ( ! player.hasLivingObject() ) {
			return;
		}

		this.runAI();

//		EventHandler handler = EventHandler.getInstance();

		// Hack, hack, hack. Detta blev inte vackert...
		if ( willThrust != thrustOn )	{
			player.routeToObject( new ThrustEvent( player.getControlledID(), willThrust ) );
			thrustOn = willFire;
		}
		if ( willRotate != rotateOn )	{
			player.routeToObject( new RotateEvent(	player.getControlledID(),
					rotateClockwise, willRotate ) );
			rotateOn = willRotate;
		}

		if ( willFire != fireOn ) {
			player.routeToObject( new FireEvent( player.getControlledID(), willFire ) );
			fireOn = willFire;
		}
//		if ( willThrust != thrustOn )	{
//			handler.postEvent( new ThrustEvent( getControlledID(), willThrust ) );
//			thrustOn = willFire;
//		}
//		if ( willRotate != rotateOn )	{
//			handler.postEvent( new RotateEvent(	getControlledID(), rotateClockwise, willRotate ) );
//			rotateOn = willRotate;
//		}
//
//		if ( willFire != fireOn ) {
//			handler.postEvent( new FireEvent(getControlledID(), willFire ) );
//			fireOn = willFire;
//		}

		willThrust = false;
		willRotate = false;
		willFire = false;
	}


	private void runAI()
	{
		GameObject obj = simulation.getObject( player.getControlledID());
		if ( obj == null ) {
			return;
		}

		Vec g = getGravitation(obj);
		if ( g == null ) {
			throw new RuntimeException("g = null!");
		}

		switch ( mode ) {
			case navMode: navigateAI( obj, g );
				break;
			case breakMode: breakAI( obj, g );
				break;
			case termMode: terminateAI( obj, g );
				break;
		}
	}


	private void terminateAI(GameObject me, Vec g)
	{
		if ( target == null ) setMode(navMode);
		if ( !this.isTarget( me, target ) ) setMode(navMode);
        if ( g.length() < lowGrav ) setMode(navMode);
        if ( g.length() > highGrav ) setMode(navMode);

        if ( mode == navMode ) {
        	return;
        }

        Vec intV = this.getInterceptVector( me, target );
        Vec targetV = this.getTargetVector( intV, me, target );

        double fromAngle = this.getAngle(me.getPhysModel().getForward());
        double toAngle = this.normalizeAngle(getAngle(targetV) + Math.sin(inaccuracy) * inaccuracyMul );

        //System.out.println( "from: "+ toDegres( fromAngle ) + "to: "+ toDegres( toAngle ) );

        if ( this.turnTo(fromAngle, toAngle) ) {
        	willFire = true;
        	//System.out.println( "Fire!" );
        	inaccuracy += inaccuracyAdd;
        }

	}


	private void breakAI(GameObject obj, Vec g)
	{

		double dirAngle = this.getAngle( obj.getPhysModel().getForward() );
		double velAngle = this.getAngle( obj.getPhysModel().getVel() );
		velAngle = this.normalizeAngle( velAngle - Math.PI );

		if ( getShortestAngleDistance( dirAngle, velAngle ) < angleDiff ) {
			willThrust = true;
		} else {
			willRotate = true;
			rotateClockwise = this.isShortWayClockwise( dirAngle, velAngle );
		}
		if ( obj.getPhysModel().getVel().length() < medVelosity ) {
			setMode(navMode);
		}
        if ( g.length() > highGrav ) {
        	setMode(navMode);
        }
	}


	private GameObject findTarget( GameObject me )
	{
		for ( Player p : players ) {
			GameObject po = simulation.getObject(p.getControlledID());
			if ( po == null ) break;

			if ( isTarget( me, po ) ) return po;
		}

		return null;
	}


	private Vec getInterceptVector( GameObject from, GameObject to )
	{
		Vec toV = to.getPhysModel().getPos();
		Vec fromV = from.getPhysModel().getPos();

		Vec res = new Vec( toV );
		res.sub( fromV );
		return res;
	}


	private Vec getTargetVector( Vec interceptVector, GameObject me, GameObject target )
	{
		double speed = target.getPhysModel().getVel().length();
		double distance = interceptVector.length();

		Vec ret = new Vec(interceptVector);

		Vec add = new Vec(target.getPhysModel().getVel());
		add.scale(speed/distance);
		ret.add( add );

		speed = me.getPhysModel().getVel().length();
		add.set(me.getPhysModel().getVel());
		add.scale(speed/distance);
		add.negate();
		ret.add(add);

		return ret;
	}


	private boolean isTarget( GameObject me, GameObject target )
	{
		// Check if me.
		if ( me.getPhysModel().getPos().equals(target.getPhysModel().getPos()) ) {
			return false;
		}

		// See if alive.
		if ( !target.isAlive() ) {
			return false;
		}

		// Make sure not out of range.
		Vec intV = this.getInterceptVector(target, me);
		boolean ret = intV.length() < maxTargetRange;

		//System.out.println( "Length: " + intV.length() + " res: " + Boolean.toString(ret) );
		return ret;
	}

	private void navigateAI(GameObject obj, Vec g)
	{

		if ( (g.length() > lowGrav) && (g.length() < highGrav) ) {
			target = this.findTarget( obj );
			if ( target != null ) setMode( termMode );
		}

		double gAngle = this.getAngle( g );

		double newAngle = steerPower*g.length();

		if ( newAngle < 0 ) {
			newAngle = 0;
		} else if ( newAngle > Math.PI ) {
			newAngle = Math.PI;
		}

		newAngle += gAngle;
		newAngle = this.normalizeAngle(newAngle);

		double oldAngle = this.getAngle( obj.getPhysModel().getForward() );
		rotateClockwise = this.isShortWayClockwise( oldAngle, newAngle );

		if ( this.turnTo( oldAngle, newAngle ) ) {
			willThrust = true;
		}

		double velocity = thrustPower*Math.abs(g.length() - 80);
		if ( velocity < lowVelosity ) {
			velocity = lowVelosity;
		}


		if ( (obj.getPhysModel().getVel().length() > maxVelosity) && (g.length() < lowGrav ) ) {
			setMode(breakMode);
		}

		//System.out.println( "ga:"+toDegres(gAngle)+ " na:" +toDegres(newAngle) );
		//System.out.println( "g:"+g.length() );
	}


	private boolean turnTo( double currentAngle, double newAngle )
	{
		rotateClockwise = this.isShortWayClockwise( currentAngle, newAngle );

		if ( this.getShortestAngleDistance( currentAngle, newAngle ) > angleDiff ) {
			willRotate = true;
			return false;
		} else {
			return true;
		}
	}

	private void setMode( int mode )
	{
//		String modeName = "Unknown"; // AI Debug code
//		if ( mode == navMode ) modeName = "navigation";
//		else if ( mode == breakMode ) modeName = "break";
//		else if ( mode == termMode ) modeName = "terminate";
//		System.out.println( "AI: Going into " + modeName + " mode." );

		this.mode = mode;
	}

	private Vec getGravitation( GameObject obj )
	{
		// This makes it calculate gravitation twise, but what the hell!
		return simulation.calcGravitation( obj );
	}

	private double getAngle( Vec vec )
	{
		double angle = vec.angle( new Vec( 1.0, 0.0 ) );
		if ( vec.y < 0.0 ) {
			angle = Math.PI*2.0 - angle;
		}
		return normalizeAngle(angle);
	}

	private double normalizeAngle( double angle )
	{
		while ( angle < 0 ) angle += Math.PI*2.0;
		return angle;
	}


	private double getShortestAngleDistance( double angle1, double angle2 )
	{
		double way0 = Math.abs(angle1 - angle2);
		double way1 = 2*Math.PI - way0;
		if ( way0 < way1 ) return way0;
		else return way1;
	}


	private boolean isShortWayClockwise( double fromAngle, double toAngle )
	{

		double way0 = Math.abs(fromAngle - toAngle);
		double way1 = 2*Math.PI - way0;
		//System.out.println( "isShortWayClockwise from:"+toDegres(fromAngle)+ " to:" +toDegres(toAngle) + " way0: " + toDegres(way0) + " way1: " + toDegres(way1) );
		if ( fromAngle > toAngle ) {
			if ( way0 < way1 ) return false;
			else return true;
		} else {
			if ( way0 > way1 ) return false;
			else return true;
		}
	}
}
