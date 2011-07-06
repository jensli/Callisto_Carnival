package cc.gui.game_display;


import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glTranslated;

import java.util.Collection;

import cc.gui.BackgroundImage;
import cc.gui.Drawable;
import cc.util.Texture;
import cc.util.Util;
import cc.util.math.Vec;
import cc.util.resources.Name;
import cc.util.resources.ResourceHandler;


/**
 * The where the acctual game is rendered, the object in space.
 * @author jens
 */
public class GameScreen
{
	// The stars in the background
	private BackgroundImage backgroundImage;

	// How far away from the game the camera is
	private float focusDistance = 900.0f;

	// Variables for controling how the zooming behaves
	private double
		maxFocusDistance = 3000.0,
		minFocusDistance = 400.0,
		zoomStopSpeed = 5,
		zoomStopValue = 0.95,
		zoomSpeed = 0.0,
		zoomSpeedValue = 0.7; // How fast zoom is done when zooming


	public static final int ZOOM_IN = -1, ZOOM_OUT = 1, STOP_ZOOM = 0;

	public GameScreen()
	{
		Texture tex = ResourceHandler.get().getTexture( Name.BACKGROUND_STARS );
		backgroundImage = new BackgroundImage( 0.001, tex );
	}

	private boolean checkFocusDistance()
	{
		return focusDistance < minFocusDistance && zoomSpeed < 0.0
			|| focusDistance > maxFocusDistance && zoomSpeed > 0.0 ;
	}

	private void updateFocusDistance( double dT )
	{
		if ( checkFocusDistance() ) {
			zoomStopSpeed = zoomStopValue;
		}

		zoomSpeed *= zoomStopSpeed;
		focusDistance += dT * zoomSpeed;
	}

	public void draw( double dT, Vec focusPoint, Collection<? extends Drawable> objectList )
	{
		updateFocusDistance( dT );

		glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
		glLoadIdentity();
		glColor3f( 1.0f, 1.0f, 1.0f );

		// Viewpoint transformation

		glTranslated( -focusPoint.x, -focusPoint.y, -focusDistance );

		backgroundImage.draw( focusPoint );

//		for ( GraphicalModel model = itr.reset(); itr.hasNext(); model = itr.next() ) {
//			model.draw( null, null );
//		}
//		for ( GameObject obj : objectList ) {
//			obj.getGraphicalModel().draw( obj.getPosition(), obj.getForward() );
//		}

		for ( Drawable obj : Util.nullToEmpty( objectList ) ) {
			obj.draw();
		}
	}

	public void setZoom( int zoomState )
	{

		switch ( zoomState ) {

			case ZOOM_IN:

				if ( focusDistance > minFocusDistance ) {
					zoomStopSpeed = 1;
					zoomSpeed = -zoomSpeedValue;
				}
				break;

			case ZOOM_OUT:

				if ( focusDistance < maxFocusDistance ) {
					zoomStopSpeed = 1;
					zoomSpeed = zoomSpeedValue;
				}
				break;

			case STOP_ZOOM:

				zoomStopSpeed = zoomStopValue;
				break;

			default:
				throw new IllegalArgumentException( "setZoom called with illegal zoomState" );
		}

	}


}

