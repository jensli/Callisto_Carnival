package cc.event.game;

import j.util.eventhandler.GroupName;
import cc.event.Event;
import cc.event.handlers.EventReceiver;
import cc.event2.EventGroups;

public class FireEvent extends Event
{
	private boolean switchOn = false;


	@Override public String getName() {
		return Event.FIRE;
	}

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
	public void toStringBuilder( StringBuilder b ) {
    	super.toStringBuilder( b );
		b.append( " " )
			.append( switchOn );
	}


	@Override
	protected int setFields( String[] data ) {
		int index = super.setFields( data );
		this.switchOn = Boolean.parseBoolean( data[ index++ ] );
		return index;
	}


	@Override
    public void dispatch( EventReceiver receiver )
    {
		receiver.receiveFireEvent( this );
    }


	public boolean isSwitchOn()
    {
    	return switchOn;
    }

//	public void setSwitchOn( boolean switchOn )
//    {
//    	this.switchOn = switchOn;
//    }

	@Override
	public GroupName getReceiverGroup() {
		return EventGroups.FIRE;
	}
}
