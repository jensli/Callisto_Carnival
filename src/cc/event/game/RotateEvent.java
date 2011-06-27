package cc.event.game;

import cc.event.Event;
import cc.event.handlers.EventReceiver;


public class RotateEvent extends Event
{
	public static final boolean
		RIGHT = false, LEFT = true;

	private boolean rotateClockwise = false;
	private static final Cathegory type = Cathegory.GAME;
	private boolean switchOn = true;


	{ setName( Event.ROTATE ); }

	public RotateEvent() {}

	public RotateEvent( int newReceiverID )
	{
		super( newReceiverID );
	}

	public RotateEvent(int newReceiverID, boolean clockwise)
	{
		this(newReceiverID);
		rotateClockwise = clockwise;
	}

	public RotateEvent(int newReceiverID, boolean clockwise, boolean switchOn )
	{
		this(newReceiverID, clockwise );
		this.switchOn = switchOn;
	}
	public RotateEvent( boolean clockwise, boolean switchOn )
	{
		this.rotateClockwise = clockwise;
		this.switchOn = switchOn;
	}

	public boolean isRotateClockwise()
	{
		return rotateClockwise;
	}

	@Override
	public void deserialize( String parameters )
	{
		super.deserialize( parameters );
		String parameter[] = parameters.split( " " );
		this.rotateClockwise = Boolean.valueOf( parameter[3] );
		this.switchOn = Boolean.valueOf( parameter[4] );
	}

	@Override
    public RotateEvent clone()
	{
		return ( RotateEvent ) super.clone();
	}

	@Override
	public String serialize()
	{
		return super.serialize() + " " + this.rotateClockwise + " " + this.switchOn;
	}

	@Override
    public void dispatch( EventReceiver receiver )
    {
		receiver.receiveRotateEvent( this );
    }

	@Override
    public Cathegory getType()
    {
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
