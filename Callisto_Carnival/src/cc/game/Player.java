package cc.game;

import cc.event.Event;
import cc.event.game.CreateEvent;
import cc.event2.EventGlobals;
import cc.event2.EventGroups;
import cc.game.objects.Ship;
import cc.util.Random;

/**
 * Class containing information about the players, and logic to handle tasks
 * not directly associated with the in-game behavior.
 */
public class Player
{
	public static final int NO_PLAYER = -1;

	public String name;

	private Ship controlledObject;

	private int
		controlledID = Player.NO_PLAYER, // TODO: Is this really used? What does it do?
		playerID = Player.NO_PLAYER;

	private double
		respawnTime = 0.5,
		timeToRespawn = 0;

	public enum Color {
		BLUE, GREEN, RED, YELLOW
	}

	private static int colorNr = Random.getGameRandom().nextInt( Color.values().length );

	private Color color = Color.values()[ colorNr++ % Color.values().length ];

	public Player( String name, int id )
	{
		this.name = name;
		this.playerID = id;
	}

	/**
	 * Updates the player. Most importantly respawns player if it has been dead
	 * long enough.
	 * @param dT
	 */
	public void update(double dT)
	{
		if ( !hasLivingObject() ) {
			timeToRespawn -= dT;

			if ( timeToRespawn <= 0.0 ) {
				respawn();
				timeToRespawn = respawnTime;
			}
		}
	}

	public void routeToObject( Event event )
	{
		if ( hasLivingObject() ) {
			controlledObject.receiveEvent( event );
		}
	}

	public boolean hasLivingObject()
	{
		return controlledObject != null && controlledObject.isAlive();
	}
	/**
	 * Creates a new spacship for the player and enters it into the game
	 */
	public void respawn()
	{
		Ship ship = GameFactory.get().createPlayerShip( name, color );
		controlledObject = ship;
		setControlledID( controlledObject.getID() );


		EventGlobals.getHandler().post( EventGroups.CREATE,
				new CreateEvent( controlledObject ) );
	}

	/////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	///
	///  SETTER AND GETTERS
	///
	/////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

    public int getPlayerID() { return playerID; }

	public Ship getControlledObject() {
		return controlledObject;
	}
	public int getControlledID() {
		return controlledID;
	}
	public void setControlledID( int controlledID ) {
		this.controlledID = controlledID;
	}
	public void setPlayerID( int playerID ) {
		this.playerID = playerID;
	}
	public void setControlledObject( Ship controlledObject ) {
		this.controlledObject = controlledObject;
	}
}
