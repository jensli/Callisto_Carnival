package cc.event2;

import j.util.eventhandler.EventHandlers;
import j.util.eventhandler.GroupName;
import j.util.eventhandler.Parent;



public final class EventGroups
{
	private EventGroups() {}

	@Parent("")
	public static GroupName
		ROOT;

	@Parent("ROOT")
	public static GroupName
		GAME_INPUT,
		REQUEST,
		APP,
		GUI,
		GAME,
		SERVER,
		NET;

	@Parent("GAME")
	public static GroupName
		ROTATE,
		FIRE,
		THRUST,
		TICK;

	@Parent("GUI")
	public static GroupName
		PLAYER_JOINED;

	@Parent("APP")
	public static GroupName
		EXIT,
		RESET,
		PAUSE,
		CANCEL_HOST,
		JOIN_MULTIPLAYER,
		CANCEL_JOIN,
		HOST_MULTIPLAYER,
		START_SINGLE_PLAYER,
		START_MULTIPLAYER;


	// Initiate the GroupNames, reflexivly loop over them and create id string
	// from the field name.
	static {
		EventHandlers.initGroupNames( EventGroups.class );
	}

}

