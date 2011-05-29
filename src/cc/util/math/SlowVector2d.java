package cc.util.math;

public class SlowVector2d extends Vec
{
    static final long serialVersionUID = 2725466658325349835L;
    
    double delay;
    private Vec target;
    
    public SlowVector2d() {
    	super();
    	this.targetToValue();
    }
    
    public SlowVector2d( Tuple2d t )
    {
    	super( t );
    	this.targetToValue();
    }
    
    public SlowVector2d( double x, double y )
    {
    	super( x, y );
    	this.targetToValue();
    }
    
    
    public void targetToValue()
    {
    	target.set( this );
    }
    
    @Override
    public void set( Tuple2d t ) {
    	
    }
    
//    private void reCalculate()
//    {
//    	// TODO: Calculate and set the values of this vector
//    	// based on time and target
//    }
//    private void reCalculate( double dT )
//    {
//    	// TODO: Calculate and set the values of this vector
//    	// based on time and target
//    }
    
    public Vec getValue()
    {
    	
    	return null;
    }

}
