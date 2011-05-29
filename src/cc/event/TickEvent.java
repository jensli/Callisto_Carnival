package cc.event;

import cc.event.handlers.EventReceiver;

public class TickEvent extends Event
{
	private static final Cathegory type = Cathegory.NETWORK;
	private double dT;
	
	{ setName( Event.TICK ); }

	public TickEvent( double dT )
	{
		this.dT = dT;
	}

	@Override
    public void dispatch( EventReceiver receiver ) {
		receiver.receiveTickEvent( this );
	}
	@Override
	public Cathegory getType() {
		return type;
	}
	public double getDt() {
		return dT;
	}
	@Override
    public TickEvent clone()
	{
		return (TickEvent) super.clone();
	}

	@Override
    public String serialize() {
	    return super.serialize() + " " + dT;
    }
	@Override
    public void deserialize( String parameters ) {
		super.deserialize( parameters );
		String parameter[] = parameters.split( " " );
		dT = Double.valueOf( parameter[3] );
    }
	
	
	
	
}
