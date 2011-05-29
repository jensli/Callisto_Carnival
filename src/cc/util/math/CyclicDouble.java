package cc.util.math;


/**
 * Double with a inc method that makes the double higher and higher until it
 * wraps around.
 */
public class CyclicDouble extends AbstractDouble
{
    private double 
    	value,
		maxValue;
	
	public CyclicDouble( double value, double maxValue ) {
	    this.value = value;
	    this.maxValue = maxValue;
    }

	@Override
    public double value() {
    	return value;
    }
	
	public void inc( double d ) {
		value = (value + d) % maxValue;
	}

	public void setValue( double value ) {
    	this.value = value % maxValue;
    }
	
    private static final long serialVersionUID = 1L;
}
