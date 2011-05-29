package cc.event;

import j.util.functional.Action0;
import cc.event.handlers.EventReceiver;

public final class EventType
{
	public static final StandardValueEvent<Integer> ZOOM
		= new StandardValueEvent<Integer>() {
			{ setType( Event.Cathegory.GUI ); }
			@Override
		    public void dispatch( EventReceiver receiver ) {
				receiver.receiveZoomEvent( this );
			}
		};
	
	public static final StandardValueEvent<Event> REQUEST
		=  new StandardValueEvent<Event>() {
			{ setType( Event.Cathegory.REQUEST ); }
			@Override
			public void dispatch( EventReceiver receiver ) {
				receiver.receiveRequestEvent( this );
			}
		};
	public static final StandardValueEvent<String> JOIN_GAME
		=  new StandardValueEvent<String>() {
			{ setType( Event.Cathegory.APPLICATION ); }
			@Override
	        public void dispatch( EventReceiver receiver ) {
				receiver.receiveJoinMultiplayerEvent( this );
			}
		},
		JOIN_GAME_NOTIFICATION
		=  new StandardValueEvent<String>() {
			{ setType( Event.Cathegory.GUI ); }
			@Override
	        public void dispatch( EventReceiver receiver ) {
				receiver.receiveJoinNotification( this );
			}
		};

	public static final StandardEvent
		HOST_GAME = new StandardEvent() { 
			{ setType( Event.Cathegory.APPLICATION ); }
			@Override
            public void dispatch( EventReceiver receiver ) {
				receiver.receiveHostGameEvent( this );
			}
		},
		CANCEL_HOST_MULTIPLAYER = new StandardEvent() { 
			{ setType( Event.Cathegory.APPLICATION ); }
			@Override
            public void dispatch( EventReceiver receiver ) {
				receiver.receiveCancelHostGameEvent( this );
			}
		},
		START_SINGELPLAYER = new StandardEvent() {
			{ setType( Event.Cathegory.APPLICATION ); }
			@Override
            public void dispatch( EventReceiver receiver ) {
				receiver.receiveStartSingelplayerEvent( this );
			}
		},
		PAUSE =  new StandardEvent() {
			{ setType( Event.Cathegory.APPLICATION ); }
			@Override
            public void dispatch( EventReceiver receiver ) {
				receiver.receivePauseEvent( this );
			}
		},
		EXIT_PROGRAM =  new StandardEvent() {
			{ setType( Event.Cathegory.APPLICATION ); }
			@Override
            public void dispatch( EventReceiver receiver ) {
				receiver.receiveExitProgramEvent( this );
			}
		},
		RESTART =  new StandardEvent() {
			{ setType( Event.Cathegory.APPLICATION ); }
			@Override
            public void dispatch( EventReceiver receiver ) {
				receiver.receiveResetEvent( this );
			}
		},
		START_MULTIPLAYER =  new StandardValueEvent<Event>() {
			{ setType( Event.Cathegory.APPLICATION ); }
			@Override
            public void dispatch( EventReceiver receiver ) {
				receiver.receiveStartMultiplayerEvent( this );
			}
		},
		CANCEL_JOIN_GAME =  new StandardEvent() {
			{ setType( Event.Cathegory.APPLICATION ); }
			@Override
            public void dispatch( EventReceiver receiver ) {
				receiver.receiveCancelJoinMultiplayerEvent( this );
			}
		};
		
	
	
	
	public static Event makeGuiEvent( final Action0 fun )
	{
		return new StandardEvent() {
			
			public boolean hasRun = false;
			
			{ setType( Event.Cathegory.GUI ); }
			
			@Override 
			
			public void dispatch( EventReceiver receiver ) {
				if ( !hasRun ) { 
					fun.run();
					hasRun = false;
				}
			}
		};
	}
	
	
}
