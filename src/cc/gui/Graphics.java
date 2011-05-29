package cc.gui;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.glu.GLU;

public class Graphics
{
	private final static Graphics instance = new Graphics();
	
	private int 
		screenWidth,
		screenHeight;
	
	private float screenRatio;
	
	final public static Graphics get()
	{
		return instance;
	}

	public float getScreenRatio() {
		return screenRatio;
	}

	public void pushAllGL()
	{
		glPushAttrib( GL_DEPTH_BUFFER_BIT | GL_ENABLE_BIT );
		glPushMatrix();
		glMatrixMode( GL_PROJECTION );
		glPushMatrix();
		glMatrixMode( GL_MODELVIEW );
	}
	
	public void popAllGL()
	{
		glMatrixMode( GL_PROJECTION );
		glPopMatrix();
		glMatrixMode( GL_MODELVIEW );
		glPopMatrix();
		glPopAttrib();
	}

	
	public void initGL() 
	{
		glEnable( GL_TEXTURE_2D );
//		glEnable( GL_DEPTH_TEST );  // Depth test disabled!!! To resolve blending issues.
		glDepthFunc( GL_LEQUAL );
		glShadeModel( GL_SMOOTH );
		glHint( GL_POINT_SMOOTH_HINT, GL_NICEST ); 		
		glEnable( GL_BLEND );
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		setStandardProjection();
	}
	
	public void setStandardProjection()
	{
		glMatrixMode( GL_PROJECTION );
		glLoadIdentity();
		GLU.gluPerspective( 45.0f, 800f / 600f, 100.0f, 5000.0f );
//		GLU.gluPerspective( 45.0f, screenRatio, 1.0f, 500.0f );
		glMatrixMode( GL_MODELVIEW );
		glHint( GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST ); 
	}
	

	public void enterOrthoProjection()
	{
		glPushMatrix();

		glLoadIdentity();

		glMatrixMode( GL_PROJECTION );
		glPushMatrix();
		
		// now enter orthographic projection
		glLoadIdentity();
//		glOrtho( 0.0, 1.0, 0.0, 1.0, -1.0, 1.0 );
//		glOrtho(0, 800, 600, 0, -1, 1);
		glOrtho(0, screenRatio, 0, 1, -1, 1);

		glPushAttrib( GL_DEPTH_BUFFER_BIT | GL_ENABLE_BIT );
		glDisable( GL_DEPTH_TEST );
		glDisable( GL_LIGHTING );

	}
	
	public void leaveOrthoProjection()
	{
		glPopMatrix();
		glMatrixMode( GL_MODELVIEW );
		glPopMatrix();
		glPopAttrib();
	}
	
	
	public void initDisplay( String windowTitle, boolean fullscreen, int newScreenWidth, int newScreenHeight )
	{
		screenHeight = newScreenHeight;
		screenWidth = newScreenWidth;
		screenRatio = (float) screenWidth / screenHeight;
		
		try {
			// find out what the current bits per pixel of the desktop is
			int currentBpp = Display.getDisplayMode().getBitsPerPixel();
			
			DisplayMode mode = findDisplayMode( screenWidth, screenHeight, currentBpp );
			
			// if can't find a mode, notify the user the give up
			if ( mode == null ) {
				throw new RuntimeException("Error: " + screenWidth + "x" + screenHeight + "x" 
						+ currentBpp + " display mode unavailable.");
			}
			
			// configure and create the LWJGL display
			Display.setTitle( windowTitle );
			Display.setDisplayMode( mode );
			Display.setFullscreen( fullscreen );
			
			// Seems to get blinkey with this on
//			Display.setVSyncEnabled( true );
			Display.setVSyncEnabled( false );
			
			Display.create();
			Mouse.create();
			
		} catch ( LWJGLException e ) {
			throw new RuntimeException( "Error while creating display", e );
		}
	}
	
	
	public static DisplayMode findDisplayMode( int width, int height, int bpp ) throws LWJGLException
	{
		DisplayMode[] modes = Display.getAvailableDisplayModes();
		DisplayMode mode = null;
		
		for ( int i = 0; i < modes.length; i++ ) {
			if ( ( modes[i].getBitsPerPixel() == bpp ) || ( mode == null ) ) {
				if ( ( modes[i].getWidth() == width ) && ( modes[i].getHeight() == height ) ) {
					mode = modes[i];
				}
			}
		}
		
		return mode;
	}
	
	public void dispose() {
		Display.destroy();
		Mouse.destroy();
	}

}
