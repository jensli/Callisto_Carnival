package cc.app;


import cc.comm.ConnectionServer;
import cc.event.TickEvent;
import cc.util.Timer;



/**
 * An HostGame object is running on the host computer only, that means one
 * of the computers in a multiplayer game or a computer running a single
 * player game.
 *
 * This is how syncronisatoin between differient multiplayer games is done, that all
 * events that will cause game actions first is sent here and then out to all games
 * at the same time (the game that is the source of event too)
 *
 * It is responsible for receiveing events from all of the client games and sending
 * events from every client to all other clients. When its time to update the games it
 * sends a TickEvent to all of them.
 *
 * Sending events is done with the Server object, which contains a list of connections
 * to client games, either in the same program or remote via a TCP connection.
 *
 * @author Jens
 */

public class ServerApp
{
	private ConnectionServer serverConnection;

	// fast looping timing
//	private double timeConst =  0.0000009;
//	private static final double timeConst =  0.009;

	// For multiplayer, slow looping
	private int tickIntervalMicros = 20000;
	private double deltaTimeToSend = 0.01;


//	private static final double timeConst =  0.0000002;
//	private static final double deltaTimeToSend = tickIntervalMicros * timeConst;
//	private double deltaTimeToSend = tickIntervalMicros*timeConst;
//	private double deltaTimeToSend = 0.03;

	// For singelplayer, fast loop, vync timing
//	private static int tickIntervalMicros = 1;
//	private double deltaTimeToSend = 0.01;


	private Timer timer = new Timer( tickIntervalMicros );

	private boolean gameRunning = false;

	private final TickEvent tickEvent;

	/**
	 *
	 * @param server
	 *            the {@link ConnectionServer} object HostGame will use to send and
	 *            receive events.
	 */
	public ServerApp( ConnectionServer server )
	{
		this.serverConnection = server;

//		TickEvent.setDt( deltaTimeToSend );
		tickEvent = new TickEvent();

		if ( gameRunning ) {
			timer.start();
		}
	}


	/**
	 * Called each program loop, receives events sent by clients and resends
	 * them to all clients. Sends a TickEvent to all clients at an
	 * interval.
	 */
	public void update()
	{
		if ( timer.retrieveTick() ) {
			// TODO: Should send the events immediatly, only wait with the tick.
			serverConnection.send( serverConnection.receive() );
//			serverConnection.send( new TickEvent( deltaTimeToSend ) );
			serverConnection.send( tickEvent );
		}
	}

	/**
	 * Set the ServerApp to be in a running state.
	 */
	public void setGameRunning( boolean newGameRunning )
	{
		if ( newGameRunning && !gameRunning ) {
			timer.start();
		} else if ( !newGameRunning && gameRunning ) {
			timer.stop();
		}

		gameRunning = newGameRunning;
	}

	public boolean isGameRunning() {
    	return gameRunning;
    }


//	public int getTickInterval()
//    {
//    	return tickIntervalMillis;
//    }

}
