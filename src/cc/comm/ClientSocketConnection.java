package cc.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import cc.event.Event;
import cc.util.logger.LogPlace;
import cc.util.logger.LogType;
import cc.util.logger.Logger;


/**
 * Client to Server connection by Socket
 *
 * Used if you're on a LAN connection and the
 * Host is on another computer.
 *
 * @author Bj�rn
 *
 */

public class ClientSocketConnection implements Connection
{
	private Socket socket;
	private InputThread inputThread;
	private OutputThread outputThread;
//	BufferedReader inStream;
//	PrintWriter outStream;

	public ClientSocketConnection() throws UnknownHostException, IOException
	{
		this( InetAddress.getLocalHost() );
//		Socket server = new Socket(InetAddress.getLocalHost(), Server.LISTEN_PORT);
//
//		inStream  = new InputThread(new BufferedReader(new InputStreamReader(server.getInputStream())));
//		outStream = new OutputThread(new PrintWriter(server.getOutputStream()));

//		inStream.start();
//		outStream.start();
	}

	public ClientSocketConnection( InetAddress addr ) throws UnknownHostException, IOException
	{
		Socket socket = new Socket( addr, ConnectionServer.LISTEN_PORT );
		socket.setTcpNoDelay( true );

		inputThread = new InputThread( new BufferedReader( new InputStreamReader( socket.getInputStream() ) ) );
		outputThread = new OutputThread( new PrintWriter( socket.getOutputStream() ) );
	}

	public void close() throws IOException
	{
		inputThread.close();
		outputThread.close();

		if ( socket != null ) {
			socket.close();
		} else {
			Logger.get().log( LogPlace.NET, LogType.WARNING, "In ClientSocketConnection.close : Socket is null." );
		}
	}

	public List<Event> receive() throws IOException
	{
		String line;
		List<Event> received = new LinkedList<Event>();

		while ( ( line = inputThread.readLine() ) != null ) {
			received.add( Event.make( line ) );
		}

		return received;
	}

	public void send(List<Event> events)
	{
		for ( Event e : events ) {
			send( e );
		}

		// Hey kids, don't forget to flush
//		outStream.flush();
	}

	public void send(Event event)
	{
		outputThread.println( event.serialize() );
	}
}
