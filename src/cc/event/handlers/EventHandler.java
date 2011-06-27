package cc.event.handlers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cc.event.Event;


/**
 * This singleton class is used to pass Event objects from one class in
 * the system to others. A class can register as a receiver of certain kinds of
 * events. An event is sent using the postEvent(...) method, the
 * EventHandler that calls the receiveEvent(...) method of all objects
 * registered as a receiver of that type of event, passing the event as
 * parameter.
 */
public class EventHandler implements IEventHandler
{
	private static IEventHandler instance = new EventHandler();
	private Map<Event.Cathegory, Set<EventReceiver>> receiverListMap
			= new HashMap<Event.Cathegory, Set<EventReceiver>>();

	private EventHandler()
	{
		for ( Event.Cathegory type : Event.Cathegory.values() ) {
			receiverListMap.put( type, new HashSet<EventReceiver>() );
		}
	}

	public static void reset() {
		instance = new EventHandler();
	}

	/**
	 * Send an event to other objects.
	 * @param event
	 *            is sent to every object registered as a receiver of that type of event
	 */
	public void postEvent( Event event )
	{
		if ( event == null ) {
			throw new NullPointerException( "Can not post null as a event." );
		}

		Collection<EventReceiver> receiverList = receiverListMap.get( event.getType() );

		for ( EventReceiver receiver : receiverList ) {
			receiver.receiveEvent( event );
		}
	}

	public void postEvents( Iterable<Event> events )
	{
		for ( Event e : events ) {
			postEvent( e );
		}
	}

	/**
	 * Returns the single instance of this class.
	 */
	public static IEventHandler get()
	{
		return instance;
	}

	public void addEventReceiver( EventReceiver receiver, Event.Cathegory type )
	{
		receiverListMap.get( type ).add( receiver );
	}

}
