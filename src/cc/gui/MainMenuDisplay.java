package cc.gui;



import static org.lwjgl.opengl.GL11.*;
import j.util.util.Pair;

import org.fenggui.Container;
import org.fenggui.Display;
import org.fenggui.IWidget;
import org.fenggui.composites.TextArea;
import org.fenggui.composites.Window;
import org.fenggui.render.lwjgl.LWJGLBinding;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.glu.GLU;

import cc.event.Event;
import cc.event.EventType;
import cc.event.StandardValueEvent;
import cc.event.Event.Cathegory;
import cc.event.handlers.EventHandler;
import cc.event.handlers.EventReceiver;
import cc.util.Texture;
import cc.util.Util;
import cc.util.logger.LogPlace;
import cc.util.logger.Logger;
import cc.util.resources.Name;
import cc.util.resources.ResourceHandler;



public class MainMenuDisplay
{
	private Texture backgroundTexture;
	
	private Window mainMenuDialog;
	private Window hostGameDialog;
	private Window joinGameDialog;
	private Window ipErrorDialog;
	private Window waitStartDialog;
	private Window instructionsDialog;
	private TextArea playerList;
	
	private Display display;
	
	private Window popup = null;
	
	private Container blockingContainer;
	
	private FadeOverlay fadeOverlay = new FadeOverlay();
	
	public MainMenuDisplay() {
		backgroundTexture = ResourceHandler.get().getTexture( Name.SPLASH );
		EventHandler.get().addEventReceiver( eventReceiver, Cathegory.GUI );
		
		init();
	}
	
	private EventReceiver eventReceiver = new EventReceiver() {
		@Override public void receiveEvent( Event event ) {
			event.dispatch( this );
		}

		@Override
        public void receiveJoinNotification( StandardValueEvent<String> event ) {
			playerList.addTextLine( event.getValue() );
        }
	};
	
	public Display getDisplay() {
    	return display;
    }

	public void addWidget( IWidget w ) {
		display.addWidget( w );
	}
	public void addWidget( Window w ) {
		
		display.addWidget( w );
		
		// Do this here instead of on creation,
		// window has to be in the widget tree for this
		if ( w.isResizable() ) {
			w.setResizable( false );
		}
		w.setMovable( false );
	}
	
	
	public void init()
	{
		Graphics.get().pushAllGL();

		display = new org.fenggui.Display( new LWJGLBinding() );
		
		GuiBuilder builder = new GuiBuilder();
		
		blockingContainer = builder.makeBlockingContainer( display.getSize() );
		mainMenuDialog = builder.makeMainMenu( this );
		
		Pair<Window, TextArea> p = builder.makeHostGameDialog( this );
		hostGameDialog = p.a; 
		playerList = p.b;
		joinGameDialog = builder.makeJoinDialog( this );
		ipErrorDialog = builder.makeIpErrorDialog( this );
		instructionsDialog = builder.makeInstructionsDialog( this );
		waitStartDialog = builder.makeWaitStartDialog( this );
        
		addWidget( mainMenuDialog );
//		GUIFactory.get().addWidget( mainMenuDialog );
//		GUIFactory.get().add( hostGameDialog );
		
        Graphics.get().popAllGL();
	}
	

	
	public void popDialogStack() 
	{
		display.removePopup();
//		GUIFactory.get().getDisplay().removePopup();
	}
	
	public void update( double dT )
	{
		GL11.glClear( GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT );
		drawBackground();
		Graphics.get().pushAllGL();
		
        GL11.glLoadIdentity();
		GLU.gluLookAt( 10, 8, 8, 0, 0, 0, 0, 0, 1 );

		display.display();
		
		fadeOverlay.update( dT );
		fadeOverlay.draw();

		Graphics.get().popAllGL();
		org.lwjgl.opengl.Display.update();

	}
	
	private void drawBackground()
	{
		backgroundTexture.bind();
		
		Graphics.get().enterOrthoProjection();

		float maxX = Graphics.get().getScreenRatio(),
			yCoordUp = 0.25f * backgroundTexture.getHeight(),
			yCoordLow = 1 - yCoordUp;
		
		glColor3f( 1, 1, 1 );
		
		glBegin( GL11.GL_QUADS );
			glTexCoord2f( 0.06f, yCoordUp );
			glVertex2f( 0, 0 );
			glTexCoord2f( 0.06f, yCoordLow );
			glVertex2f( 0, 1 );
			glTexCoord2f( 0.88f, yCoordLow );
			glVertex2f( maxX, 1 );
			glTexCoord2f( 0.88f, yCoordUp );
			glVertex2f( maxX, 0 );
		glEnd();
				
		Graphics.get().leaveOrthoProjection();
	}
	
	public void activate() {
		fadeOverlay.startFade();
		display.setFocusedWidget( null );  // So 'space's from the game dont activate a button
	}

	private void openPopup( Window newPopup ) 
	{
		if ( isPopupOpen() ) {
			throw new IllegalStateException( "Tried to open new popup window without closing the old one." );
		}
		
		this.popup = newPopup;
		addWidget( blockingContainer );
		addWidget( newPopup );
		display.getDisplay().setFocusedWidget( newPopup );
    }
	
	public void closePopup() 
	{
		display.removeWidget( blockingContainer );
		popup.close();
		popup = null;
	}

	public void openHostGameDialog() {
		playerList.setText( "" );
		openPopup( hostGameDialog );
	}
	public void openJoinGameDialog() {
		openPopup( joinGameDialog );
	}
	public void openInstructionsDialog() {
		openPopup( instructionsDialog );
	}
	
	public void openWaitStartDialog( String ipStr )
	{
		closePopup();
		
		if ( Util.isIpAddress( ipStr ) ) {
			Logger.get().log( LogPlace.GUI, "Wait start good." );
			openPopup( waitStartDialog );
			EventHandler.get().postEvent( Event.make( EventType.JOIN_GAME, ipStr ) );
		} else {
			Logger.get().log( LogPlace.GUI, "Wait start error." );
			openPopup( ipErrorDialog );
		}
	}
	
	public void addressErrorOk() {
		closePopup();
		openPopup( joinGameDialog );
	}
	
	public boolean isPopupOpen() {
		return popup != null;
	}
	
	public void deactivate()
	{
		if ( isPopupOpen() ) {
			closePopup();
		}
	}
	
}

