package cc.util.math;

public class LinearInterpolatingDouble extends InterpolatingDouble
{
	private double speed;
	
	public LinearInterpolatingDouble( double value, double speed )
    {
	    super( value );
	    this.speed = speed;
    }

	public LinearInterpolatingDouble( double value, double startValue, double speed )
    {
	    super( value, startValue );
	    this.speed = speed;
    }

	@Override 
	public void update( double dT ) 
	{
		double 
			t = speed * dT,
			delta = value() - targetValue();
		
		if ( Math.abs( delta ) > t ) {
			changeValue( delta > 0.0 ? -t : t );
		} else {
			setValue( targetValue() );
		}
    }
	
    private static final long serialVersionUID = 1L;
}
