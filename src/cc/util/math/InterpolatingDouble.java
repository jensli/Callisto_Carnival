package cc.util.math;
/**
 * Contains a value and a target for that value. Each time it is updated it
 * approaches the target, in a way that is defined by subclasses.
 */
public abstract class InterpolatingDouble extends AbstractDouble
{
    private static final double EPSILON = 0.0001;
	private double value, targetValue, startValue;
	
	public InterpolatingDouble( double value ) {
		this( value, Double.NaN );
	}
	public InterpolatingDouble( double value, double startValue ) {
		this.value = value;
		this.targetValue = value;
		this.startValue = startValue;
	}
	
	public abstract void update( double dT );

	public double targetValue() {
		return targetValue;
	}
	public void setTargetValue( double targetValue ) {
		this.targetValue = targetValue;
	}
	@Override
    public double value() {
		return value;
	}
	
	public void restart() {
		value = startValue;
	}
	
	public void setValue( double value ) {
		this.value = value;
	}
	
	public void changeValue( double delta ) {
		value += delta;
	}
	
	public boolean isAtTarget() {
		return Math.abs( value - targetValue ) < EPSILON;
	}
	
    private static final long serialVersionUID = 1L;
}
