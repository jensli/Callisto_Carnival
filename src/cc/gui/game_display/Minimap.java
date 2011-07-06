package cc.gui.game_display;

import static java.lang.Math.abs;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_POINT_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPointSize;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.Collection;

import org.lwjgl.opengl.GL11;

import cc.game.GameObject;
import cc.game.Player;
import cc.game.objects.Ship;
import cc.gui.Graphics;
import cc.gui.models.GraphicalModel;
import cc.util.GraphicsUtil;
import cc.util.Texture;
import cc.util.math.Vec;
import cc.util.resources.Name;
import cc.util.resources.ResourceHandler;


public class Minimap
{
	private Player focusPlayer;

	private Texture texture;
	private Vec pos;
	private float width, height,
		halfWidth, halfHeight;

	private final float VIEW_DIST = 3800,
		MINIFY_FACTOR;


	public Minimap()
	{
		pos = new Vec( 0.16, 0.825 );

		height = 0.30f;
    	halfHeight = 0.5f*height;
		width = 0.30f;
		halfWidth = 0.5f*width;

		MINIFY_FACTOR = 0.43f*height/VIEW_DIST;

		texture = ResourceHandler.get().getTexture( Name.RADAR );

		// Dummy objs to avoid nullpex if the real ones havent arrived yet
		focusPlayer = new Player( "Dummy", -1 );
		focusPlayer.setControlledObject( new Ship("Dummy minimap object", null, null ) );
	}


	public void draw( Vec focusPoint, Collection<GameObject> objectList )
	{
//		Vector2d focusPoint = focusPlayer.getControlledObject().getPosition();

		Graphics.get().enterOrthoProjection();

		texture.bind();
		GL11.glColor4f( 1, 1, 1, 0.7f );

		GL11.glTranslatef( (float)pos.x, (float)pos.y, 0 );


		// Draw radar background
		glBegin( GL_QUADS );
			glTexCoord2f( 0, 0 );
			glVertex2f( -halfWidth, -halfHeight );
			glTexCoord2f( 0, 1 );
			glVertex2f( -halfWidth, halfHeight );
			glTexCoord2f( 1, 1 );
			glVertex2f( halfWidth, halfHeight );
			glTexCoord2f( 1, 0 );
			glVertex2f( halfWidth, -halfHeight );
		glEnd();

		glDisable( GL_TEXTURE_2D );
		glEnable( GL_POINT_SMOOTH );


		Vec v = new Vec();

		// Draw dots on radar
		for ( GameObject obj : objectList ) {

			GraphicalModel model = obj.getGraphicalModel();
			float x = (float) (model.getPosition().x - focusPoint.x),
			y = (float) (model.getPosition().y - focusPoint.y);

			v.setSub( obj.getPos(), focusPoint );

			// Continue if object is to far away to been seen on the minimap
			if ( abs(v.x) > VIEW_DIST || abs(v.y) > VIEW_DIST ||
					obj.getPos().distance( focusPoint ) > VIEW_DIST) {
				continue;
			}

			v.scale( MINIFY_FACTOR );

			x *= MINIFY_FACTOR;
			y *= MINIFY_FACTOR;

			double radius = model.getRadius();

			// Set color and size depending on obj radius
			if ( radius < 8 ) {
				continue;  // To small obj, do not draw
//				GL11.glColor3f( 0.65f, 0.0f, 0.0f );
//				GL11.glPointSize( 1 );
			} else if ( radius < 35 ) {
				glColor3f( 0.9f, 0.1f, 0.1f );
				glPointSize( 2 );
			} else if ( radius < 150 ) {
				glColor3f( 0.1f, 0.9f, 0.9f );
				glPointSize( 4 );
			} else {
				glColor3f( 0.9f, 0.9f, 0.1f );
				glPointSize( 8 );
			}

			// Draw the dot
			glBegin( GL_POINTS );
				GraphicsUtil.putVertex( v );
//				GL11.glVertex2f( x, y );
			glEnd();

			glColor3f( 1.0f, 1.0f, 1.0f );
		}

		Graphics.get().leaveOrthoProjection();
	}

//	private void drawMini( SpaceObject obj )
//	{
//
//	}


//	@Override
//    public void receiveEvent( Event event )
//    {
//		event.route( this );
//    }
//
//	@Override
//    public void receiveGuiEvent( GuiEvent event )
//    {
//		focusPlayer = event.getFocusPlayer();
//		objectList = event.getObjectList();
//    }


	public void setHeight( double height )
    {
    	this.height = (float)height;
    	halfHeight = this.height;
    }


	public void setWidth( double width )
    {
    	this.width = (float)width;
    	halfWidth = this.width;
    }

}
