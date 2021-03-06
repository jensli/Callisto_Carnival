package cc.gui.input;


import j.util.lists.IterCacheList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lwjgl.input.Keyboard;

import cc.util.GraphicsUtil;


// NOT USED!!!
class KeyEvent
{
	private boolean isPress = true;
	private int modifyers;
	private int code;

	public KeyEvent( boolean isPress, int modifyers, int code ) {
	    this.isPress = isPress;
	    this.modifyers = modifyers;
	    this.code = code;
    }

	public boolean isPress() {
    	return isPress;
    }
	public void setPress( boolean isPress ) {
    	this.isPress = isPress;
    }
	public int getModifyers() {
    	return modifyers;
    }
	public void setModifyers( int modifyers ) {
    	this.modifyers = modifyers;
    }
	public int getCode() {
    	return code;
    }
	public void setCode( int code ) {
    	this.code = code;
    }
}


/**
 * Modifyers does not work!!!
 *
 * Maintains a list of KeyBind objects, loop through them to see if
 * their key is pressed, then sends event
 */

public class GameInputHandler<T>
{
	// Which keycodes trigger which binding
	Map<Integer, ArrayList<KeyBind<T>>> bindList = new HashMap<Integer, ArrayList<KeyBind<T>>>();

	// The keycodes for keys pressed down at the moment
	Set<Integer> pressedKeys = new HashSet<Integer>();

	public GameInputHandler()
	{
	}


//	public void update()
//	{
//		int pressedModifyers = readModifyers();
//
//		// Loop throught all binds, send start event if they are just pressed
//		// and stop event if the wore just released.
//		for ( Map.Entry<Integer, List<KeyBind>> entry : bindList.entrySet() ) {
//
//			int keyCode = entry.getKey();
//			boolean isKeyDown = Keyboard.isKeyDown( keyCode );
//
//			for ( KeyBind bind : entry.getValue() ) {
//
////				final KeyBind bind = entry.getValue();
//
//				// Has key status changed since last time?
//				if ( isKeyDown != pressedKeys.contains( keyCode ) )  {
//
//					if ( isKeyDown ) {
//
//						pressedKeys.add( keyCode );
//
//						if ( bind.validMods( pressedModifyers ) ) {
//							bind.setPressed( true );
//							bind.doKeyPress();
//						}
//
//					} else {
//
//						pressedKeys.remove( keyCode );
//
//						if ( bind.isPressed() ) {
//							bind.doKeyRelease();
//							bind.setPressed( false );
//						}
//					}
//
//				} else if ( isKeyDown && bind.isPressed() ) {
//					// If doKeyPress() was called last time and the key is
//					// still down we end up here
//					bind.doKeyHoldDown();
//				}
//
//			}  // End entry for
//
//		}  // End bindList for
//
//	}

	public static <T> void addIfNotNull( Collection<T> list, T e )
	{
		if ( e != null ) {
			list.add( e );
		}
	}

	private IterCacheList<KeyBind<T>> bindIterable = new IterCacheList<KeyBind<T>>();

	public List<T> update()
	{
		int pressedModifyers = readModifyers();

		List<T> generatedEvents = new LinkedList<T>();

		// Loop throught all binds, send start event if they are just pressed
		// and stop event if the wore just released.
		for ( Map.Entry<Integer, ? extends List<KeyBind<T>>> entry : bindList.entrySet() ) {

			int keyCode = entry.getKey();
			boolean isKeyDown = Keyboard.isKeyDown( keyCode );

			for ( KeyBind<T> bind : bindIterable.withList( entry.getValue() ) ) {
//			for ( KeyBind<T> bind : entry.getValue() ) {

//				final KeyBind bind = entry.getValue();

				// Has key status changed since last time?
				if ( isKeyDown != pressedKeys.contains( keyCode ) )  {

					if ( isKeyDown ) {

						pressedKeys.add( keyCode );

						if ( bind.validMods( pressedModifyers ) ) {
							bind.setPressed( true );
							addIfNotNull( generatedEvents, bind.doKeyPress() );
						}

					} else {

						pressedKeys.remove( keyCode );

						if ( bind.isPressed() ) {
							addIfNotNull( generatedEvents, bind.doKeyRelease() );
							bind.setPressed( false );
						}
					}

				} else if ( isKeyDown && bind.isPressed() ) {
					// If doKeyPress() was called last time and the key is
					// still down we end up here
					addIfNotNull( generatedEvents, bind.doKeyHoldDown() );
				}

			}  // End entry for

		}  // End bindList for

		return generatedEvents;
	}

	public int readModifyers()
	{
		int mod = 0;

		if ( GraphicsUtil.isAnyDown( Keyboard.KEY_LCONTROL, Keyboard.KEY_RCONTROL ) ) {
			mod |= KeyBind.CTRL;
		}
		if ( GraphicsUtil.isAnyDown( Keyboard.KEY_LSHIFT, Keyboard.KEY_RSHIFT ) ) {
			mod |= KeyBind.SHIFT;
		}

		return mod;
	}


	public void addBind( int keyCode, KeyBind<T> bind )
	{
		if ( !bindList.containsKey( keyCode ) ) {
			bindList.put( keyCode, new ArrayList<KeyBind<T>>() );
		}
		bindList.get( keyCode ).add( bind );
		bindList.get( keyCode ).trimToSize();
	}

	public void addBind( KeyBind<T> bind ) {
		addBind( bind.getKey(), bind );
	}

	public void addBinds( Iterable<? extends KeyBind<T>> binds ) {
		for ( KeyBind<T> b : binds ) {
			addBind( b.getKey(), b );
		}
	}

//	public void addStandardBind( int keyCode, Event onEvent ) {
//		addStandardBind( keyCode, onEvent, null );
//	}
//
//	public void addStandardBind( int keyCode, int mods, Event onEvent ) {
//		addStandardBind( keyCode, onEvent, null, null, mods );
//	}
//
//	public void addStandardBind( int keyCode, Event onEvent, Event offEvent ) {
//		addStandardBind( keyCode, onEvent, offEvent, null, 0);
//	}
//
//	public void addStandardBind( int keyCode, Event onEvent, Event offEvent, Event holdEvent, int mods ) {
//		addBind( keyCode, new StandardBind( onEvent, offEvent, holdEvent, mods ) );
//	}
}

