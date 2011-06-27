package cc.gui.input;

import org.fenggui.Display;
import org.fenggui.render.lwjgl.EventHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiInputHandler
{
	private int lastButtonDown = -1;

	public void update ( Display display )
	{
		readBufferedKeyboard( display );
		readBufferedMouse( display );
	}

	public void readBufferedKeyboard( Display desk ) {

	      //check keys, buffered
	      Keyboard.poll();

	      while ( Keyboard.next() ) {
			if ( Keyboard.getEventKeyState() ) // if pressed
			{
				desk.fireKeyPressedEvent( EventHelper.mapKeyChar(), EventHelper.mapEventKey() );
				// XXX: dirty hack to make TextEditor usable again on LWJGL.
				// This needs to be solved nicer in the future!
				desk.fireKeyTypedEvent( EventHelper.mapKeyChar() );
			} else {
				desk.fireKeyReleasedEvent( EventHelper.mapKeyChar(), EventHelper.mapEventKey() );
			}
		}
	}

	  /**
	   * reads a mouse in buffered mode
	   */
	  public void readBufferedMouse( Display desk )
	  {
		int x = Mouse.getX();
		int y = Mouse.getY();

		boolean hitGUI = false;
		// @todo the click count is not considered in LWJGL! #

		if ( lastButtonDown != -1 && Mouse.isButtonDown( lastButtonDown ) ) {
			hitGUI |= desk.fireMouseDraggedEvent( x, y, EventHelper.getMouseButton( lastButtonDown ) );
		} else {

			if ( Mouse.getDX() != 0 || Mouse.getDY() != 0 ) {
				hitGUI |= desk.fireMouseMovedEvent( x, y );
			}
			if ( lastButtonDown != -1 ) {
				hitGUI |= desk.fireMouseReleasedEvent( x, y, EventHelper.getMouseButton( lastButtonDown ), 1 );
				lastButtonDown = -1;
			}

			while ( Mouse.next() ) {

				if ( Mouse.getEventButton() != -1 && Mouse.getEventButtonState() ) {
					lastButtonDown = Mouse.getEventButton();
					hitGUI |= desk.fireMousePressedEvent( x, y, EventHelper.getMouseButton( lastButtonDown ), 1 );
				}
				// int wheel = Mouse.getEventDWheel();
				// if(wheel != 0) {
				// hitGUI |= desk.fireMouseWheel(x, y, wheel > 0, 1);
				// }
			}
		}
	}


}
