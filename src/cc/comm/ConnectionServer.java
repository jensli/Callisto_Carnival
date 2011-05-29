package cc.comm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cc.event.Event;
import cc.util.logger.LogPlace;
import cc.util.logger.LogType;
import cc.util.logger.Logger;


/**
 * The Server Class,
 * 
 * Oh hai, I'm responsible for attending to connecting clients,
 * making sure they're feeling alright and stuff. Sometimes I give the app
 * unique id:s so I can keep track of them all.
 * 
 * If I'd want to know what my clients are up to, this is where i'd look. 
 * You can send/recieve data to all clients simultaneously (no it's not multicasting, even though that'd be cool)
 * 
 * @author Bj?rn
 */

//TODO Make sure thread is properly killed on garbagecollect

public class ConnectionServer
{
	public static final int LISTEN_PORT = 30010;
	
	private List<Connection> clients;
	private ServerSocket serverSocket;
	private Thread listenThread;
	private int clientId;
	private volatile boolean listening = false; // Temporary solution for the "close thread"-problem
	
	public Runnable loopRunner = new Runnable() {
		public void run()
		{
			Socket newClientSocket;
            ServerSocketConnection ssc;
            int clientId;
            Logger.get().log( LogPlace.NET, "Server: Listening for incoming connections on port: " + LISTEN_PORT );
            
            while ( listening ) {
            	try {
            		// If the thread is interrupted while listening it will
            		// wait unitl a new app connects
            		// and drop the new app connection
            		newClientSocket = serverSocket.accept();
            		
            		newClientSocket.setTcpNoDelay( true );
            		
            		clientId = getUniqueId();
            		ssc = new ServerSocketConnection( newClientSocket, clientId );
            		
            		// Drop the app if listener is closing down
            		synchronized ( clients ) {
            			clients.add( ssc );
            		}
            		
            		Logger.get().log( LogPlace.NET, "Server: A new app has connected through socket (id: " + clientId + ")" );
            		
            	} catch ( SocketTimeoutException e ) {
            		; // Timeout to check the listening variable if we
            		  // should continue listening or begin the game
            	} catch ( IOException e ) {
            		Logger.get().log( LogPlace.NET, LogType.WARNING, "IO error while listening for new connections." + e );
            	}
            }
            
            // Close socket, good bye!
            try {
            	Logger.get().log( LogPlace.NET, "Closing server" );
            	serverSocket.close();
            } catch ( IOException e ) {
            	Logger.get().log( LogPlace.NET, LogType.WARNING, "IO error when closing server." + e );
            }
            
            Logger.get().log( LogPlace.NET, "ServerSocket closing down ..." );
		}
	};
	
	public ConnectionServer( boolean delayStart ) throws IOException
	{
		clientId = 0;
		clients = Collections.synchronizedList( new ArrayList<Connection>() );
		serverSocket = new ServerSocket( ConnectionServer.LISTEN_PORT );
		
		serverSocket.setSoTimeout( 500 ); // Added by Jens
		
//		Util.runInNewThread( "Server", loopRunner );
//		initListenThread();
		
		listenThread = new Thread( loopRunner, "Server" );
		
		if ( !delayStart ) {
			startListening();
		}
	}
	
	public ConnectionServer() throws IOException
	{
		this(false);
	}
	
	/**
	 * Start listening for incoming connections
	 */
	public void startListening()
	{
		listening = true;
		listenThread.start();
	}
	
	/**
	 * Stop listening for incoming connections
	 */
	public void stopListening()
	{
		// listenThread.stop();
		// No longer used, causes undefined behaviour according to Sun's Java ref. doc.
		// In a perfect world, Thread.interrupt() would be used, but it does not
		// interrupt blocking calls such as reading/writing from IO,
		// hence this ugly solution:
		listening = false;
	}
	
	/**
	 * Returns true if the server is set to listen for incoming connections
	 * 
	 * NOTE: The listen-thread can be running even if this method returns false,
	 * it takes a while for the thread to close down.
	 */
	public boolean isListening()
	{
		return listening;
	}
	
	/**
	 * Returns an unique Id for app identification
	 */
	synchronized private int getUniqueId()
	{
		return clientId++;
	}
	
	/**
	 * Returns all events that the connected clients have sent to the server.
	 */
	public List<Event> receive()
	{
		ArrayList<Event> received = new ArrayList<Event>();
		
		synchronized(clients) {
			for (Connection client : clients) {
				try {
					received.addAll( client.receive() );
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return received;
	}
	
	/**
	 * Send a List of events to the connected clients
	 * 
	 * NOTE: The data passed to this function will be sent out to all connected clients.
	 */
	public void send(List<Event> events)
	{
		synchronized(clients) {
			for (Connection client:clients) {
				//System.out.println("Server: Sending events to app");
				client.send(events);
			}
		}
	}
	
	/**
	 * Send an event to the connected clients
	 * 
	 * NOTE: The data passed to this function will be sent out to all connected clients.
	 * @param events
	 */	
	public void send(Event event)
	{
		synchronized (clients) {
			for (Connection client : clients) {
				//System.out.println("Server: Sending event to app");
				client.send(event);
			}
		}
	}
	
	/**
	 * Get a "Client to Server by reference" object,
	 * 
	 * Used when the host is on the same machine as the app.
	 */
	public ClientDirectConnection getDirectConnection()
	{
		ServerDirectConnection serverToClient = new ServerDirectConnection( getUniqueId() );
		ClientDirectConnection clientToServer = new ClientDirectConnection( serverToClient );
		
		synchronized ( clients ) {
			clients.add(serverToClient);
		}
		
		return clientToServer;
	}

	
	public void closeClients() throws IOException
	{
		for ( Connection c : clients ) {
			c.close();
		}
	}
}
