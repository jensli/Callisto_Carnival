package cc.gui.game_display;

import j.util.eventhandler.EventHandler;
import j.util.eventhandler.NoArgReceiver;
import j.util.eventhandler.Receiver;
import j.util.eventhandler.Sub;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.lwjgl.opengl.Display;

import cc.app.AppContext;
import cc.event.GuiResetEvent;
import cc.event2.EventGroups;
import cc.game.GameObject;
import cc.game.ObjectCathegory;
import cc.game.Player;
import cc.gui.FadeOverlay;
import cc.util.math.Vec;


/**
 * Displays the game view = the acctual game, the minimap, the Hud with Armour, fuel etc.
 * Receives a GuiResetEvent to get a ref to the Player object te be able to read info there for
 * the Hud and the list of all GameObjects
 */

public class GameDisplay // extends EventReceiver
{
	private GameScreen gameScreen;
	private Minimap minimap;
	private Hud hud;
	private NoticeArea noticeArea;
	private FadeOverlay fadeOverlay;

	// Init to dummy list to avoid NullPointer
	private Collection<GameObject> objectList = Collections.emptyList();

	private GameObject focusObject = null;

	private Player focusPlayer = new Player( "Dummy", -1 );

	boolean hasPlayerFocus = true;
	private EventHandler eventHandler;

	public GameDisplay( AppContext context, org.fenggui.Display display )
	{
		eventHandler  = context.getEventHandler();

		gameScreen = new GameScreen();
		minimap = new Minimap();
		hud = new Hud();
		fadeOverlay = new FadeOverlay( 0.0005 );
		noticeArea = new NoticeArea();

		for ( Sub sub : subs ) {
			eventHandler.addReceiver( sub );
		}

		subs = null;
	}


	public void update( double dT )
	{
//		if ( !Display.isActive() ) {
//			return;
//		}

		Vec focusPoint = findFocusPoint();

		gameScreen.draw( dT, focusPoint, objectList );
		minimap.draw( focusPoint, objectList );
		hud.draw( focusPlayer );
		noticeArea.draw( null );

		fadeOverlay.update( dT );
		fadeOverlay.draw();

		Display.update();
	}

	private Vec findFocusPoint()
	{
	    Vec focusPoint = new Vec( 0, 0 );

		// Render, protect for null for player, object or object list
		if ( hasPlayerFocus && focusPlayer != null && focusPlayer.getControlledObject() != null ) {
			focusObject = focusPlayer.getControlledObject();
		}

		if ( focusObject != null ) {
			focusPoint = focusObject.getPos();
		}

	    return focusPoint;
    }


	public void resetFocusObject()
	{
		hasPlayerFocus = true;
	}

	/**
	 * Update the obj that have the focus in the game, to enable cycling
	 * through all game objs.
	 */
	public void nextFocusObject()
	{
		hasPlayerFocus = false;
		boolean isFound = false;
		GameObject first = null;

		Iterator<GameObject> itr = objectList.iterator();

		while ( itr.hasNext() ) {

			GameObject obj = itr.next();

			// Skip all shots
			if ( obj.getCathegory() == ObjectCathegory.SHOT ) {
				continue;
			}

			// Save the first found obj to warp around the list
			if ( first == null ) {
				first = obj;
			}

			// When current focus is found, make the next obj the focus,
			// or the first one if current focus is the last obj
			if ( obj == focusObject ) {
				focusObject = itr.hasNext() ? itr.next() : first;
				isFound = true;
				break;
			}
		}

		// If focusObject is not found, e.g. have died, choose first obj
		if ( !isFound ) {
			focusObject = first;
		}
	}

//	@Override
//    public void receiveEvent( Event event ) {
//		event.dispatch( this );
//	}
//
//	@Override
//    public void receiveGuiResetEvent( GuiResetEvent event )
//    {
//		focusPlayer = event.getFocusPlayer();
////		objectList_OLD = event.getObjectList();
//		objectList = event.getObjectList();
//		itr = event.getItr();
//    }

//	@Override
//    public void receiveZoomEvent( StandardValueEvent<Integer> event )
//	{
//		gameScreen.setZoom( event.getValue() );
//    }

	public void activate() {
		fadeOverlay.startFade();
	}

	public void destroyDisplay() {
//		Graphics.get().destroyDisplay();
	}

	private void guiReset( GuiResetEvent event ) {
		focusPlayer = event.getFocusPlayer();
		objectList = event.getObjectList();
	}



	private Sub[] subs = new Sub[] {
			new Sub( EventGroups.ZOOM_IN, new NoArgReceiver() {
				public void receive() {
					gameScreen.setZoom( GameScreen.ZOOM_IN );
				}
			} ),
			new Sub( EventGroups.ZOOM_OUT, new NoArgReceiver() {
				public void receive() {
					gameScreen.setZoom( GameScreen.ZOOM_OUT );
				}
			} ),
			new Sub( EventGroups.ZOOM_STOP, new NoArgReceiver() {
				public void receive() {
					gameScreen.setZoom( GameScreen.STOP_ZOOM );
				}
			} ),
			new Sub( EventGroups.GUI_RESET,
					GuiResetEvent.class,
					new Receiver<GuiResetEvent>() {
						public void receive( GuiResetEvent event ) {
							guiReset( event );
						}
			} ),

			// TODO:
	};


}
