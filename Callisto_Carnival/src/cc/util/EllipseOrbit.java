package cc.util;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import j.util.util.Asserts;
import cc.util.math.Vec;
/**
 * This class describes an elliptical orbit. It stores data about the path
 * and calculates the pos of the body from how much of a cycle it has completed.
 */
public class EllipseOrbit
{
	private final Vec
		center = new Vec(),
		dir = new Vec(),
		pos = new Vec();

	private double
		ecce,
		major,
		minor,
		smoothFactor;

	public EllipseOrbit( Vec center, Vec dir, double dist )
	{
		this( center, dir, dist, dist );
    }

	public EllipseOrbit( Vec center, Vec dir, double perihelionDist, double aphelionDist )
	{
		Asserts.verifyArgInRange( ecce, 0, 0.9999999, "ecce" );

		this.center.set( center );
	    this.major = (perihelionDist + aphelionDist) / 2;
	    this.ecce = (aphelionDist - perihelionDist) / (2*major);
	    this.dir.set( dir );

	    this.smoothFactor = ecce;
	    this.minor = major * sqrt( 1 - ecce*ecce );
	}

//	public EllipseOrbit( Vec center, Vec dir, double periDist, double ecce )
//	{
//		Util.verifyArgInRange( ecce, 0, 0.9999999, "ecce" );
//
//		this.pos = center;
//	    this.ecce = ecce;
//	    this.dir = dir;
////	    this.periDist = periDist;
//
//	    major = 2 * periDist /( 1 - ecce );
//	    minor = major * sqrt( 1 - ecce*ecce );
//    }


	/**
	 * Returns the position on this ellipse corresponding to how large part
	 * of one cycle it has completed (d)
	 */
//	public Vec calcVector( double d )
//    {
//    	final double
//    		V_in = 2*PI*d,
//			V_eff = Util.sinSmoothIter( V_in, smoothFactor, (int) round( smoothFactor * 5 ) ),
//	    	x = cos( V_eff ) * major - major*ecce,  // Last term is the correction to place the focus right
//	    	y = sin( V_eff ) * minor;
//
//	    Vec vec = new Vec( x, y );
//	    vec.rotate( dir );
//	    vec.add( center );
//
//	    return vec;
//    }

	public Vec calcVector( double d )
    {
	    Vec vec = new Vec();
	    setProgress( d, vec );
	    return vec;
    }


	public void setProgress( double d ) {
		setProgress( d, pos );
	}

	public void setProgress( double d, Vec result )
	{
    	final double
			V_in = 2*PI*d,
			V_eff = CcUtil.sinSmooth( V_in, smoothFactor, (int) round( smoothFactor * 5 ) ),
	    	x = cos( V_eff ) * major - major*ecce,  // Last term is the correction to place the focus right
	    	y = sin( V_eff ) * minor;

    	result.set( x, y );
	    result.rotate( dir );
	    result.add( center );
	}

	public Vec getCenter() {
    	return center;
    }
	public void setCenter( Vec center ) {
    	this.center.set( center );
    }
	public Vec getDir() {
    	return dir;
    }

	public Vec getPos() {
		return pos;
	}

	public void setDir( Vec dir ) {
    	this.dir.set( dir );
    }
	public double getEcce() {
    	return ecce;
    }
	public double getMajor() {
    	return major;
    }
	public double getMinor() {
    	return minor;
    }

}
