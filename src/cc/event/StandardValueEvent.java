package cc.event;

import cc.event.handlers.EventReceiver;



public class StandardValueEvent<TYPE> extends StandardEvent
{
	private TYPE value;

	public StandardValueEvent() {}

	public StandardValueEvent( TYPE value ) {
		this.value = value;
	}


	public TYPE getValue() {
		return value;
	}
	public void setValue( TYPE value ) {
		this.value = value;
	}



	@Override
	public void dispatch( EventReceiver receiver ) {
		throw new UnsupportedOperationException( "Can not call dispatch on this event" );
	}

	@Override
    @SuppressWarnings("unchecked")
	public StandardValueEvent<TYPE> clone()
	{
		return (StandardValueEvent<TYPE>) super.clone();
	}


}
