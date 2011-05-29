package cc.util;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cc.util.math.Vec;
import cc.util.math.VecMath;



public class GraphicsUtil
{
	public static void putVertex( Vec v ) 
	{
		glVertex2d( v.getX(), v.getY() );
	}
	
	public static void applyRotation( double v )
	{
		glRotatef( (float) Math.toDegrees( v ), 0, 0, 1 );
	}

	public static void applyRotation( Vec v )
	{
		applyRotation( v.angleNormalized() );
	}

	public static void applyRotation( Vec v, double a )
	{
		applyRotation( v.angleNormalized() + a );
	}

	public static void putVertexRelative( Vec v, Vec relative ) 
	{
		putVertex( VecMath.add( v, relative ) );
	}
	
	
	public static void putVertex( double x, double y )
	{
		glVertex2d( x, y );
	}
	
	public static boolean isAnyDown( int... keys )
	{
		for ( int code : keys ) {
			if ( Keyboard.isKeyDown( code ) ) {
				return true;
			}
		}
		
		return false;
	}

	
	public static void applyTranslation( Vec v ) 
	{
		GL11.glTranslated( v.x, v.y, 0.0 );
	}
	
	public static void setColor( Color c )
	{
		glColor4f( c.r, c.g, c.b, c.a );

//		if ( c.hasAlpha() ) {
//			glColor4f( c.r, c.g, c.b, c.a );
//		} else {
//			glColor3f( c.r, c.g, c.b );
//		}
	}
	
	private GraphicsUtil() {}
}
