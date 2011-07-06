package cc.event.game;

import cc.event.Event;
import cc.event.handlers.EventReceiver;

public class KillEvent extends Event
{
	public KillEvent(int newReceiverID)
	{
		super(newReceiverID);
		setName( Event.KILL );
	}
	public KillEvent() {
		super();
		setName( Event.KILL );
	}

	@Override
    public void dispatch( EventReceiver receiver )
    {
		receiver.receiveKillEvent( this );
    }

}
