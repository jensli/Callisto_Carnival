package cc.gui.input;

import cc.event.Event;
import cc.event.Events;

public abstract class KeyBind
{
	public static final int
		NO_MOD = 0,
		CTRL = 1,
		SHIFT = 2,
		ALT = 4;

	private boolean pressed = false;

	private int modifyers = 0;

	public Event doKeyPress() {
		return null;
	}
	public Event doKeyRelease() {
		return null;
	}
	public Event doKeyHoldDown() {
		return null;
	}

	public int getModifyers() {
		return modifyers;
	}
	public void setModifyers( int modifyers ) {
    	this.modifyers = modifyers;
    }
	public boolean isPressed() {
    	return pressed;
    }
	public void setPressed( boolean pressed ) {
    	this.pressed = pressed;
    }

	public boolean validMods( int mods )
	{
//		return mods == getModifyers();
		return ( mods & getModifyers() ) == getModifyers();
	}
}

class StandardBind extends KeyBind
{
	private Event onEvent;
	private Event offEvent;
	private Event holdEvent;


    public StandardBind( Event onEvent, Event offEvent, Event holdEvent ) {
	    this.onEvent = onEvent;
	    this.offEvent = offEvent;
	    this.holdEvent = holdEvent;
    }



	public StandardBind( Event onEvent, Event offEvent, Event holdEvent, int modifyers ) {
	    this.onEvent = onEvent;
	    this.offEvent = offEvent;
	    this.holdEvent = holdEvent;

	    setModifyers( modifyers );
    }

	public StandardBind( Event onEvent, Event offEvent ) {
		this( onEvent, offEvent, null );
    }
    public StandardBind( Event onEvent ) {
    	this( onEvent, null );
    }

	@Override
    public Event doKeyPress()
	{
		return doPost( onEvent );
    }
	@Override
    public Event doKeyRelease()
	{
		return doPost( offEvent );
    }
	@Override
    public Event doKeyHoldDown()
	{
		return doPost( holdEvent );
	}
	private Event doPost( Event event )
	{
//		if ( event != null ) {
//			EventHandler.get().postEvent( Events.cloneEvent( event ) );
//		}
		if ( event != null ) {
			return Events.cloneEvent( event );
		} else {
			return null;
		}

	}

}




//class OnOffBind extends KeyBind
//{
//	private OnOffEvent event;
//	public OnOffBind( OnOffEvent event )
//	{
//		this.event = event;
//	}
//	public void doKeyPress()
//	{
//		OnOffEvent newEvent = event.clone();
//		newEvent.setOn( true );
//		EventHandler.get().postEvent( newEvent );
//	}
//	public void doKeyRelease()
//	{
//		OnOffEvent newEvent = event.clone();
//		newEvent.setOn( false );
//		EventHandler.get().postEvent( newEvent );
//	}
//
//	public void doKeyHoldDown() {}
//}
//
//class StearingBind extends KeyBind
//{
//	String onEventString, offEventString;
//
//	public StearingBind( String eventString )
//    {
//	    onEventString = eventString + " " + Event.SWITCH_ON;
//	    offEventString = eventString + " " + Event.SWITCH_OFF;
//    }
//    public void doKeyPress()
//    {
//		EventHandler.get().postEvent(
//				new RequestEvent( Event.NO_RECEIVER, onEventString ) );
//    }
//    public void doKeyRelease()
//    {
//		EventHandler.get().postEvent(
//				new RequestEvent( Event.NO_RECEIVER, offEventString ) );
//    }
//}
//
//class RequestBind extends KeyBind
//{
//	String eventString;
//
//	public RequestBind( String eventString )
//    {
//	    this.eventString = eventString;
//    }
//
//    public void doKeyPress()
//    {
//		EventHandler.get().postEvent( new RequestEvent( Event.NO_RECEIVER, eventString  ) );
//    }
//}
//
//



//bindList.put( Keyboard.KEY_UP, new StandardBind( thrustOn, thrustOff ) );
//bindList.put( Keyboard.KEY_UP, new StearingBind( Event.THRUST + " 0" + " -1" ));
//bindList.put( Keyboard.KEY_RIGHT, new StearingBind( Event.ROTATE + " 0" + " -1 " + false ));
//bindList.put( Keyboard.KEY_LEFT, new StearingBind( Event.ROTATE + " 0" + " -1 " + true ));
//bindList.put( Keyboard.KEY_SPACE, new StearingBind( Event.FIRE + " 0" + " -1" ) );
//bindList.put( Keyboard.KEY_K, new RequestBind( Event.KILL + " 0" + " -1" ) );
//bindList.put( Keyboard.KEY_Z, new OnOffBind( new ZoomEvent( ZoomEvent.ZOOM_IN ) ) );
//bindList.put( Keyboard.KEY_X, new OnOffBind( new ZoomEvent( ZoomEvent.ZOOM_OUT ) ) );
//bindList.put( Keyboard.KEY_Z, new OnOffBind( Events.makeEvent( EventType.ZOOM, GameScreen.ZOOM_IN ) ) );
//bindList.put( Keyboard.KEY_X, new OnOffBind( Events.makeEvent( EventType.ZOOM, GameScreen.ZOOM_OUT ) ) );
//bindList.put( Keyboard.KEY_Z, new StandardBind( zoomIn, stopZoom ) );
//bindList.put( Keyboard.KEY_X, new StandardBind( zoomOut, stopZoom ) );
