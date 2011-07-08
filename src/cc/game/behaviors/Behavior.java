package cc.game.behaviors;

import cc.event.Event;
import cc.event.handlers.EventReceiver;
import cc.game.GameObject;
import cc.game.Simulation;

/**
 * A Behavior is added to a GameObject to make it act in a certain way.
 * A GameObject contains a list of Behavior objects. Each update the it loop
 * through the list calling the perform(double dT) method of each behavior. A Behavior
 * can receive events and modify its GameObject as a result of the event.
 */
public abstract class Behavior extends EventReceiver
{
	// TODO: UGLY, UGLY, REMOVE!!! This is added for convenience, you dont have to pass in refs to
	// objList anymore. But this has not been changed yet in the code of the behavs.
	private static Simulation simulation;

	private static final Behavior EMPTY_BEHAVIOR = new Behavior() {
		@Override public void perform( GameObject controlled, double dT )
		{}

		@Override public Behavior.Type getType() {
			return Type.EMPTY;
		}

		@Override public Behavior makeGroupWith( Behavior b ) {
			return b;
		}
	};

//	protected GameObject controlled;

	public enum Type {
		CONTROLLED,
		COLLISION,
		MOVEMENT,
		TIMER,
		DEFAULT,
		DOCKED,
		STATION,
		EMPTY,
	}

	private boolean finished = false;


	public static void setSimulation( Simulation newSimulation )
	{
		simulation = newSimulation;
	}
	protected static Simulation getSimulation()
	{
		return simulation;
	}

	public Behavior()
	{}

	@Override
    public void receiveEvent( Event event ) {}

	public abstract void perform(GameObject controlled, double dT);

	public boolean isFinished() {
		return finished;
	}
	public void setFinished( boolean finished ) {
		this.finished = finished;
	}

	public static Behavior getEmpty() {
		return EMPTY_BEHAVIOR;
	}

	public Behavior.Type getType()
	{
		return null;
	}

//	public void add( Behavior behavior ) {
//		throw new UnsupportedOperationException( "Trying to add behavior to non-group behavior" );
//	}

//	public boolean isGroup() {
//		return false;
//	}

	public Behavior makeGroupWith( Behavior b )
	{
		return new BehaviorGroup( this, b );
	}

//	public boolean isEmpty() {
//		return false;
//	}

	public boolean isEmpty()
	{
		return this == EMPTY_BEHAVIOR;
	}

	public static boolean isEmpty( Behavior b )
	{
		return b == EMPTY_BEHAVIOR;
	}

}
