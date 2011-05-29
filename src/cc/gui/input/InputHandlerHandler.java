package cc.gui.input;


import org.lwjgl.opengl.Display;

import cc.event.Event;
import cc.event.EventType;
import cc.event.handlers.EventHandler;



// Not used right now

public class InputHandlerHandler
{
	private GameInputHandler bindingsHandler = new GameInputHandler();
	private GuiInputHandler guiHandler = new GuiInputHandler();
	
	public void update()
	{
		if ( Display.isCloseRequested() ) {
			EventHandler.get().postEvent( Event.make( EventType.EXIT_PROGRAM ) );
		}
		
		bindingsHandler.update();
		guiHandler.update( null );
	}
	
	
	
}
