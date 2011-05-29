package cc.event;



public abstract class StandardValueEvent<TYPE> extends StandardEvent 
{
	private TYPE value;
	
	public TYPE getValue() {
		return value;
	}
	public void setValue( TYPE value ) {
		this.value = value;
	}
	
	@Override
    @SuppressWarnings("unchecked")
	public StandardValueEvent<TYPE> clone()
	{
		return (StandardValueEvent<TYPE>) super.clone();
	}
	

}
