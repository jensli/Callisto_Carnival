package cc.event;

import cc.event.handlers.EventReceiver;

public class ValueEvent<T> extends Event {

	private T value;

	public ValueEvent( T value ) {
		super();
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	@Override
	public void dispatch( EventReceiver receiver ) {
		throw new UnsupportedOperationException( "Not implemented" );
	}

	@Override public Cathegory getType() {
		throw new UnsupportedOperationException( "Not implemented" );
	}

}
