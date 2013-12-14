package cc.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import cc.event.Event;


/**
 * Server to Client connection by Socket
 *
 * If you're hosting a game this class provides a method
 * for server-app communication over LAN.
 *
 * @author Bj�rn
 *
 */

public class ServerSocketConnection implements Connection
{
	private Socket client;
	private InputThread inStream;
	private OutputThread outStream;

	public ServerSocketConnection(Socket c, int id) throws IOException
	{
		this.client = c;

		BufferedReader in = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
		PrintWriter out = new PrintWriter( client.getOutputStream() );

		// A "Thread" decorator
		inStream = new InputThread( in );
		outStream = new OutputThread( out );
	}

	public void close() throws IOException
	{
		inStream.close();
		outStream.close();
		client.close();
	}


	public List<Event> receive() throws IOException
	{
		return CommUtil.lazyReadThread( inStream );
//		String line = inStream.readLine();
//		if ( line == null ) return Collections.emptyList();
//		String line2 = inStream.readLine();
//		if ( line2 == null ) return Collections.singletonList( Event.make( line ) );
//
//		List<Event> rec = new LinkedList<Event>();
//
//		rec.add( Event.make( line ) );
//		rec.add( Event.make( line2 ) );
//
//		while ( ( line = inStream.readLine() ) != null ) {
//			rec.add( Event.make( line ) );
//		}
//
//		return rec;
	}



	public void send(List<Event> events)
	{
		for ( Event e : events ) {
			send( e );
		}
	}

	public void send( Event event )
	{
		outStream.println( event.serialize() );
	}



}
