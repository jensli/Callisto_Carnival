package cc.gui.menu_old_system;

import org.lwjgl.input.Keyboard;

public class MenuInputHandler
{
	Menu menu;

	public MenuInputHandler( Menu menu )
    {
		this.menu = menu;
    }

	public void update()
	{
		while ( Keyboard.next() ) {
			// If not a keydown, skip this key event
			if ( !Keyboard.getEventKeyState() ) {
				continue;
			}
			// Do menu action depending on key
			switch ( Keyboard.getEventKey() ) {

				case Keyboard.KEY_UP:
					menu.selectUp();
					break;
				case Keyboard.KEY_DOWN:
					menu.selectDown();
					break;
				case Keyboard.KEY_RETURN:
					menu.enterPress();
					break;
				case Keyboard.KEY_ESCAPE:
					menu.goBack();
					break;
				case Keyboard.KEY_SPACE:
					menu.spacePress();
					break;

				default:
					// Check if this was a normal printable char, then send it to the menu.
					char ch = Keyboard.getEventCharacter();
					if ( !Character.isISOControl( ch ) ) {
						menu.charInput( ch );
					}
			}
		}
	}

}
