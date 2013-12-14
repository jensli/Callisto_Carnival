package cc.gui.mainmenu;


import j.util.eventhandler.EventHandler;
import j.util.util.Pair;

import org.fenggui.Container;
import org.fenggui.Label;
import org.fenggui.TextEditor;
import org.fenggui.appearance.TextAppearance;
import org.fenggui.binding.render.Font;
import org.fenggui.binding.render.text.ITextRenderer;
import org.fenggui.composite.TextArea;
import org.fenggui.composite.Window;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.layout.RowLayout;
import org.fenggui.text.content.factory.simple.TextStyle;
import org.fenggui.util.Dimension;
import org.fenggui.util.Point;

import cc.app.AppContext;
import cc.event2.EventGroups;
import cc.gui.FengUtils;
import cc.gui.Graphics;

public class MainMenuBuilder
{
	private GuiFactory factory;
	private EventHandler eventHandler;

	public MainMenuBuilder( AppContext context ) {
		eventHandler = context.getEventHandler();
		factory = new GuiFactory(
				FengUtils.loadFont( "res/battlefield.ttf", 26 ),
				FengUtils.loadFont( "res/battlefield.ttf", 20 ) );
	}

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

		mainContainer.setLayoutManager( new RowLayout( false ) );

		Label text = factory.makeLabel( "Waiting for connections from other players" );

		IButtonPressedListener startLis = new IButtonPressedListener() {
			public void buttonPressed( ButtonPressedEvent arg0 ) {
				eventHandler.postEmpty( EventGroups.START_MULTIPLAYER );
			}
		};
		IButtonPressedListener cancelLis = new IButtonPressedListener() {
			public void buttonPressed( ButtonPressedEvent arg0 ) {
				eventHandler.postEmpty( EventGroups.CANCEL_HOST );
				mainMenu.closePopup();
			}
		};

		Container lowerContainer = new Container( new RowLayout( true ) );

		TextArea playerList = new TextArea();
		setFont( playerList.getTextEditor().getAppearance(), factory.getFont1() );
//		playerList.getTextEditor().getAppearance().setFont( factory.getFont1() );
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


	public static void setFont( TextAppearance appearance, Font font ) {
	    TextStyle def = new TextStyle();
//	    def.getTextStyleEntry(TextStyle.DEFAULTSTYLEKEY).setColor(color);
	    appearance.addStyle(TextStyle.DEFAULTSTYLEKEY, def);
	    ITextRenderer renderer = appearance.getRenderer(ITextRenderer.DEFAULTTEXTRENDERERKEY).copy();
	    renderer.setFont(font);
	    appearance.addRenderer(ITextRenderer.DEFAULTTEXTRENDERERKEY, renderer);

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
		setFont( textArea.getTextEditor().getAppearance(), factory.getFont1() );
//		textArea.getTextEditor().getAppearance().setFont( factory.getFont1() );
		textArea.getTextEditor().setEnabled( false );
		textArea.setMinSize( 800, 300 );

		for ( String line : text ) {
			textArea.getTextEditor().addContentAtEnd( line );
//			textArea.addTextLine( line );
		}

		mainContainer.addWidget( textArea );
		mainContainer.addWidget( factory.makeButton( "Close", closelLis ) );
		window.pack();

		return window;
	}

	public Window makeWaitStartDialog( final MainMenuDisplay mainMenu )
	{
		Window window = factory.makeDialogWindow( "Waiting", new Point( 80, 200 ) );

		Container mainContainer = window.getContentContainer();
		mainContainer.setLayoutManager( new RowLayout( false ) );

		final IButtonPressedListener cancelLis = new IButtonPressedListener() {
			public void buttonPressed( ButtonPressedEvent arg0 ) {
				// TODO: This does not work
				mainMenu.closePopup();
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

		final TextEditor addressBox = new TextEditor();
		setFont( addressBox.getAppearance(), factory.getFont1() );
//		addressBox.getAppearance().setFont( factory.getFont1() );
		addressBox.setMinSize( 50, 30 );
		addressBox.setText( "127.0.0.1" );
//		addressBox.setSelectOnFocus( true );

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
            	eventHandler.postEmpty( EventGroups.START_SINGLE_PLAYER );
            }
		};
		IButtonPressedListener hostLis = new IButtonPressedListener() {
			public void buttonPressed( ButtonPressedEvent arg0 ) {
				mainMenu.openHostGameDialog();
				eventHandler.postEmpty( EventGroups.HOST_MULTIPLAYER );
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
		IButtonPressedListener resetLis = new IButtonPressedListener() {
			public void buttonPressed( ButtonPressedEvent arg0 ) {
				eventHandler.postEmpty( EventGroups.RESET );
			}
		};
		IButtonPressedListener quitLis = new IButtonPressedListener() {
			public void buttonPressed( ButtonPressedEvent arg0 ) {
				eventHandler.postEmpty( EventGroups.EXIT );
			}
		};

		Container con = window.getContentContainer();
		con.addWidget( factory.makeButton( "Start single player game", newSingLis ) );
		con.addWidget( factory.makeButton( "Join multiplayer game", joinLis ) );
		con.addWidget( factory.makeButton( "Host new multiplayer game", hostLis ) );
		con.addWidget( factory.makeButton( "Load saved game", emptyLis ) );
		con.addWidget( factory.makeButton( "Settings", emptyLis ) );
		con.addWidget( factory.makeButton( "Instructions", instLis ) );
		con.addWidget( factory.makeButton( "Reset", resetLis ) );
		con.addWidget( factory.makeButton( "Quit", quitLis ) );

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
