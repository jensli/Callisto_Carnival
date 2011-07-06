package cc.gui;

import j.util.eventhandler.EventHandler;
import j.util.eventhandler.Posting;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import cc.app.AppContext;
import cc.event.QuitEvent;
import cc.event.game.FireEvent;
import cc.event.game.KillEvent;
import cc.event.game.RotateEvent;
import cc.event.game.ThrustEvent;
import cc.event2.EventGroups;
import cc.gui.game_display.GameDisplay;
import cc.gui.input.GameInputHandler;

public class GameGui
{
	private GameDisplay gameDisplay;

	private GameInputHandler<Posting> inputHandler;

	private EventHandler eventHandler;

//	private GUIInputHandler guiInputHandler;

	public GameGui( AppContext context )
	{
		gameDisplay = new GameDisplay( context );
		inputHandler = new GameInputHandler<Posting>();

		this.eventHandler = context.getEventHandler();

		//		this.eventHandler = context.getEventHandler();
		// For gui elements added to the game view
//		guiInputHandler = new GUIInputHandler();
		inputHandler.addBinds( Arrays.asList( binds ) );
		binds = null;
	}



	public void update( double localDT )
	{
		if ( Display.isCloseRequested() ) {
			// TODO: remove
			eventHandler.postEmpty( EventGroups.EXIT );
			return;
		}

		gameDisplay.update( localDT );

		List<Posting> events = inputHandler.update();

		for ( Posting p : events ) {
			eventHandler.post( p );
		}

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


	private Bind[] binds = new Bind[] {

		new Bind( Keyboard.KEY_Z,
				new Posting( EventGroups.ZOOM_IN ),
				new Posting( EventGroups.ZOOM_STOP ),
				null ),

		new Bind( Keyboard.KEY_X,
				new Posting( EventGroups.ZOOM_OUT ),
				new Posting( EventGroups.ZOOM_STOP ),
				null ),

		new Bind( Keyboard.KEY_RIGHT,
				new Posting( EventGroups.REQUEST, new RotateEvent( false, true ) ),
				new Posting( EventGroups.REQUEST, new RotateEvent( false, false ) ),
				null ),

		new Bind( Keyboard.KEY_LEFT,
				new Posting( EventGroups.REQUEST, new RotateEvent( true, true ) ),
				new Posting( EventGroups.REQUEST, new RotateEvent( true, false ) ),
				null ),

		new Bind( Keyboard.KEY_UP,
				new Posting( EventGroups.REQUEST, new ThrustEvent( true ) ),
				new Posting( EventGroups.REQUEST, new ThrustEvent( false ) ),
				null ),

		new Bind( Keyboard.KEY_SPACE,
				new Posting( EventGroups.REQUEST, new FireEvent( true ) ),
				new Posting( EventGroups.REQUEST, new FireEvent( false ) ),
				null ),

		new Bind( Keyboard.KEY_K, new Posting( EventGroups.REQUEST, new KillEvent() ), null, null ),

		new Bind( Keyboard.KEY_R, new Posting( EventGroups.RESET ), null, null ),

		new Bind( Keyboard.KEY_ESCAPE, new Posting( EventGroups.QUIT, new QuitEvent() ), null, null ),
	};


}

