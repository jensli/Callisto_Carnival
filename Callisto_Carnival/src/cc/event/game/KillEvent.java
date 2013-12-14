package cc.event.game;

import j.util.eventhandler.GroupName;
import cc.event.Event;
import cc.event.handlers.EventReceiver;
import cc.event2.EventGroups;

public class KillEvent extends Event
{
	public KillEvent(int newReceiverID)
	{
		super(newReceiverID);
	}
	public KillEvent() {
		super();
	}

	@Override
    public void dispatch( EventReceiver receiver )
    {
		receiver.receiveKillEvent( this );
    }

	@Override public String getName() {
		return Event.KILL;
	}
	@Override public GroupName getReceiverGroup() {
		return EventGroups.KILL;
	}


}
