package cc.gui.models;

import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;

class Particle
{
	private float life,
		alpha,
		x,
		y,
		r, g, b,
		size;
	
	public void draw() 
	{
		glPushAttrib( GL_DEPTH_BUFFER_BIT );
		
		GL11.glDisable( GL11.GL_DEPTH_TEST );
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

		GL11.glBegin( GL11.GL_QUADS );

			if ( life <= 0 ) {
				return;
			}
			
			float scalar = alpha / 3;
			GL11.glColor4f( ( r * ( 1 - scalar ) ) + scalar, 
					( g * ( 1 - scalar ) ) + scalar, 
					( b * ( 1 - scalar ) ) + scalar, 
					alpha );
			
			float x = this.x,
				y = this.y,
				partSize = size;
			
			GL11.glTexCoord2f( 0, 0 );
			GL11.glVertex3f( x - partSize, y - partSize, -0.3f );
			
			GL11.glTexCoord2f( 1, 0 );
			GL11.glVertex3f( x + partSize, y - partSize, -0.3f );
			
			GL11.glTexCoord2f( 1, 1 );
			GL11.glVertex3f( x + partSize, y + partSize, -0.3f );
			
			GL11.glTexCoord2f( 0, 1 );
			GL11.glVertex3f( x - partSize, y + partSize, -0.3f );
		
			
		GL11.glEnd();

		glPopAttrib();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f( 1, 1, 1, 1 );

	}
	
	
}
