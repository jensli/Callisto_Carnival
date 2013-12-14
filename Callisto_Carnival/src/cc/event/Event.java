package cc.event;

/**
 * This class stores the information the program use to communicate within its classes and server/clients.
 */

import j.util.eventhandler.GroupName;
import j.util.lists.Maps;
import j.util.util.Asserts;

import java.util.HashMap;

import cc.event.game.CreateEvent;
import cc.event.game.FireEvent;
import cc.event.game.KillEvent;
import cc.event.game.RotateEvent;
import cc.event.game.ThrustEvent;
import cc.event.handlers.EventReceiver;




public abstract class Event implements Cloneable
{
	public static final String TICK = "tick", ROTATE = "rot", THRUST = "thrust",
		KILL = "kill", FIRE = "fire", REQUEST_ACTION = "req", CREATE_OBJECT = "create",
		JOIN = "join", QUIT = "quit", REMOTE = "rem";


	// Identification of the event. Should be moved to a GameEvent subclass?
	private int receiverID = Event.NO_RECEIVER,
		senderID = Event.NO_SENDER;

	public static final int NO_SENDER = -1,
		NO_RECEIVER = -1;

	private static Object[][] eventNameMap =
		new Object[][] {
			{ Event.ROTATE, new RotateEvent() },
			{ Event.THRUST, new ThrustEvent() },
			{ Event.KILL, new KillEvent() },
			{ Event.CREATE_OBJECT, new CreateEvent( NO_RECEIVER ) },
			{ Event.FIRE, new FireEvent( NO_RECEIVER ) },
			{ Event.TICK, new TickEvent() },
			{ Event.JOIN, new JoinEvent( NO_RECEIVER, "no nick", false, false) },
			{ Event.QUIT, new QuitEvent() },
		};

	private static HashMap<String, Event> eventMap;

	static {
		// Initializing of the static eventMap
		eventMap = Maps.make( eventNameMap );
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
	 * @param eventData Event parameters. (event_name param_1 ... param_n)
	 * @return newEvent The Event that has been called and everything worked.
	 */
	public static Event make( String eventData )
	{

		String[] param = eventData.split( " " );

		Asserts.arg( param.length != 0, "Illegal event string" );

		Event newEvent = eventMap.get( param[0] );

		Asserts.notNull( newEvent, "Trying to deserialise unknown Event." );

		newEvent = newEvent.clone();

		newEvent.setFields( param );

//		newEvent.deserialize( eventData );

		return newEvent;
	}

	public void fromString( String data ) {
		String resultArr[] = data.split( " " );
		setFields( resultArr );
	}

	protected int setFields( String[] data ) {
		int i = 1;
		receiverID = Integer.parseInt( data[ i++ ] );
		senderID = Integer.parseInt( data[ i++ ] );
		return i;
	}


	/**
	 * This transforms the event to a string.
	 * @return The event as a string.
	 */
	public String serialize()
	{
		StringBuilder b = new StringBuilder();
		toStringBuilder( b );
		return b.toString();
	}

	public void toStringBuilder( StringBuilder b ) {
		b.append( getName() )
			.append( " " )
			.append( getReceiverId() )
			.append( " " )
			.append( getSenderId() );
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

	public long getSenderId() {
		return senderID;
	}

	public void setSenderID( int senderID ) {
		this.senderID = senderID;
	}

	public void setReceiverID( int receiverID ) {
		this.receiverID = receiverID;
	}

	public int getReceiverId() {
		return receiverID;
	}

	public String getName() {
		throw new UnsupportedOperationException( "Trying to get receiver group from event type that has non set" );
	}

	public GroupName getReceiverGroup() {
		throw new UnsupportedOperationException( "Trying to get receiver group from event type that has non set" );
	}

}







