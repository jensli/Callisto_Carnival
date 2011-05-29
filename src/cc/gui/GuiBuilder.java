package cc.gui;


import j.util.util.Pair;

import org.fenggui.Container;
import org.fenggui.Label;
import org.fenggui.TextEditor;
import org.fenggui.composites.TextArea;
import org.fenggui.composites.Window;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.layout.RowLayout;
import org.fenggui.util.Dimension;
import org.fenggui.util.Point;

import cc.event.Event;
import cc.event.EventFactory;
import cc.event.EventType;
import cc.event.handlers.EventHandler;
import cc.event.handlers.IEventHandler;

public class GuiBuilder
{
	private GuiFactory factory;
	
	public GuiBuilder() {
		factory = new GuiFactory( 
				FengUtils.loadFont( "res/battlefield.ttf", 26 ),
				FengUtils.loadFont( "res/battlefield.ttf", 20 ) );
	}
	
//	public static GuiBuilder get() {
//		return instance;
//	}
	
	public void init()
	{
		Graphics.get().pushAllGL();
		
//		try {
//			URL url = Thread.currentThread().getContextClassLoader().getResource( "res/QtCurve/QtCurve.xml" );
//	        FengGUI.setTheme( new XMLTheme( url.getPath() ) );
//	    } catch ( IOException e ) {
//	    	throw new RuntimeException( e );
//	    } catch ( IXMLStreamableException e ) {
//	    	throw new RuntimeException( e );
//	    }
		
		Graphics.get().popAllGL();
	}
	
	public Pair<Window, TextArea> makeHostGameDialog( final MainMenuDisplay mainMenu )
	{
		Window window = factory.makeDialogWindow( "Host multiplayer game", new Point( 80, 250 ) );
//		window.setSize( new Dimension( display.getWidth(), display.getDisplayY() ) );
		Container mainContainer = window.getContentContainer();
		final IEventHandler events = EventHandler.get();

		mainContainer.setLayoutManager( new RowLayout( false ) );
		
		Label text = factory.makeLabel( "Waiting for connections from other players" );
		
		IButtonPressedListener startLis = new IButtonPressedListener() {
			public void buttonPressed( ButtonPressedEvent arg0 ) {
				events.postEvent( Event.make( EventType.START_MULTIPLAYER ) );
			}
		};
		IButtonPressedListener cancelLis = new IButtonPressedListener() {
			public void buttonPressed( ButtonPressedEvent arg0 ) {
				events.postEvent( Event.make( EventType.CANCEL_HOST_MULTIPLAYER ) );
				mainMenu.closePopup();
			}
		};
		
		Container lowerContainer = new Container( new RowLayout( true ) );

		TextArea playerList = new TextArea();
		playerList.getTextEditor().getAppearance().setFont( factory.getFont1() );
		playerList.getTextEditor().setEnabled( false );
		playerList.setMinSize( 50, 100 );
		
		lowerContainer.addWidget( factory.makeButton( "Start game", startLis ) );
		lowerContainer.addWidget( factory.makeButton( "Cancel", cancelLis ) );

		mainContainer.addWidget( text );
		mainContainer.addWidget( playerList );
		mainContainer.addWidget( lowerContainer );
		
		window.pack();
		
		return Pair.make( window, playerList );
	}
	
	public Window makeInstructionsDialog( final MainMenuDisplay mainMenu )
	{
		Window window = factory.makeDialogWindow( "Instructions", new Point( 80, 200 ) );
		Container mainContainer = window.getContentContainer();
		mainContainer.setLayoutManager( new RowLayout( false ) );
		
		IButtonPressedListener closelLis = new IButtonPressedListener() {
			public void buttonPressed( ButtonPressedEvent arg0 ) {
				mainMenu.closePopup();
			}
		};
		
		String[] text = {
				"Stear ship - Left/Right keys",
				"Thrust - Up key",
				"Fire - Space",
				"Zoom in - 'z'",
				"Zoom out - 'x'",
				"Pause - 'p'",
		};
		
		TextArea textArea = new TextArea();
		textArea.getTextEditor().getAppearance().setFont( factory.getFont1() );
		textArea.getTextEditor().setEnabled( false );
		textArea.setMinSize( 800, 300 );
		
		for ( String line : text ) {
			textArea.addTextLine( line );
		}
		
		mainContainer.addWidget( textArea );
		mainContainer.addWidget( factory.makeButton( "Close", closelLis ) );
		window.pack();
		
		return window;
	}
	
	public Window makeWaitStartDialog( final MainMenuDisplay mainMenu )
	{
		final IEventHandler events = EventHandler.get();
		
		Window window = factory.makeDialogWindow( "Waiting", new Point( 80, 200 ) );
		
		Container mainContainer = window.getContentContainer();
		mainContainer.setLayoutManager( new RowLayout( false ) );
		
		final IButtonPressedListener cancelLis = new IButtonPressedListener() {
			public void buttonPressed( ButtonPressedEvent arg0 ) {
				mainMenu.closePopup();
				events.postEvent( Event.make( EventType.CANCEL_JOIN_GAME ) );
			}
		};
		
		final Label text = factory.makeLabel( 
				"Successfully connected to host. Waiting for the host to start the game." );
		
		mainContainer.addWidget( text );
		mainContainer.addWidget( factory.makeButton( "Cancel", cancelLis ) );
		window.pack();
		
		return window;
	}
	
	
	public Window makeJoinDialog( final MainMenuDisplay mainMenu )
	{
		Window window = factory.makeDialogWindow( "Join remote multiplayer game", new Point( 90, 250 ) );
		Container mainContainer = window.getContentContainer();
		mainContainer.setLayoutManager( new RowLayout( false ) );
		
		Label label = factory.makeLabel( "Enter the IP address of the host: " );

		final TextEditor addressBox = new TextEditor( false );
		addressBox.getAppearance().setFont( factory.getFont1() );
		addressBox.setMinSize( 50, 30 );
		addressBox.setText( "127.0.0.1" );
		addressBox.setSelectOnFocus( true );
		
		Container buttonContainer = new Container( new RowLayout( true ) );

		IButtonPressedListener joinLis = new IButtonPressedListener() {
			public void buttonPressed( ButtonPressedEvent arg0 ) {
				mainMenu.openWaitStartDialog( addressBox.getText() );
			}
		};
		IButtonPressedListener cancelLis = new IButtonPressedListener() {
			public void buttonPressed( ButtonPressedEvent arg0 ) {
				mainMenu.closePopup();
			}
		};
		
		buttonContainer.addWidget( factory.makeButton( "Join game", joinLis ) );
		buttonContainer.addWidget( factory.makeButton( "Cancel", cancelLis ) );
		mainContainer.addWidget( label );
		mainContainer.addWidget( addressBox );
		mainContainer.addWidget( buttonContainer );
		
		window.pack();
		
		return window;
	}
	
	public Window makeIpErrorDialog( final MainMenuDisplay mainMenu )
	{
		final Window window = factory.makeDialogWindow( "Error", new Point( 90, 250 ) );
		final Container mainContainer = window.getContentContainer();
		
		mainContainer.setLayoutManager( new RowLayout( false ) );
		
		IButtonPressedListener okLis = new IButtonPressedListener() {
			public void buttonPressed( ButtonPressedEvent arg0 ) {
				mainMenu.addressErrorOk();
			}
		};
		
		mainContainer.addWidget( factory.makeLabel( "The entered address is not a valid IP address." ) );
		mainContainer.addWidget( factory.makeButton( "Ok", okLis ) );
		
		window.pack();
		
		return window;
	}
	
	public Window makeMainMenu( final MainMenuDisplay mainMenu )
	{
		final IEventHandler events = EventHandler.get();
		
		// init the root Widget, that spans the whole
		Window window = factory.makeDialogWindow( "Callisto Carnival - Main Menu", new Point( 30, 100 ) );
		window.getContentContainer().setLayoutManager( new RowLayout( false ) );
		
		IButtonPressedListener emptyLis = new IButtonPressedListener() {
            public void buttonPressed( ButtonPressedEvent arg0 ) {
            	;
            }
		};
		IButtonPressedListener newSingLis = new IButtonPressedListener() {
            public void buttonPressed( ButtonPressedEvent arg0 ) {
            	events.postEvent( EventFactory.makeEvent( EventFactory.Type.START_SINGELPLAYER ) );
            }
		};
		IButtonPressedListener hostLis = new IButtonPressedListener() {
			public void buttonPressed( ButtonPressedEvent arg0 ) {
				mainMenu.openHostGameDialog();
				
				events.postEvent( Event.make( EventType.HOST_GAME ) );
			}
		};
		IButtonPressedListener joinLis = new IButtonPressedListener() {
			public void buttonPressed( ButtonPressedEvent arg0 ) {
				mainMenu.openJoinGameDialog();
			}
		};
		IButtonPressedListener instLis = new IButtonPressedListener() {
			public void buttonPressed( ButtonPressedEvent arg0 ) {
				mainMenu.openInstructionsDialog();
			}
		};
//		IButtonPressedListener startMultiLis = new IButtonPressedListener() {
//			public void buttonPressed( ButtonPressedEvent arg0 ) {
//				events.postEvent( Event.make( EventType.START_MULTIPLAYER ) );
//			}
//		};
		IButtonPressedListener quitLis = new IButtonPressedListener() {
			public void buttonPressed( ButtonPressedEvent arg0 ) {
				events.postEvent( Event.make( EventType.EXIT_PROGRAM ) );
			}
		};
		
		Container container = window.getContentContainer();
		container.addWidget( factory.makeButton( "Start single player game", newSingLis ) );
		container.addWidget( factory.makeButton( "Join multiplayer game", joinLis ) );
		container.addWidget( factory.makeButton( "Host new multiplayer game", hostLis ) );
//		container.addWidget( factory.makeButton( "Start multiplayer game", startMultiLis ) );
		container.addWidget( factory.makeButton( "Load saved game", emptyLis ) );
		container.addWidget( factory.makeButton( "Settings", emptyLis ) );
		container.addWidget( factory.makeButton( "Instructions", instLis ) );
		container.addWidget( factory.makeButton( "Quit", quitLis ) );
		
		window.pack();
		
		return window;
	}
	
	
	public Container makeBlockingContainer( Dimension size )
	{
		Container container = new Container();
		container.setPosition( new Point( 0, 0 ) );
		container.setSize( size );
		return container;
	}
	
	
	
	
	
	
}
