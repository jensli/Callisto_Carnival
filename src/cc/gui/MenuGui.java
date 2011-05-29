package cc.gui;


import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import cc.event.Event;
import cc.event.EventType;
import cc.event.handlers.IEventHandler;
import cc.gui.input.GuiInputHandler;


public class MenuGui
{
	private GuiInputHandler guiInputHandler;
	private MainMenuDisplay menuDisplay;
	private FadeOverlay fadeOverlay;
	
	private IEventHandler eventHandler;

	
	public MenuGui( IEventHandler eventHandler )
	{
		this.eventHandler = eventHandler;
		
		guiInputHandler = new GuiInputHandler();
		menuDisplay = new MainMenuDisplay();
		fadeOverlay = new FadeOverlay();
	}
	
	public void update( double dT )
	{
		if ( Display.isCloseRequested() ) {
			eventHandler.postEvent( Event.make( EventType.EXIT_PROGRAM ) );
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
