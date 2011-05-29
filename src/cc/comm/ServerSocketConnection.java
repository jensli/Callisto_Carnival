package cc.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
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
		ArrayList<Event> rec = new ArrayList<Event>();
		String line;
		
		while ( ( line = inStream.readLine() ) != null ) {
			rec.add( Event.make( line ) );
		}
		
		return rec;
	}
	
	

	public void send(List<Event> events)
	{	
		for ( Event e : events ) {
			outStream.println( e.serialize() );
		}
	}
	
	public void send( Event event )
	{
		outStream.println( event.serialize() );
	}
	
	

}
