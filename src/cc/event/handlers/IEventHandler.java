package cc.event.handlers;
import cc.event.Event;
public interface IEventHandler
{
	/**
	 * Send an event to other objects.
	 * @param event
	 *            is sent to every object registered as a receiver of that type of event
	 */
	public void postEvent( Event event );
	public void postEvents( Iterable<Event> events );
	public void addEventReceiver( EventReceiver receiver, Event.Cathegory type );
}
