package cc.event;

import cc.event.handlers.EventReceiver;


// Experiment, not used.

public enum EventType2
{
		HOST_GAME( Event.Cathegory.APPLICATION, new StandardEvent() {
			@Override
            public void dispatch( EventReceiver receiver ) {
				receiver.receiveHostGameEvent( this );
			}
		}),
		START_SINGELPLAYER( Event.Cathegory.APPLICATION, new StandardEvent() {
			@Override
            public void dispatch( EventReceiver receiver ) {
				receiver.receiveStartSingelplayerEvent( this );
			}
		}),
		ZOOM( Event.Cathegory.GUI, new StandardValueEvent<Integer>() {
			@Override
            public void dispatch( EventReceiver receiver ) {
				receiver.receiveZoomEvent( this );
			}
		});



//		PAUSE,
//		EXIT_PROGRAM
//		;

		private final StandardEvent event;
		@SuppressWarnings( "rawtypes" )
		private final StandardValueEvent valueEvent;

		EventType2( Event.Cathegory cat, StandardEvent event ) {
			event.setType( cat );
			 this.event = event;
			 this.valueEvent = null;
		}
		@SuppressWarnings("rawtypes")
		EventType2( Event.Cathegory cat, StandardValueEvent event ) {
			event.setType( cat );
			this.valueEvent = event;
			this.event = null;
		}

		public StandardEvent make() {
			return event.clone();
		}
		@SuppressWarnings("unchecked")
		public <TYPE> StandardEvent make( TYPE value ) {
			valueEvent.setValue( value );
			return event.clone();
		}




}
