package cc.comm;

import java.util.List;

import cc.event.Event;


/**
 * Client to Server connection by Reference
 * 
 * Sends/Receives events from the game server, it is
 * used when the host/app are on the same computer.
 * 
 * Uses the ServerDirectConnection for sending/receiving events.
 * @author Bj�rn
 *
 */

public class ClientDirectConnection implements Connection
{
	private ServerDirectConnection link;
	
	public ClientDirectConnection(ServerDirectConnection newLink)
	{
		link = newLink;
	}
	
	// TODO: Allow closing of connection :)
	public void close()
	{
		;
	}
	
	public List<Event> receive() {
		return link.getSent();
	}
	
	public void send(List<Event> events) {
		link.setReceived(events);
	}
	
	public void send(Event event)
	{
		link.setReceived(event);
	}
	
}
