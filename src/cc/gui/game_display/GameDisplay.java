package cc.gui.game_display;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.lwjgl.opengl.Display;

import cc.event.Event;
import cc.event.GuiResetEvent;
import cc.event.StandardValueEvent;
import cc.event.handlers.EventHandler;
import cc.event.handlers.EventReceiver;
import cc.game.GameObject;
import cc.game.ObjectCathegory;
import cc.game.Player;
import cc.gui.FadeOverlay;
import cc.gui.models.GraphicalModelIterator;
import cc.util.math.Vec;


/**
 * Displays the game view = the acctual game, the minimap, the Hud with Armour, fuel etc.
 * Receives a GuiResetEvent to get a ref to the Player object te be able to read info there for
 * the Hud and the list of all GameObjects 
 * @author jens
 */

public class GameDisplay extends EventReceiver 
{
	private GameScreen gameScreen;
	private Minimap minimap;
	private Hud hud;
	private FadeOverlay fadeOverlay;
	
	private GraphicalModelIterator itr;

	//	private Collection<GameObject> objectList_OLD = new LinkedList<GameObject>();

	// Init to dummy list to avoid NullPointer
	private Collection<GameObject> objectList = new LinkedList<GameObject>();
	
	private GameObject focusObject = null;
	
	private Player focusPlayer = new Player( "Dummy", -1 );
	
	boolean hasPlayerFocus = true;
	
	public GameDisplay()
	{
		EventHandler.get().addEventReceiver( this, Event.Cathegory.GUI );
		
		gameScreen = new GameScreen();
		minimap = new Minimap();
		hud = new Hud();
		fadeOverlay = new FadeOverlay( 0.0005 );
	}
	
	public void update( double dT )
	{
		if ( !Display.isActive() ) {
			return;
		}
		
		Vec focusPoint = findFocusPoint();
		
		gameScreen.draw( dT, focusPoint, objectList );
		minimap.draw( focusPoint, itr, objectList );
		hud.draw( focusPlayer );
		
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
	
	@Override
    public void receiveEvent( Event event ) {
		event.dispatch( this );
	}
	
	@Override
    public void receiveGuiResetEvent( GuiResetEvent event )
    {
		focusPlayer = event.getFocusPlayer();
//		objectList_OLD = event.getObjectList();
		objectList = event.getObjectList();
		itr = event.getItr();
    }
	
	@Override
    public void receiveZoomEvent( StandardValueEvent<Integer> event ) 
	{
		gameScreen.setZoom( event.getValue() );
    }
	
	public void activate() {
		fadeOverlay.startFade();
	}

	public void destroyDisplay() {
//		Graphics.get().destroyDisplay();
	}
}
