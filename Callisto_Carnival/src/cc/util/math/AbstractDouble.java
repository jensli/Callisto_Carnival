package cc.util.math;

public abstract class AbstractDouble extends Number
{
    private static final long serialVersionUID = 1L;

	public abstract double value();

	@Override
    public long longValue() {
	    return (long) value();
    }

	@Override
    public int intValue() {
	    return (int) value();
    }

	@Override
    public float floatValue() {
	    return (float) value();
    }

	@Override
    public double doubleValue() {
	    return value();
    }
}
