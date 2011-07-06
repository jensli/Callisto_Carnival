package cc.gui.mainmenu;



import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;
import j.util.eventhandler.EventHandler;
import j.util.eventhandler.Receiver;
import j.util.eventhandler.Sub;
import j.util.util.Pair;

import org.fenggui.Container;
import org.fenggui.Display;
import org.fenggui.IWidget;
import org.fenggui.composites.TextArea;
import org.fenggui.composites.Window;
import org.fenggui.render.lwjgl.LWJGLBinding;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.glu.GLU;

import cc.app.AppContext;
import cc.event.StandardValueEvent;
import cc.event2.EventGroups;
import cc.gui.FadeOverlay;
import cc.gui.Graphics;
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
	private EventHandler eventHandler;

	private FadeOverlay fadeOverlay = new FadeOverlay();

	private Sub playerJoinSub = new Sub(
			EventGroups.PLAYER_JOINED,
			StandardValueEvent.class,
			new Receiver<StandardValueEvent<String>>() {
				public void receive( StandardValueEvent<String> event ) {
					playerList.addTextLine( event.getValue() );
				}
			});

	public MainMenuDisplay( AppContext context )
	{
		eventHandler = context.getEventHandlerNew();
		backgroundTexture = ResourceHandler.get().getTexture( Name.SPLASH );

		eventHandler.addReceiver( playerJoinSub );

		init( context );
	}


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


	public void init( AppContext context )
	{
		Graphics.get().pushAllGL();

		display = new org.fenggui.Display( new LWJGLBinding() );

		MainMenuBuilder builder = new MainMenuBuilder( context );

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
			eventHandler.post( EventGroups.JOIN_MULTIPLAYER,
					new StandardValueEvent<String>( ipStr ) );

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

