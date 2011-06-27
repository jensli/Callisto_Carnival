package cc.gui.menu_old_system;


import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import cc.gui.Graphics;
import cc.util.math.Vec;


public class Test
{

	public static final String GAME_TITLE = "My Game";
	private static final int FRAMERATE = 60;
	private static boolean finished;
	private static float angle;

	public String str = "";
	public boolean b1 = false,
		b2 = true,
		b3 = false;

	private Menu menu;
	private MenuInputHandler inputHandler;

	private boolean rootOn = true;

	public static void main( String[] args ) throws InterruptedException
	{
		boolean fullscreen = ( args.length == 1 && args[0].equals( "-fullscreen" ) );
		Test test = null;

		try {
			test = new Test();
			test.init( fullscreen );
			test.run();
		} catch ( LWJGLException e ) {
			e.printStackTrace( System.err );
			Sys.alert( GAME_TITLE, "An error occured and the game will exit." );
		} finally {
			if ( test != null ) {
				test.cleanup();
			}
		}
	}

	public Menu makeMenu()
	{
		Menu newMenu = new Menu( "The Menu:" );
		newMenu.setPos( new Vec( 0.4, 0.6 ) );

		BooleanEntry e1 = new BooleanEntry( "Wooo" );
		e1.setValue( false );
		e1.setListener( new MenuListener() {
			public void informChange( Object newValue ) {
				b1 = (Boolean) newValue;
				System.out.println( "b1: " + b1 );
			}
		});
		newMenu.addEntry( e1 );

		BooleanEntry e2 = new BooleanEntry( "Waa" );
		e2.setValue( true );
		e2.setListener( new MenuListener() {
			public void informChange( Object newValue ) {
				b2 = (Boolean) newValue;
				System.out.println( "b2: " + b2 );
			}
		});
		newMenu.addEntry( e2 );

		BooleanEntry e3 = new BooleanEntry( "Tjo" );
		e3.setValue( false );
		e3.setListener( new MenuListener() {
			public void informChange( Object newValue ) {
				b3 = (Boolean) newValue;
				System.out.println( "b3: " + b3 );
			}
		});
		newMenu.addEntry( e3 );

		ActionEntry e4 = new ActionEntry( "Do!" );
		e4.setListener( new MenuListener() {
			public void informChange( Object newValue ) {
				rootOn = !rootOn;
			}
		});
		newMenu.addEntry( e4 );

//		InputEntry e5 = new InputEntry( "Edit:" );
//		e5.setValue( "woooo?" );
//		e5.setListener( new MenuListener() {
//			public void informChange( Object newValue ) {
//				;
//			}
//		});
//		newMenu.addEntry( e5 );

		return newMenu;
	}


	public void cleanup()
	{
		Display.destroy();
	}

	private void init( boolean fullscreen ) throws LWJGLException
	{
//		Display.setTitle( GAME_TITLE );
//		Display.setFullscreen( fullscreen );
//		Display.setVSyncEnabled( true );
//		Display.create();
		Graphics.get().initDisplay( "Callisto Carnival", false, 800, 600 );
		Graphics.get().initGL();

		menu = makeMenu();
		inputHandler = new MenuInputHandler( menu );
	}

	private void render()
	{
		// clear the screen
		GL11.glClear( GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT );
		GL11.glLoadIdentity();

		GL11.glDisable( GL11.GL_TEXTURE_2D );

		// center square according to screen size
		GL11.glPushMatrix();

//		GL11.glTranslatef( 700, 525, 0 );
		GL11.glTranslatef( 0, 0, -900 );
		GL11.glRotatef( angle, 0, 0, 1 );

		// render the square
		final double s = 2;

		GL11.glColor3f( 1, 0, 0 );
		GL11.glBegin( GL11.GL_QUADS );
			GL11.glVertex2d( -50*s, -50*s );
			GL11.glVertex2d( 50*s, -50*s );
			GL11.glVertex2d( 50*s, 50*s );
			GL11.glVertex2d( -50*s, 50*s );
		GL11.glEnd();

		GL11.glPopMatrix();

		GL11.glEnable( GL11.GL_TEXTURE_2D );

		menu.draw();
	}

	private void run() throws InterruptedException
	{
//		menu.draw();

		while ( !finished ) {

			Display.update();

			if ( Display.isCloseRequested() ) {

				finished = true;

			} else if ( Display.isActive() ) {

				logic();
				render();
				Display.sync( FRAMERATE );

			} else {

				Thread.sleep( 100 );
				logic();
				if ( Display.isVisible() || Display.isDirty() ) {
					render();
				}
			}
		}
	}

	private void logic()
	{
		if ( Keyboard.isKeyDown( Keyboard.KEY_ESCAPE ) ) {
			finished = true;
		}

		menu.update( 100 );
		inputHandler.update();

//		while ( Keyboard.next() ) {
//			if ( Keyboard.getEventKeyState() ) {
//				char ch = Keyboard.getEventCharacter();
//				if ( Character.isISOControl( ch ) ) {
//					System.out.println( "ISO controll." );
//				} else {
//					System.out.println( "Char: " + Keyboard.getEventCharacter() );
//				}
//			}
//		}

		// Rotate the square
		if ( rootOn ) {
			angle += 2.0f % 360;
		}
	}
}
