package cc.event;

import java.util.ArrayList;
import java.util.EnumMap;

import cc.event.handlers.EventReceiver;


//
// Not used.
//
public class EventFactory  {
	
	public enum Type {
		HOST_GAME, START_SINGELPLAYER,
		ZOOM, PAUSE,
		EXIT_PROGRAM
		;
		
	}
	
	@SuppressWarnings("unchecked")
	public static Events<EventFactory.Type> events = new Events<EventFactory.Type>( 
			new EnumMap<EventFactory.Type, StandardEvent>( EventFactory.Type.class ), 
			new EnumMap<EventFactory.Type, StandardValueEvent>( EventFactory.Type.class ) );
	
	
	public ArrayList<String> list;
	
	public static StandardEvent makeEvent( EventFactory.Type c ) {
		return events.makeEvent( c );
	}
	public static <T> StandardValueEvent<T> makeEvent( EventFactory.Type type, T value ) {
		return events.makeEvent( type, value );
	}
	
	public static void makeEventTypes()
	{
		events.makeEventType( EventFactory.Type.HOST_GAME, Event.Cathegory.APPLICATION, new StandardEvent() {
			@Override
            public void dispatch( EventReceiver receiver ) {
				receiver.receiveHostGameEvent( this );
			}
		});
		events.makeEventType( EventFactory.Type.START_SINGELPLAYER, Event.Cathegory.APPLICATION, new StandardEvent() {
			@Override
            public void dispatch( EventReceiver receiver ) {
				receiver.receiveStartSingelplayerEvent( this );
			}
		});
		events.makeEventType( EventFactory.Type.ZOOM, Event.Cathegory.GUI, new StandardValueEvent<Integer>() {
			@Override
            public void dispatch( EventReceiver receiver ) {
				receiver.receiveZoomEvent( this );
			}
		});
		events.makeEventType( EventFactory.Type.PAUSE, Event.Cathegory.APPLICATION, new StandardEvent() {
			@Override
            public void dispatch( EventReceiver receiver ) {
				receiver.receivePauseEvent( this );
			}
		});
		events.makeEventType( EventFactory.Type.EXIT_PROGRAM, Event.Cathegory.APPLICATION, new StandardEvent() {
			@Override
            public void dispatch( EventReceiver receiver ) {
				receiver.receiveExitProgramEvent( this );
			}
		});
		

	}
}
