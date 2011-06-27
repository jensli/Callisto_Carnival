package cc.event.game;

import cc.event.Event;
import cc.event.handlers.EventReceiver;

// Serialization not fixed!!! Has no data, no serializ!

public class FireEvent extends Event
{
	private static final Cathegory type = Cathegory.GAME;
	private boolean switchOn = false;

	{ setName( Event.FIRE ); }

	public FireEvent(int newReceiverID)
	{
		super();
	}

	public FireEvent( int newReceiverID, boolean switchOn )
	{
		this(newReceiverID);
		this.switchOn = switchOn;
	}
	public FireEvent( boolean switchOn )
	{
		this.switchOn = switchOn;
	}

    @Override
    public FireEvent clone()
    {
        FireEvent copy = (FireEvent)super.clone();
        return copy;
    }

    @Override
	public String serialize() {
		return super.serialize() + " " + this.switchOn;
	}

	@Override
	public void deserialize(String parameters)
	{
		super.deserialize(parameters);
		String parameter[] = parameters.split( " " );
		this.switchOn = Boolean.valueOf( parameter[3] );
	}

	@Override
    public void dispatch( EventReceiver receiver )
    {
		receiver.receiveFireEvent( this );
    }

	@Override
    public Cathegory getType() {
    	return type;
    }

	public boolean isSwitchOn()
    {
    	return switchOn;
    }

	public void setSwitchOn( boolean switchOn )
    {
    	this.switchOn = switchOn;
    }

}
