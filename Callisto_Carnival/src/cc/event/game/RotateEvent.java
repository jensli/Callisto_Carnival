package cc.event.game;

import j.util.eventhandler.GroupName;
import cc.event.Event;
import cc.event.handlers.EventReceiver;
import cc.event2.EventGroups;


public class RotateEvent extends Event
{
	private boolean rotateClockwise = false;
	private boolean switchOn = true;


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
	public void toStringBuilder( StringBuilder b ) {
    	super.toStringBuilder( b );
		b.append( " " )
			.append( rotateClockwise ).append( " " )
			.append( switchOn );
	}




	@Override
	protected int setFields( String[] data ) {
		int i = super.setFields( data );
		this.rotateClockwise = Boolean.parseBoolean( data[ i++ ] );
		this.switchOn = Boolean.parseBoolean( data[ i++ ] );
		return i;
	}

	@Override
    public void dispatch( EventReceiver receiver )
    {
		receiver.receiveRotateEvent( this );
    }

	public boolean isSwitchOn()
    {
    	return switchOn;
    }

	public void setSwitchOn( boolean switchOn )
    {
    	this.switchOn = switchOn;
    }


	@Override
	public GroupName getReceiverGroup() {
		return EventGroups.ROTATE;
	}

	@Override public String getName() {
		return Event.ROTATE;
	}


}
