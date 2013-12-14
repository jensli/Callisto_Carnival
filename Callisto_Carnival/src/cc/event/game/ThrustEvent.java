
package cc.event.game;

import j.util.eventhandler.GroupName;
import cc.event.Event;
import cc.event.handlers.EventReceiver;
import cc.event2.EventGroups;

public class ThrustEvent extends Event
{
	private  boolean switchOn;

	public ThrustEvent() {}

	public ThrustEvent( boolean switchOn ) {
		this.switchOn = switchOn;
	}
	public ThrustEvent( int receiverID, boolean switchOn ) {
		super( receiverID );
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
		int i = super.setFields( data );
		this.switchOn = Boolean.parseBoolean( data[ i++ ] );
		return i;
	}


	@Override
	public void dispatch( EventReceiver receiver ) {
		receiver.receiveThrustEvent( this );
	}
	public boolean isSwitchOn() {
		return switchOn;
	}
	public void setSwitchOn( boolean switchOn ) {
		this.switchOn = switchOn;
	}


	@Override
	public GroupName getReceiverGroup() {
		return EventGroups.THRUST;
	}

	@Override public String getName() {
		return Event.THRUST;
	}
}
