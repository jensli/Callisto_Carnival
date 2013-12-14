package cc.game;

import j.util.eventhandler.EventHandler;
import j.util.eventhandler.Receiver;
import j.util.eventhandler.Sub;
import j.util.util.Asserts;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cc.app.AppContext;
import cc.event.Event;
import cc.event.GuiResetEvent;
import cc.event.game.CollisionEvent;
import cc.event.game.CreateEvent;
import cc.event.game.FireEvent;
import cc.event.game.KillEvent;
import cc.event.game.RotateEvent;
import cc.event.game.ThrustEvent;
import cc.event.handlers.EventReceiver;
import cc.event2.EventGroups;
import cc.util.logger.LogPlace;
import cc.util.logger.LogType;
import cc.util.logger.Logger;


/**
 * <p>
 * Class containing game logic.
 * </p>
 * <p>
 * One of these is created locally at every client player.
 * </p>
 */

public class GameEngine extends EventReceiver
{
	private Simulation simulation;

//	private double stepSize = 0.01;
	private double timeConstant = 1.0;
//	private double timeConstant = 0.5;
//	private double stepSize = timeConstant * ServerApp.getTickInterval();

//	private Timer timer = new Timer( 0 ); // NOT USED!!!

	private Map<Integer, Player> playerList = new TreeMap<Integer, Player>();

	 // The deamons are things that is updated each tick and performs something,
	 // but they are not an acctual game object. Like AI-players.
	private List<GameDeamon> deamonList = new LinkedList<GameDeamon>();

	private boolean gameRunning = false;

	 // The id of the player that plays on this machine
	private int localPlayerID = Player.NO_PLAYER;

	private EventHandler eventHandler;


	/**
	 * Sets up a working game and fills it with objects.
	 */
	public GameEngine( AppContext context )
	{
		simulation = new Simulation();
		createGame();

		eventHandler = context.getEventHandler();
		eventHandler.addReceivers( subs );
		subs = null;
	}

	// Update the game state, move all game objects and check for collisions,
	// update players.
	public void update( double dT )
	{
		if ( !gameRunning ) {
			return;
		}

		double adjustedDT  = dT*timeConstant;

		for ( Player player : playerList.values() ) {
			player.update( adjustedDT );
		}

		for ( GameDeamon deamon : deamonList ) {
			deamon.update( simulation, adjustedDT );
		}

		simulation.update( adjustedDT );

	}

	/**
	 * Creates simulation object and calls methods of GameMap class to fill the
	 * simulation with planets and other space objects.
	 */
	private void createGame()
	{
		simulation.addSpaceObjectList( GameFactory.get().createPlanets( ) );
	}


	/**
	 * Entry point for events into this class.
	 */
	@Override
	public void receiveEvent( Event event )
	{
		Asserts.notNull( event );

		event.dispatch( this );
	}

	/**
	 * Adds a player to the game. Human or AI. Inserts them in the player list.
	 * When a player is in the player list, in will respawn automaticly in the
	 * next update.
	 *
	 * @param playerID
	 *            The PlayerID to set for this player.
	 * @param nick
	 *            Player nickname.
	 * @param isBot
	 *            Whether the player is AI controlled or human.
	 * @param isMe
	 *            true if this player is the local player.
	 */
	public void addPlayer( int playerID, String nick, boolean isBot, boolean isMe )
	{

		if ( playerList.containsKey( playerID ) ) {
			Logger.get().log( LogPlace.GAME, LogType.WARNING, "Player " + playerID + " already in the list!" );
			throw new IllegalStateException("Player already in player list!");
		}

		Player player = new Player( nick, playerID );
		playerList.put( playerID, player );

		if ( isBot ) {
			GameDeamon ai = new ComputerPlayer( simulation, playerList.values(), player );
			deamonList.add( ai );
		}

		if ( isMe ) {
			localPlayerID = playerID;
			// This is a little weird because there has been experementaion on
			// how to send the object list.

			// Send an event to the GUI that there is a player that can be showed.

			eventHandler.post( EventGroups.GUI_RESET,
					new GuiResetEvent( player,
							simulation.getObjectList(),
							simulation.getDrawableList() ) );
		}
	}



	/**
	 * The object controlled by the local player.
	 *
	 * @return object controlled by the local player or null if that object is
	 *         not in the object list.
	 */

	public GameObject getPlayerObject()
	{
		Player player = getPlayer( localPlayerID );

		if ( player == null ) {
			return null;
		}

		int objID = player.getControlledID();

		if ( objID < 0 ) {
			return null;
		}

		return simulation.getObject( objID );
	}

	/**
	 * Get a player object from the Id of the {@link GameObject} it controlles
	 */
	public Player getPlayerByObject( int objectID )
	{
		for ( Player player : playerList.values() ) {
			if ( player.getControlledID() == objectID ) {
				return player;
			}
		}
		return null;
	}

	public void setGameRunning( boolean newGameRunning )
	{
		if ( !gameRunning && newGameRunning ) {
			// Reset timer
//			timer.elapsedTime();
		}

		gameRunning = newGameRunning;
	}

	private List<Sub> subs = Arrays.asList(
			new Sub( EventGroups.GAME,
					Event.class,
					new Receiver<Event>() {
						public void receive( Event event ) {
							event.dispatch( GameEngine.this );
						}
				} ) );

	@Override
	public void receiveCollisionEvent( CollisionEvent event ) {
		routeToObject( event );
	}

	@Override
	public void receiveFireEvent( FireEvent event ) {
		routeToPlayer( event );
	}

	@Override
	public void receiveRotateEvent( RotateEvent event ) {
		routeToPlayer( event );
	}

	@Override
	public void receiveThrustEvent( ThrustEvent event ) {
		routeToPlayer( event );
	}

	@Override
	public void receiveCreateEvent( CreateEvent event ) {
		simulation.addSpaceObject( event.getObject() );
	}

	@Override
	public void receiveKillEvent( KillEvent event ) {
		routeToObject( event );
		// this.killObject( event.getReceiverID() );
	}

	private void routeToObject( Event event ) {
//		simulation.storeEvent( event );
		GameObject obj = simulation.getObject( event.getReceiverId() );

		if ( obj != null ) {
			obj.receiveEvent( event );
		}
	}

	private void routeToPlayer( Event event )
	{
		GameObject obj = simulation.getObject( event.getReceiverId() );

		if ( obj != null ) {
			obj.receiveEvent( event );
		}
	}


	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	////
	//// Setters and getters
	////
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////


	public Simulation getSimulation() {
		return simulation;
	}
	private Player getPlayer( int ID ) {
		return playerList.get( ID );
	}
	public Collection<Player> getPlayerList() {
		return playerList.values();
	}
	public int getLocalPlayerID() {
		return localPlayerID;
	}
	public void killObject( int objectID ) {
		simulation.killObject( objectID );
	}
	public Collection<GameObject> getObjectList() {
		return simulation.getObjectList();
	}


}



