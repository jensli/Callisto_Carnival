package cc.event2;

import static j.util.eventhandler.EventHandler.Setting.PREBUILD_CACHE;
import static j.util.eventhandler.EventHandler.Setting.THROW_ON_NO_REC;
import j.util.eventhandler.EventHandler;
import j.util.eventhandler.EventHandler.Cashing;
import j.util.eventhandler.EventHandlerImpl;

public final class EventGlobals
{
	private EventGlobals() {}

	public static EventHandler handler;

	public static void init() {
		handler = new EventHandlerImpl();

		handler.getSettings()
			.setThrowOnNoReceiver( true )
			.setCasheStrategy( Cashing.PREBUILD );

		handler.settings( THROW_ON_NO_REC, PREBUILD_CACHE );

//		handler.addGroup( ROOT );
//		handler.addGroup( GUI, ROOT );
//		handler.addGroup( GAME, ROOT );
////		handler.addGroup( OBJECTS, ROOT );
//		handler.addGroup( SERVER, ROOT );
//		handler.addGroup( REQUEST, SERVER );

		handler.addReceiversFromClass( EventGroups.class );
	}

	public static EventHandler getHandler() {
		return handler;
	}
}
