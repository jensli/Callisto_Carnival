package cc.comm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cc.event.Event;


/**
 * Server to Client by reference
 *
 * The ClientDirectConnection gets a reference to this class
 * passed when it's constructed, allowing data to flow up/downstream.
 *
 * This connection type is used when the Host and Client are on the same
 * computer. It allows for greater speed than a Socket connection would.
 *
 * @author Bj�rn
 *
 */

public class ServerDirectConnection implements Connection {

	List<Event> inBuf;
	List<Event> outBuf;
	int clientId;

	public ServerDirectConnection(int id)
	{
		inBuf =  Collections.synchronizedList( new ArrayList<Event>() );
		outBuf = Collections.synchronizedList( new ArrayList<Event>() );

		clientId = id;
	}

	//TODO: Implement
	public void close()
	{
		;
	}

	public List<Event> receive()
	{
		if ( inBuf.isEmpty() ) return Collections.emptyList();

		ArrayList<Event> rec = new ArrayList<Event>();

		synchronized(inBuf) {
			rec.addAll(inBuf);
			inBuf.clear();
		}

		return rec;
	}

	public void send(List<Event> events)
	{
		synchronized(outBuf) {
			outBuf.addAll(events);
		}
	}

	public void send(Event event)
	{
		synchronized(outBuf) {
			outBuf.add(event);
		}
	}

	/**
	 * Called from ClientDirectConnection,
	 *
	 * get the events that are in store.
	 *
	 * @return List<Event>
	 */
	protected List<Event> getSent()
	{
		if ( outBuf.isEmpty() ) {
			return Collections.emptyList();
		}

		List<Event> rec = new LinkedList<Event>();

		synchronized (outBuf) {
			rec.addAll(outBuf);
			outBuf.clear();
		}

		return rec;
	}

	/**
	 * Called for ClientDirectConnection,
	 *
	 * for sending events.
	 *
	 * @param List inData
	 */
	protected void setReceived(List<Event> inData)
	{
		synchronized (inBuf) {
			inBuf.addAll(inData);
		}
	}

	/**
	 * Called for ClientDirectConnection,
	 *
	 * for a single events.
	 *
	 * @param Event inData
	 */
	protected void setReceived(Event event)
	{
		synchronized(inBuf) {
			inBuf.add(event);
		}
	}



}
