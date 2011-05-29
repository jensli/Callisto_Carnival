package cc.comm;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import cc.event.Event;


/**
 * Connection Interface
 * 
 * Provides a common interface for communication classes,
 * making it easier for the server to handle all clients
 * in the same manner.
 * 
 * Current connection types implemented:
 *   * Socket
 *   * By reference
 *   
 * @author Bj�rn
 *
 */

public interface Connection extends Closeable
{
	/**
	 * Send several events to the recipient
	 * @param List
	 */
	public abstract void send(List<Event> events);
	
	/**
	 * Send an event to the recipient
	 * @param Event
	 */
	public abstract void send(Event event);
	
	/**
	 * Receives incoming event from the remote host
	 * 
	 * Returns an empty List if no new events have arrived.
	 * 
	 * @return List<Event>
	 * @throws IOExceptione
	 */
	public abstract List<Event> receive() throws IOException;
	
	/**
	 * Closes the remote connection
	 * @throws IOException
	 */
	public abstract void close() throws IOException;
}
