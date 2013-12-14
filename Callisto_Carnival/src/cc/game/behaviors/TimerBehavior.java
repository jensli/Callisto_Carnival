package cc.game.behaviors;

import j.util.functional.Action1;
import cc.event.Event;
import cc.event.game.KillEvent;
import cc.event2.EventGlobals;
import cc.event2.EventGroups;
import cc.game.GameObject;
import cc.game.objects.DeathFaderBehavior;


/**
 * Kills the object after some time.
 *
 */
public class TimerBehavior extends Behavior
{
	private double
		timeToLive = 0.0,
		initTime;

	private boolean isRepeating = false;

	private static Behavior.Type type = Behavior.Type.TIMER;

	private Action1<GameObject> action;

	public TimerBehavior( double newTimeToLive )
	{
		this( newTimeToLive, DEATH_ACTION );
	}

	public TimerBehavior( double newTimeToLive, Action1<GameObject> action )
	{
		timeToLive = newTimeToLive;
		initTime = newTimeToLive;
		this.action = action;
	}

	@Override
	public void receiveEvent( Event event )
	{}

	/**
	 * Sends a KillEvent when the time is up
	 */
	@Override
	public void perform( GameObject controlled, double dT )
	{
		timeToLive -= dT;

		if (timeToLive <= 0.0 ) {
			action.run( controlled );

			if ( isRepeating ) {
				timeToLive = initTime;
			} else {
				setFinished( true );
			}
		}
	}

	@Override
    public Behavior.Type getType() {
    	return type;
    }

	public static Action1<GameObject>
		DEATH_ACTION = new Action1<GameObject>() {
			public void run( GameObject arg ) {
				EventGlobals.getHandler().post( EventGroups.KILL,
						new KillEvent( arg.getID() ) );
			}
		};

//	private static class FADE_ACTION implements Action1<GameObject>
//	{
//		private double fadeSpeed;
//		private FADE_ACTION( double fadeSpeed ) {
//	        this.fadeSpeed = fadeSpeed;
//        }
//		public void run( GameObject arg ) {
//			arg.addExtraBehavior( new DeathFaderBehavior( fadeSpeed ) );
//		}
//	};


	public static TimerBehavior makeDeathFader( double time, final double fadeSpeed )
	{
		Action1<GameObject> action = new Action1<GameObject>() {
			public void run( GameObject arg ) {
				arg.addExtraBehavior( new DeathFaderBehavior( fadeSpeed ) );
			}
		};

		return new TimerBehavior( time, action );
	}
}
