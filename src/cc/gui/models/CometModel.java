package cc.gui.models;

import static cc.util.GraphicsUtil.applyTranslation;
import static cc.util.GraphicsUtil.putVertex;
import static java.lang.Math.PI;
import static java.lang.Math.sin;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_BIT;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import cc.game.GameObject;
import cc.util.Color;
import cc.util.GraphicsUtil;
import cc.util.math.Quad;
import cc.util.math.Vec;
import cc.util.math.VecMath;

public class CometModel extends GraphicalModel
{

	private final Color
		color = new Color( 1f, 1f, 1f, 0.7f ),
		transColor = Color.WHITE_TRANS;

	private double
	length = 300000,
		tailAngle = 0.1 * PI;

	private Quad shape,
		temp = new Quad();

	private GameObject mother;

	public CometModel( GameObject o, GameObject mother )
	{
		super( o );
		shape = makeQuad( o.getPhysModel().getRadius(), tailAngle );
		this.mother = mother;
	}

	public GameObject getMother()
	{
    	return mother;
    }

	public void setMother( GameObject mother )
	{
    	this.mother = mother;
    }


	public static Quad makeQuad( double radius, double tailAngle )
	{
//		final double radius  = o.getPhysModel().getRadius();
		final Quad q = new Quad();

		q.p1 = new Vec( 0, radius );
		q.p2 = new Vec( 0, -radius );
		q.p4 = new Vec( 1, sin( tailAngle ) );
		q.p3 = new Vec( 1, -sin( tailAngle ) );

		return q;
	}


	@Override
	public void draw( Vec OLD_pos, Vec old_forward )
	{

		Vec v = VecMath.sub( getObject().getPos(), mother.getPos() );

		temp.set( shape );

		final double dist = v.length();
		temp.p3.scale( length / dist );
		temp.p4.scale( length / dist );

		v.normalize();
		temp.rotate( v );

		glPushAttrib( GL_TEXTURE_BIT );

		glDisable( GL_TEXTURE_2D );

		glPushMatrix();

		applyTranslation( getObject().getPos() );

		glBegin( GL_POLYGON );

			GraphicsUtil.setColor( color );
			putVertex( temp.p1 );
			putVertex( temp.p2 );

			GraphicsUtil.setColor( transColor );
			putVertex( temp.p3 );
			putVertex( temp.p4 );

		glEnd();

		glPopAttrib();

		glPopMatrix();
	}

}
