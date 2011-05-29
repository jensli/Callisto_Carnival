package cc.gui.models;

import static cc.util.GraphicsUtil.*;
import static java.lang.Math.*;
import static org.lwjgl.opengl.GL11.*;
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
	
	private Quad shape;
	
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
		
		Quad q = new Quad( shape );
		
		final double dist = v.length();
		q.p3.scale( length / dist );
		q.p4.scale( length / dist );
		
		v.normalize();
		q.rotate( v );
		
		glPushAttrib( GL_TEXTURE_BIT );
		
		glDisable( GL_TEXTURE_2D );
		
		glPushMatrix();
		
		applyTranslation( getObject().getPos() );

		glBegin( GL_POLYGON );
			
			GraphicsUtil.setColor( color );
			putVertex( q.p1 );
			putVertex( q.p2 );
			
			GraphicsUtil.setColor( transColor );
			putVertex( q.p3 );
			putVertex( q.p4 );
			
		glEnd();
		
		glPopAttrib();
		
		glPopMatrix();
	}
	
}
