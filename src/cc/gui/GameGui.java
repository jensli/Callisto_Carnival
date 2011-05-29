package cc.gui;

import j.util.functional.Action0;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import cc.event.Event;
import cc.event.EventType;
import cc.event.QuitEvent;
import cc.event.game.FireEvent;
import cc.event.game.KillEvent;
import cc.event.game.RotateEvent;
import cc.event.game.ThrustEvent;
import cc.event.handlers.IEventHandler;
import cc.gui.game_display.GameDisplay;
import cc.gui.game_display.GameScreen;
import cc.gui.input.GameInputHandler;

public class GameGui
{
	private GameDisplay gameDisplay;
	
	private GameInputHandler bindingInputHandler;
	private IEventHandler eventHandler;
	
//	private GUIInputHandler guiInputHandler;
	
	public GameGui( IEventHandler eventHandler )
	{
		gameDisplay = new GameDisplay();
		bindingInputHandler = new GameInputHandler();
		
		this.eventHandler = eventHandler;
		// For gui elements added to the game view
//		guiInputHandler = new GUIInputHandler();
		init();
	}
	
	public void init()
	{
		// Init the key bindings for the game
		Event zoomIn = Event.make( EventType.ZOOM, GameScreen.ZOOM_IN ),
			zoomOut = Event.make( EventType.ZOOM, GameScreen.ZOOM_OUT ),
			stopZoom = Event.make( EventType.ZOOM, GameScreen.STOP_ZOOM ),
			thrustOn = Event.make( EventType.REQUEST, new ThrustEvent( Event.SWITCH_ON ) ),
			thrustOff = Event.make( EventType.REQUEST, new ThrustEvent( Event.SWITCH_OFF ) ),
			turnRightOn = Event.make( EventType.REQUEST, new RotateEvent( RotateEvent.RIGHT, Event.SWITCH_ON ) ),
			turnRightOff = Event.make( EventType.REQUEST, new RotateEvent( RotateEvent.RIGHT, Event.SWITCH_OFF ) ),
			turnLeftOn = Event.make( EventType.REQUEST, new RotateEvent( RotateEvent.LEFT, Event.SWITCH_ON ) ),
			turnLeftOff = Event.make( EventType.REQUEST, new RotateEvent( RotateEvent.LEFT, Event.SWITCH_OFF ) ),
			fireOn = Event.make( EventType.REQUEST, new FireEvent( Event.SWITCH_ON ) ),
			fireOff = Event.make( EventType.REQUEST, new FireEvent( Event.SWITCH_OFF ) ),
			kill = Event.make( EventType.REQUEST, new KillEvent() ),
			paus = Event.make( EventType.PAUSE ),
			restart = Event.make( EventType.RESTART );
		
		bindingInputHandler.addStandardBind( Keyboard.KEY_RIGHT, turnRightOn, turnRightOff );
		bindingInputHandler.addStandardBind( Keyboard.KEY_LEFT, turnLeftOn, turnLeftOff );
		bindingInputHandler.addStandardBind( Keyboard.KEY_UP, thrustOn, thrustOff );
		bindingInputHandler.addStandardBind( Keyboard.KEY_ESCAPE, new QuitEvent() );
		bindingInputHandler.addStandardBind( Keyboard.KEY_P, paus );
		bindingInputHandler.addStandardBind( Keyboard.KEY_Z, zoomIn, stopZoom );
		bindingInputHandler.addStandardBind( Keyboard.KEY_X, zoomOut, stopZoom );
		bindingInputHandler.addStandardBind( Keyboard.KEY_SPACE, fireOn, fireOff );
		bindingInputHandler.addStandardBind( Keyboard.KEY_K, kill  );

		bindingInputHandler.addStandardBind( Keyboard.KEY_R, EventType.makeGuiEvent( new Action0() {
			public void run() { gameDisplay.resetFocusObject(); }
		}) );
		bindingInputHandler.addStandardBind( Keyboard.KEY_Q, restart );
		
		bindingInputHandler.addStandardBind( Keyboard.KEY_TAB, EventType.makeGuiEvent( new Action0() {
			public void run() { gameDisplay.nextFocusObject(); }
		}) );
		
	}
	
	
	public void update( double localDT )
	{
		if ( Display.isCloseRequested() ) {
			eventHandler.postEvent( Event.make( EventType.EXIT_PROGRAM ) );
			return;
		}
		
		gameDisplay.update( localDT );
		
		List<Event> events = bindingInputHandler.update();
		eventHandler.postEvents( events );
		
		// TODO:
//		guiInputHandler.update(  null );
	}
	
	/**
	 * Called when app switched from another view the game view
	 */
	public void activate()
	{
		gameDisplay.activate();
//		Mouse.setGrabbed( true );
	}

	/**
	 * Called when app switched from game view to another view
	 */
	public void deactivate()
	{
		
	}
	/**
	 * Called when app exits (typically)
	 */
	public void finish()
	{
//		gameDisplay.destroyDisplay();
	}
	
	
}

