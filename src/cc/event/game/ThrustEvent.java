
package cc.event.game;

import j.util.eventhandler.GroupName;
import cc.event.Event;
import cc.event.handlers.EventReceiver;
import cc.event2.EventGroups;

public class ThrustEvent extends Event
{
	private static final Cathegory type = Cathegory.GAME;
	private  boolean switchOn;

	{ setName( Event.THRUST ); }

	public ThrustEvent() {}

	public ThrustEvent( boolean switchOn ) {
		this.switchOn = switchOn;
	}
	public ThrustEvent( int receiverID, boolean switchOn ) {
		super( receiverID );
		this.switchOn = switchOn;
	}

	@Override
	public void deserialize(String parameters)
	{
		super.deserialize(parameters);
		String parameter[] = parameters.split( " " );
		this.switchOn = Boolean.valueOf( parameter[3] );
	}

	@Override
    public ThrustEvent clone() {
		ThrustEvent copy = (ThrustEvent) super.clone();
		return copy;
	}
	@Override
	public String serialize() {
		return "" + super.serialize() + " " + this.switchOn;
	}
	@Override
	public void dispatch( EventReceiver receiver ) {
		receiver.receiveThrustEvent( this );
	}
	@Override
    public Cathegory getType() {
		return type;
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
}
