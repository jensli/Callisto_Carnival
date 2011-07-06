package cc.event.game;

import cc.event.Event;
import cc.event.handlers.EventReceiver;

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
}
