package cc.util;



public class CcUtil
{
	private static final String IP_REGEX = "^\\s*(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\s*$";

	public static boolean isIpAddress( String s ) {
		return s.matches( IP_REGEX );
	}


	// Elegant but less effisient that iteration version.
//	public static double sinSmoothRec( double orig, double scale, int nr )
//    {
//    	if ( nr == 0 ) return orig;
//
//    	return orig + scale*Math.sin( sinSmoothRec( orig, scale, nr - 1 ) );
//    }

	/**
	 * Enter a number orig between 0 and 1. Returns a number between 0 and 1
	 * that has been "smoothed".
	 *
	 * That is, while this is a non decreesing function of orig, it is steeper
	 * in the begining and end of the interval. "More" values in the output lies
	 * nearer to the center of the interval, an fewer close to the ends.
	 */
	public static double sinSmooth( double orig, double scale, int nr )
    {
    	double result = orig;

    	for ( int i = 0; i < nr; i++ ) {
    		result = orig + scale*Math.sin( result );
    	}

    	return result;
    }

}
