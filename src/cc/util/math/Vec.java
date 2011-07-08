package cc.util.math;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import j.util.functional.Action1;

/**
 * Extents the util.Vector2d class, providing some additional functionality.
 * @author Jens
 *
 */
public class Vec extends VecMathVector2d
{
//	public static Vec tempVector = new Vec();
    static final long serialVersionUID = 1L;

    public static final Vec
    	LEFT = new Vec( 1, 0 ),
    	RIGHT = new Vec( -1, 0 ),
    	UP = new Vec( 0, 1 ),
    	DOWN = new Vec( 0, -1 );

    public static Vec makeUp() {
    	return UP.copy();
    }
    public static Vec makeDown() {
    	return DOWN.copy();
    }
    public static Vec makeLeft() {
    	return LEFT.copy();
    }
    public static Vec makeRight() {
    	return RIGHT.copy();
    }

   	// Dangerous, too easy to use this in two different places
    // in the same time
//	public static Vec getTemp() {
//		return tempVector;
//	}
//	public static Vec getTemp( double x, double y ) {
//		tempVector.set( x, y );
//		return tempVector;
//	}


    private static class RotateAction implements Action1<Vec>
    {
    	private final Vec dir;

		public RotateAction( Vec dir ) {
	        this.dir = dir;
        }

		@Override
        public void run( Vec arg ) {
			arg.rotate( dir );
        }
    }

//    public final static ActionD<Vec> RotateAction = new ActionD<Vec>() {
//		@Override
//		public void run( Vec vec, Vec dir ) {
//			vec.rotate( dir );
//		}
//	};


    public Vec copy()
    {
    	return new Vec( this );
    }

	public static Action1<Vec> rotateAction( final Vec dir )
	{
		return new RotateAction( dir );
	}

	public Vec()
	{
		;
	}

	public Vec(double x, double y)
	{
		super(x, y);
	}

	public Vec( Tuple2d t )
	{
		super( t );
	}


	public final Vec normalized()
	{
		Vec v = new Vec( this );
		v.normalize();
		return v;
	}

	/**
	 * Rotates this vector conter clockwise.
	 *
	 * THIS METHOD ARE TO BE USED ON NORMALIZED VECTORS ONLY!
	 *
	 * @param a angel to rotate (radians)
	 */
	public void rotateNormalized(double a)
	{
		double a2 = this.angle() + a;

		set( Math.cos( a2 ), Math.sin( a2 ) );
	}


	/**
	 * Rotates this vector counter clockwise
	 * @param a angel to rotate (radians)
	 */
//	public void rotate(double a)
//	{
//		double l = this.length();
//		double vDot = this.x / l;
//		double ret = Math.acos( vDot );
//
//		if ( this.y < 0.0 ) {
//			ret = 2 * Math.PI - ret;
//		}
//
//		double a2 = this.angle() + a;
//
//		this.set( l*Math.cos( a2 ), l*Math.sin( a2 ) );
//	}


	/**
	 * Rotates vector by angle determened by v.
	 *
	 * Rotation angle is equal to arcsin(v.y) = arccos(v.x)
	 *
	 * v MUST BE A NORMAL VECTOR!
	 */
	public final void rotate( Vec v )
	{
		rotate( v.x, v.y );
	}


	/**
	 * Rotates vector by angle determened by ax and ay.
	 *
	 * Rotation angle is equal to arcsin(ay) = arccos(ax)
	 *
	 * ax and ay must satisfy arcsin(ay) = arccos(ax)
	 */
	public final void rotate( final double ax, final double ay )
	{
		final double oldX = x;

		x = x*ax - y*ay;
		y = oldX*ay + y*ax;
	}

	/**
	 * Rotates vector by d radians
	 */
	public final void rotate( final double a )
	{
		rotate( cos( a ), sin( a ) );
//		final double
//			sinA = sin( a ),
//			cosA = cos( a ),
//			oldX = x;
//
//		this.x = x*cosA - y*sinA;
//		this.y = oldX*sinA + y*cosA;
	}

	public final Vec rotated( double a )
	{
		Vec v = new Vec( this );
		v.rotate( a );
		return v;
	}

	public final Vec rotated( Vec dir )
	{
		Vec v = new Vec( this );
		v.rotate( dir );
		return v;
	}

	public final Vec negated()
	{
		return new Vec( -x, -y );
	}

	/**
	 * @return the from positive x axis to this vector, counter clockwise
	 *         (radians)
	 */

	public double angle()
	{
	      double vDot = this.x / this.length();

//	      if( vDot < -1.0) vDot = -1.0;
//	      if( vDot >  1.0) vDot =  1.0;

	      double ret = Math.acos( vDot );

	      if ( this.y < 0.0 ) {
	    	  ret = 2*PI - ret;
	      }

	      return ret;
	}

	public final void addScaled( Vec v, double scale )
	{
		x += v.x*scale;
		y += v.y*scale;
	}

	public double angleNormalized()
	{
		double ret = Math.acos( this.x );

		if ( this.y < 0.0 ) {
			ret = 2*PI - ret;
		}

		return ret;
	}

	/**
	 * Returns the distance between this vector and v, interpreted as coordinates.
	 */
	public double distance( Vec v )
	{
//		double dx = this.x - v.x,
//			dy = this.y - v.y;
//		return Math.sqrt(dx*dx + dy*dy );
		return VecMath.distance( this, v );
	}

	@Override
    public String toString()
	{
		return "" + x + " " + y;
	}

	public void fromString(String param)
	{
		String[] params = param .split(" ");
		this.x = Double.valueOf(params[0]);
		this.y = Double.valueOf(params[1]);
	}
}
