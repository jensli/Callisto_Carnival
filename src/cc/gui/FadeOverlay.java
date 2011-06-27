package cc.gui;

import static org.lwjgl.opengl.GL11.*;
import cc.util.Color;
import cc.util.GraphicsUtil;
import cc.util.math.InterpolatingDouble;
import cc.util.math.LinearInterpolatingDouble;

public class FadeOverlay
{
	private Color color = new Color( 1, 1, 1, 1 );
	private InterpolatingDouble alpha;

	public FadeOverlay( double speed )
	{
		alpha = new LinearInterpolatingDouble( 0, 1, speed );
	}

	public FadeOverlay()
	{
		this( 0.002 );
	}

	public void update( double dT )
	{
		alpha.update( dT );
		color.a = (float) alpha.value();
	}

	public void draw()
	{
		if ( alpha.isAtTarget() ) return;

		final float maxX = Graphics.get().getScreenRatio();
		Graphics.get().enterOrthoProjection();

		glPushAttrib( GL_TEXTURE_BIT );
		glDisable( GL_TEXTURE_2D );

		glBegin( GL_QUADS );
			GraphicsUtil.setColor( color );
			glVertex2f( 0, 0 );
			glVertex2f( 0, 1 );
			glVertex2f( maxX, 1 );
			glVertex2f( maxX, 0 );
		glEnd();

		glPopAttrib();

		Graphics.get().leaveOrthoProjection();
	}

	public void startFade() {
		alpha.restart();
	}
}
