package cc.gui;

import static org.lwjgl.opengl.GL11.*;
import cc.util.Texture;
import cc.util.math.Vec;


/**
 * A background image, moving slower than the rest of the objects to simulate
 * distance. 
 */
public class BackgroundImage
{
	private Texture texture;
	private float scrollSpeed;
//	private float zOffset = -0.3f;
	
	public BackgroundImage( double scrollSpeed, Texture texture )
    {
	    this.texture = texture;
	    this.scrollSpeed = (float) scrollSpeed;
    }

	public void draw( Vec focusPoint )
	{
		texture.bind();
		
		float 
			maxX = Graphics.get().getScreenRatio(),
			x = (float) focusPoint.x * scrollSpeed,
			y = (float) focusPoint.y * scrollSpeed;
		
		Graphics.get().enterOrthoProjection();

		glBegin( GL_QUADS );
			glTexCoord2f( x, y );
			glVertex2f( 0, 0 );
			
			glTexCoord2f( x, 1 + y );
			glVertex2f( 0, 1 );
			
			glTexCoord2f( 1 + x, 1 + y );
			glVertex2f( maxX, 1 );
			
			glTexCoord2f( 1 + x, y );
			glVertex2f( maxX, 0 );
		glEnd();
		
//		GL11.glBegin(GL11.GL_QUADS);
//		GL11.glTexCoord2f( x, y );
//		GL11.glVertex3f( 0, 0, zOffset );
//		
//		GL11.glTexCoord2f( x, 1 + y );
//		GL11.glVertex3f( 0, 1, zOffset );
//		
//		GL11.glTexCoord2f( 1 + x, 1 + y );
//		GL11.glVertex3f( maxX, 1, zOffset );
//		
//		GL11.glTexCoord2f( 1 + x, y );
//		GL11.glVertex3f( maxX, 0, zOffset );
//		GL11.glEnd();
		
		Graphics.get().leaveOrthoProjection();
		
	}
}
