package cc.gui.mainmenu;


import j.util.eventhandler.EventHandler;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import cc.app.AppContext;
import cc.event2.EventGroups;
import cc.gui.FadeOverlay;
import cc.gui.input.GuiInputHandler;

/**
 * Overall handler for the menu system
 */
public class MenuGui
{
	private GuiInputHandler guiInputHandler;
	private MainMenuDisplay menuDisplay;
	private FadeOverlay fadeOverlay;

	private EventHandler eventHandler;


	public MenuGui( AppContext context )
	{
		this.eventHandler = context.getEventHandlerNew();

		guiInputHandler = new GuiInputHandler();
		menuDisplay = new MainMenuDisplay( context );
		fadeOverlay = new FadeOverlay();
	}

	public void update( double dT )
	{
		if ( Display.isCloseRequested() ) {
			eventHandler.postEmpty( EventGroups.EXIT );
		}

		guiInputHandler.update( menuDisplay.getDisplay() );
		menuDisplay.update( dT );
	}

	public void activate()
	{
		Mouse.setGrabbed( false );
		menuDisplay.activate();
		fadeOverlay.startFade();
	}

	public void deactivate()
	{
		menuDisplay.deactivate();
	}


}
