package cc.util;

public class Color
{
	public float r, g, b, a = 1;

	public boolean hasAlpha = false;

	public static final Color
		RED = new Color( 1, 0, 0 ),
		GREEN = new Color( 0, 1, 0 ),
		BLUE = new Color( 0, 0, 1 ),
		WHITE_TRANS = new Color( 1, 1, 1, 0 );

	private static final Color
		WHITE = new Color( 1, 1, 1 );


	public static Color getWhite() {
		return new Color( WHITE );
	}

	private Color( float r, float g, float b, float a, boolean hasAlpha )
    {
	    this.r = r;
	    this.g = g;
	    this.b = b;
	    this.a = a;

//	    hasAlpha = true;
    }

	public void set( Color other )
	{
	    this.r = other.r;
	    this.g = other.g;
	    this.b = other.b;
	    this.a = other.a;
	}

	public Color( float r, float g, float b )
    {
		this( r, g, b, 1.0f, false );
    }

	public Color( float r, float g, float b, float a )
    {
		this( r, g, b, a, true );
    }

	public Color( Color c )
	{
		set( c );
	}

	public float getR() {
		return r;
	}

	public void setR( float r ) {
		this.r = r;
	}

	public float getG() {
		return g;
	}

	public void setG( float g ) {
		this.g = g;
	}

	public float getB() {
		return b;
	}

	public void setB( float b ) {
		this.b = b;
	}

	public float getA() {
		return a;
	}

	public void setA( float a ) {
		this.a = a;
	}




//	public boolean hasAlpha() {
//    	return hasAlpha;
//    }

//	public void scale( double scale )
//	{
//	    this.r *= scale;
//	    this.g *= scale;
//	    this.b *= scale;
//	}
//
//	public Color scaled( double scale )
//	{
//		final float s = (float) scale;
//
//		if ( hasAlpha() ) {
//			return new Color( s*r, s*g, r*b, r*a );
//		} else {
//			return new Color( s*r, s*g, r*b );
//		}
//	}
}
