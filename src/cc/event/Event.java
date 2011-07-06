package cc.event;

/**
 * This class stores the information the program use to communicate within its classes and server/clients.
 */

import j.util.eventhandler.GroupName;

import java.util.HashMap;

import cc.event.game.CreateEvent;
import cc.event.game.FireEvent;
import cc.event.game.KillEvent;
import cc.event.game.RotateEvent;
import cc.event.game.ThrustEvent;
import cc.event.handlers.EventReceiver;




public abstract class Event implements Cloneable
{
	public static final String TICK = "tick", ROTATE = "rotation", THRUST = "thrust",
		KILL = "kill", FIRE = "fire", REQUEST_ACTION = "req", CREATE_OBJECT = "create",
		JOIN = "join", QUIT = "quit", REMOTE = "rem";

	public enum Cathegory {
		GAME, NETWORK, GUI, REQUEST, APPLICATION
	}

	public static final boolean
		SWITCH_ON = true,
		SWITCH_OFF = false;

	// Identification of the event. Should be moved to a GameEvent subclass?
	private int receiverID = Event.NO_RECEIVER,
		senderID = Event.NO_SENDER;

	public static final int NO_SENDER = -1,
		NO_RECEIVER = -1;

	private String name;

	private static HashMap<String, Event> eventMap = new HashMap<String, Event>();

	static {
		// Initializing of the static eventMap
		eventMap.put( Event.ROTATE, new RotateEvent() );
		eventMap.put( Event.THRUST, new ThrustEvent() );
		eventMap.put( Event.KILL, new KillEvent() );
		eventMap.put( Event.CREATE_OBJECT, new CreateEvent( NO_RECEIVER ) );
		eventMap.put( Event.FIRE, new FireEvent( NO_RECEIVER ) );
		eventMap.put( Event.TICK, new TickEvent( 0.0 ) );
		eventMap.put( Event.JOIN, new JoinEvent( NO_RECEIVER, "no nick", false, false) );
		eventMap.put( Event.QUIT, new QuitEvent() );
	}

	/**
	 * Each subclass overrides this to call the corresponding receive method
	 * on the receiver. This method is called from receiver.receiveEvent( Event ),
	 * and is the way that the receiver identifies the event.
	 * @param receiver the receiver of the event
	 */
	public abstract void dispatch( EventReceiver receiver );

	public Event() {}

	public Event( int newReceiverID ) {
		receiverID = newReceiverID;
	}

	/**
	 * This is called when a new event is created from a serialized string.
	 * Uses desirialize() internally.
	 * @param parameters Event parameters. (event_name param_1 ... param_n)
	 * @return newEvent The Event that has been called and everything worked.
	 */
	public static Event make( String parameters )
	{

		String[] param = parameters.split( " ", 2 );
		Event newEvent = eventMap.get( param[0] );

		if ( newEvent == null ) {
			throw new RuntimeException( "Trying to deserialise unknown Event." );
		}

		newEvent = newEvent.clone();

		newEvent.deserialize( parameters );

		return newEvent;
	}

	/**
	 * This sets an event from a given event string.
	 * @param parameters A string with the events name, receiverID and other parameters
	 *            		 separated with a space.
	 */
	public void deserialize( String parameters )
	{
		String resultArr[] = parameters.split( " " );
		name = resultArr[0];
		receiverID = Integer.valueOf( resultArr[1] );//.intValue();
		senderID = Integer.valueOf( resultArr[2] );
	}



	/**
	 * This transforms the event to a string.
	 * @return The event as a string.
	 */
	public String serialize()
	{
		return getName() + " " + getReceiverID() + " " + getSenderID();
	}

	/**
	 * Clone current Event. For this operation to be successful this method
	 * needs to be overridden in any related sub-classes.
	 */
	@Override
	public Event clone()
	{
		try {
	        return ( Event ) super.clone();
        } catch ( CloneNotSupportedException e ) {
	        throw new RuntimeException( e + " while trying to clone event.", e );
        }
	}

	public long getSenderID() {
		return senderID;
	}

	public void setSenderID( int senderID ) {
		this.senderID = senderID;
	}

	public void setReceiverID( int receiverID ) {
		this.receiverID = receiverID;
	}

	public int getReceiverID() {
		return receiverID;
	}

	public String getName() {
		return name;
	}

	public GroupName getReceiverGroup() {
		return null;
	}


	public GroupName getRecveiverGroup() {
		throw new UnsupportedOperationException( "Trying to get receiver group from event type that has no set" );
	}

	public void setName( String newName ) {
		name = newName;
	}
}







