package cc.gui.input;


import org.lwjgl.opengl.Display;

import cc.event2.EventGlobals;
import cc.event2.EventGroups;



// Not used right now

public class InputHandlerHandler
{
	private GameInputHandler<Object> bindingsHandler = new GameInputHandler<Object>();
	private GuiInputHandler guiHandler = new GuiInputHandler();

	public void update()
	{
		if ( Display.isCloseRequested() ) {
			EventGlobals.getHandler().postEmpty( EventGroups.EXIT );
		}

		bindingsHandler.update();
		guiHandler.update( null );
	}



}
