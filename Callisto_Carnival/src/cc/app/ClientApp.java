//
// DONE: Fix minimap bounderys
// DONE: Explosion
// DONE: Flame
// DONE: Fuel, ammo
// DONE: Space station
// DONE: Comet
// DONE: Powerup

// TODO: Showing messages during game
// TODO: Work on smoothness
// TODO: Mission system
// TODO: Fansier gui components
// TODO: Asteroids
// TODO: Jupiter
//
// TODO: Would be nice if the program ran even if all textures where not there.

package cc.app;



import j.util.eventhandler.EventHandler;
import j.util.eventhandler.NoArgReceiver;
import j.util.eventhandler.Receiver;
import j.util.eventhandler.Sub;
import j.util.util.Asserts;
import j.util.util.Disposable;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import cc.comm.ClientSocketConnection;
import cc.comm.Connection;
import cc.comm.Host;
import cc.event.Event;
import cc.event.JoinEvent;
import cc.event.QuitEvent;
import cc.event.TickEvent;
import cc.event.ValueEvent;
import cc.event2.EventGlobals;
import cc.event2.EventGroups;
import cc.game.GameEngine;
import cc.game.GameObject;
import cc.gui.GameGui;
import cc.gui.Graphics;
import cc.gui.mainmenu.MenuGui;
import cc.util.Random;
import cc.util.Timer;
import cc.util.logger.LogLevel;
import cc.util.logger.LogPlace;
import cc.util.logger.LogType;
import cc.util.logger.Logger;
import cc.util.logger.PlacedLogger;



/**
 * The main program class. It controlls the gui and the game engine and runs
 * the main looop. If also routs events, mainly from user input, to ServerApp object,
 * wich then sends them to all clients.
 *
 * It's responsibilities include:
 *
 * Instanciating the game and executing the game's main loop
 * Server/Client relationship (direct, through socket)
 * Keeping track of the game's current state (paused, running etc)
 * Creating a new game, either as a host with a "direct" client or as a
 * client of a network game. (to be implemented)
 */
public class ClientApp implements Disposable
{
	private ServerApp serverApp;
	private GameEngine gameEngine;
	private GameGui gameGui;
	private MenuGui menuGui;

	private EventHandler eventHandler;

	private ProgramState programState;

	private Host host;
	private Connection serverConnection = null;

	private Timer localTimer = new Timer();

	private boolean isProgramRunning = true;

	private ProgramStateCode stateCode = ProgramStateCode.RUNNING;

	// Each player is given a random ID, hope there wont be any collisions. 1 in 4200000000...
	private int clientID = Random.getNormalRandom().nextInt();

	private PlacedLogger logger;

	private AppContext appContext;

	// For multiplayer, slow looping
	private int tickIntervalMicros = 20000;
	private double dtToSend = 0.01;

	// fast looping timing
//	private double timeConst =  0.0000009;
//	private static final double timeConst =  0.009;


//	private static final double timeConst =  0.0000002;
//	private static final double deltaTimeToSend = tickIntervalMicros * timeConst;
//	private double deltaTimeToSend = tickIntervalMicros*timeConst;
//	private double deltaTimeToSend = 0.03;

	// For singelplayer, fast loop, vync timing
//	private static int tickIntervalMicros = 1;
//	private double deltaTimeToSend = 0.01;




	public ClientApp( StartArgs args )
	{
		logger = Logger.get().createPlaced( LogPlace.APP );
		this.eventHandler = EventGlobals.getHandler();

		appContext = new AppContext( eventHandler, logger );

		menuGui = new MenuGui( appContext );
		gameGui = new GameGui( appContext );

		eventHandler.addReceivers( Arrays.asList( recs  ) );
		recs = null;

		TickEvent.setDt( dtToSend );

		programState = MENU;
	}

    private final ProgramState
		MENU = new MenuState(),
		CLIENT = new ClientGameState(),
//		WAIT_START = new WaitStartState(),
		HOST_GAME = new HostGameState();


	public class MenuState extends ProgramState
	{
		public MenuState() {
//			Logger.get().log( LogPlace.APP, "Switched to MenuState" );
//			menuGui.activate();
		}

		@Override
        public void enter()
		{
			Logger.get().log( LogPlace.APP, "Switched to MenuState" );
			menuGui.activate();
        }

		@Override
		public void update( double localDT ) {
			menuGui.update( localDT );
		}
	}

//	// NOT USED!!!
//	public class WaitStartState extends ProgramState
//	{
//		public WaitStartState()
//		{
////			Logger.get().info( "Switching to WaitStartState" );
//		}
//
//        @Override
//        public void tickState( double dT )
//        {
//        	setState( CLIENT );
//        	menuGui.deactivate();
//        	programState.tickState( dT );
//        	logger.info( "Switched from WaitStartState to ClientGameState" );
//        }
//
//		@Override
//		public void update( double localDT )
//		{
//			menuGui.update( localDT );
//		}
//	}

	public class ClientGameState extends ProgramState
	{
        @Override
        public void enter()
		{
        	logger.info( "Switched to ClientGameState" );
			menuGui.deactivate();
			gameGui.activate();
        }

		@Override
        public void tickState( double dT ) {
			gameEngine.update( dT );
		}

		@Override
		public void update( double localDT )
		{
			reciveServerEvents();
			gameGui.update( localDT );
		}

        @Override
        public void exit() {
        	cleanGameState();
        }
	}

	private void cleanGameState()
	{
		closeConnections();
		serverApp = null;
		gameEngine = null;
	}

	public class HostGameState extends ClientGameState
	{
        @Override
		public void update( double localDT )
        {
			serverApp.update();
			super.update( localDT );
		}
	}

	public void closeConnections()
	{
		logger.info( "Closing connections" );
    	try {
    		if ( host != null ) {
    			// Make sure the connection listening thread terminates
    			host.getServer().closeClients();
    		}
	        serverConnection.close();

        } catch ( IOException e ) {
        	Logger.get().log( LogPlace.APP, LogType.WARNING, "closeConnection(): Error while ending connection to server" );
        }

        host = null;
        serverConnection = null;
	}

//  Game state for running without the sever app
//	public class CheapGameState extends ProgramState
//	{
//		public CheapGameState() {
//			cheapStartGame();
//			timer.elapsedTime();
//		}
//		public void update() {
//			// TO DO: Update with local timer
//			gameEngine.update( timer.elapsedTime() );
//			gameGui.update();
//		}
//	}

	public ProgramStateCode run()
	{
		logger.info( "Entering main loop" );

		localTimer.start();
		programState.enter();

		while ( isProgramRunning ) {
			programState.update( localTimer.retrieveElapsedMillis() );
		}

		logger.info( "Exiting main loop" );
		return stateCode;
	}


	private void handleJoinEvent( JoinEvent event )
	{
        if (event.getReceiverId() == clientID ) {
			event.setMe( true );
			logger.info( LogPlace.NET, "Adding the local human player" );
		} else if ( event.isBot() ) {
			logger.info( LogPlace.NET, "Adding AI player" );
		} else {
			logger.info( LogPlace.NET, "Adding remote human player" );

			eventHandler.post( EventGroups.PLAYER_JOINED,
					new ValueEvent<String>( event.getNick() ) );
		}

		gameEngine.addPlayer( event.getReceiverId(), event.getNick(), event.isBot(), event.isMe() );
    }


	/**
	 * Here the events from the input reader arrive in the form on RequenstEvnets.
	 * First they are turned into real events, then they are set with the sender as the local player.
	 * Then they are sent to the Server game (either on local or remote machine) throught the serverConnection.
	 */
	private void handleRequestEvent( Event event )
	{
		GameObject obj = gameEngine.getPlayerObject();

		Asserts.notNull( event );
		if ( obj == null ) {
			return;
		}

		event.setReceiverID( obj.getID() );
		event.setSenderID( gameEngine.getLocalPlayerID() );

		// For the cheap game mode, just post the events
		//		eventHandler.postEvent( eventToSend );

		// For the real game mode, send evnets through the connection to the ServerApp
		serverConnection.send( event );
    }

	/**
	 * Fetches events from the server connection and sends them into the event
	 * system.
	 */
	public void reciveServerEvents()
	{
		try {
			for ( Event event : serverConnection.receive() ) {
				eventHandler.post( event.getReceiverGroup(), event );
			}
		} catch ( IOException exc ) {
			logger.log( LogPlace.NET, LogType.ERROR, "Error while trying to receive events from server. " + exc );
		}
	}

	/**
	 * Creates a host, connecting itself to it.
	 * Used in Singeplayer games and when hosting multiplayer games.
	 */
	private void setupLocalServer()
	{
		// Start listening, connect self to server
		host = new Host();
		serverApp = new ServerApp( host.getServer() );

		serverApp.setTickIntervalMicros( tickIntervalMicros );

		// Connect yourself to the (local) server
		serverConnection = host.connectLocalClient();

	}

	/**
	 * Start a new Single Player Game
	 */
	public void startSinglePlayer()
	{
		// TODO: Way does this happend? Does it?
		if ( gameEngine != null ) {
			logger.warn( "gameEngine not null, has game been started twise?" );
			return;
		}

		setupLocalServer();
		host.stopListening();
		gameStart( true );

		// Create AI players
		serverConnection.send( new JoinEvent( clientID + 1, "Plupp 1", true, false ) );
		serverConnection.send( new JoinEvent( clientID + 2, "Plupp 2", true, false ) );
		serverConnection.send( new JoinEvent( clientID + 3, "Plupp 3", true, false ) );
		serverConnection.send( new JoinEvent( clientID + 4, "Plupp 4", true, false ) );
		serverConnection.send( new JoinEvent( clientID + 5, "Plupp 5", true, false ) );

		serverApp.setGameRunning( true );
		gameEngine.setGameRunning( true );
		setState( HOST_GAME );
	}

	/**
	 * Start the game, add some AI players and change the gamestate to RUNNING.
	 */
	private void gameStart( boolean hosting )
	{
		gameEngine = new GameEngine( appContext );
		serverConnection.send( new JoinEvent( clientID, "Plupp", false, false ) );
	}

	/**
	 * Host a game, listening to incoming socket connections until the GUI tells us not to.
	 */
	public void hostMultiplayer()
	{
		Logger.get().log( LogPlace.APP, LogLevel.LOW, "Hosting multiplayer" );
		setupLocalServer();
	}

	public void cancelHosting()
	{
		// TODO: Polish multiplayer connection
	}
	public void cancelJoining()
	{
		// TODO: Polish multiplayer connection
	}

	public void startMultiplayer()
	{
		Logger.get().log( LogPlace.APP, LogLevel.LOW, "Startning multiplayer" );

		host.stopListening();

		gameStart( true );

		gameEngine.setGameRunning( true );
		serverApp.setGameRunning( true );

		setState( HOST_GAME );
	}


	public void joinRemoteGame( String addressStr )
	{
		Logger.get().log( LogPlace.APP, LogLevel.LOW, "Join menu event ip: " + addressStr );

		try {
	        serverConnection = new ClientSocketConnection( InetAddress.getByName( addressStr ) );
        } catch ( UnknownHostException e ) {
        	throw new RuntimeException( e );
        } catch ( IOException e ) {
        	throw new RuntimeException( e );
        }

		gameStart( false );
		gameEngine.setGameRunning( true );

		setState( CLIENT );
	}

	/**
	 * Starts the game without setting up connections and those things, if the game is
	 * run without sending events through the ServerApp and with timing without the ServerApp.
	 */
	// Dont work right now, not if events are sent through the connection and not
	// directly with the EventHandler
//	public void cheapStartGame()
//	{
//		gameEngine = new GameEngine();
//
//		eventHandler.postEvent( new JoinEvent( clientID, "Plupp", false, false ) );
//		eventHandler.postEvent( new JoinEvent( clientID + 1, "Plupp", true, false ) );
//		eventHandler.postEvent( new JoinEvent( clientID + 2, "Plupp", true, false ) );
//		eventHandler.postEvent( new JoinEvent( clientID + 3, "Plupp", true, false ) );
//		eventHandler.postEvent( new JoinEvent( clientID + 4, "Plupp", true, false ) );
//		eventHandler.postEvent( new JoinEvent( clientID + 5, "Plupp", true, false ) );
//
//		gameEngine.setGameRunning( true );
//	}



	public void dispose()
	{
		if ( host != null ) {
			host.stopListening();  // Make sure the connection listening thread terminates
		}

		try {
			if ( serverConnection != null ) {
				serverConnection.close();
			}
		} catch ( IOException e ) {
			Logger.get().log( LogPlace.NET, LogType.WARNING, "Error while closing server connection" );
		}

		Graphics.get().dispose();
		logger.info( "Exiting program" );
		Logger.get().dispose();
	}

	public void setState( ProgramState newState )
	{
		programState.exit();
		newState.enter();
    	this.programState = newState;
    }

	private void handleExitEvent()
	{
	    programState.finish();
	    isProgramRunning = false;
	    stateCode = ProgramStateCode.EXITING;
    }


	private void pauseGame()
	{
	    boolean newGameRunning = !serverApp.isGameRunning();
	    serverApp.setGameRunning( newGameRunning );
	    gameEngine.setGameRunning( newGameRunning );
    }


	private Sub[] recs = new Sub[] {

			new Sub( EventGroups.QUIT, QuitEvent.class,
					new Receiver<QuitEvent>() {
						public void receive( QuitEvent event ) {
						    logger.info("Receive  quit");
							programState.finish(); // this state should be a game state
							setState( MENU );
						}
					} ),

			new Sub( EventGroups.REQUEST,
					Event.class,
						new Receiver<Event>() {
							public void receive( Event event ) {
								handleRequestEvent( event );
							}
						} ),

			new Sub( EventGroups.EXIT,
					new NoArgReceiver() {
						public void receive() {
						    logger.info("Exit event");
							handleExitEvent();
						}
					} ),

			new Sub( EventGroups.RESET,
					new NoArgReceiver() {
						public void receive() {
							handleExitEvent();
							stateCode = ProgramStateCode.RESTARTING;
						}
				} ),


			new Sub( EventGroups.TICK,
					TickEvent.class,
					new Receiver<TickEvent>() {
						public void receive( TickEvent event ) {
							programState.tickState( event.getDt() );
						}
					} ),


			new Sub( EventGroups.PAUSE,
					new NoArgReceiver() {
						public void receive() {
							pauseGame();
						}
					} ),

			new Sub( EventGroups.NET, JoinEvent.class,
					new Receiver<JoinEvent>() {
						public void receive( JoinEvent event ) {
							handleJoinEvent( event );
						}
					} ),

			new Sub( EventGroups.CANCEL_HOST,
					new NoArgReceiver() {
						public void receive() {
							logger.info( "Canceling hosting game" );
							host.stopListening();
						}
					} ),

			new Sub( EventGroups.START_SINGLE_PLAYER,
					new NoArgReceiver() {
						public void receive() {
							startSinglePlayer();
						}
					} ),

			new Sub( EventGroups.HOST_MULTIPLAYER,
					new NoArgReceiver() {
						public void receive() {
							hostMultiplayer();
						}
					} ),

			new Sub( EventGroups.JOIN, JoinEvent.class ,
					new Receiver<JoinEvent>() {
						public void receive(
								JoinEvent event ) {
							handleJoinEvent( event );
						}
					} ),

			new Sub( EventGroups.JOIN_MULTIPLAYER, ValueEvent.class,
					new Receiver<ValueEvent<String>>() {
						public void receive( ValueEvent<String> event ) {
							joinRemoteGame( event.getValue() );
						}
					} ),

			new Sub( EventGroups.START_MULTIPLAYER,
					new NoArgReceiver() {
						public void receive() {
							startMultiplayer();
						}
					} ),
	};

}


