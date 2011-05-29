package cc.util;

import static java.lang.Math.sin;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class Util
{
	public static void dispose( Disposable d )
	{
		if ( d != null ) {
			d.dispose();
		}
	}
	
	public static void sleep( int ms ) {
		try {
			Thread.sleep( ms );
		} catch (InterruptedException e) { ; }
	}
	
	private static final String IP_REGEX = "^\\s*(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\s*$";

	public static boolean isIpAddress( String s ) {
		return s.matches( IP_REGEX );
	}
	
	public static void close( Closeable c )
	{
		if ( c == null ) return;
			
		try {
			c.close();
		} catch ( IOException e ) {
			// Ignore
		}
	}
	
	public static double clamp( double value, double low, double high )
	{
		if ( value < low ) return low;
		if ( value > high ) return high;
		
		return value;
	}
	
	public static double clampHigh( double value, double limit )
	{
		return Math.min( value, limit );
	}
	
	public static Thread runInNewThread( String name, final Runnable r )
	{
		Thread t = new Thread( r, name );
		t.start();
		return t;
	}
	
	public static <T extends Comparable<T>> int compare(T o1, T o2) {
		return o1.compareTo( o2 ); 
	}

	
	public static void verifyArg( boolean expr, String message )
	{
		if ( !expr ) {
			throw new IllegalArgumentException( message );
		}
	}

	public static void verifyNotNull( Object o, String message )
	{
		if ( o == null ) {
			throw new NullPointerException( message + " must not be null" );
		}
	}
	
	public static void verifyArgInRange( double arg, double l1, double l2, String name )
	{
		if ( !inRange( arg, l1, l2 ) ) {
			throw new IllegalArgumentException( "Argument " + name + " must be between " + l1 + " and " + l2 );
		}
	}
	
	public static boolean inRange( final double d, final double low, final double high )
	{
		return low <= d && d <= high; 
	}

	public static double sinSmoothRec( double orig, double scale, int nr )
    {
    	if ( nr == 0 ) return orig;
    	
    	return orig + scale*sin( sinSmoothRec( orig, scale, nr - 1 ) );
    }

	public static double sinSmoothIter( double orig, double scale, int nr )
    {
    	double result = orig;
    	
    	for ( int i = 0; i < nr; i++ ) {
    		result = orig + scale*sin( result );
    	}
    	
    	return result;
    }
	
	
	@SuppressWarnings("unchecked")
	public static <T> Collection<T> nullToEmpty( Collection<T> coll )
	{
		return coll == null ? Collections.EMPTY_LIST : coll; 
	}
	
}
