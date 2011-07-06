package cc.event;

import cc.event.handlers.EventReceiver;

public class QuitEvent extends Event
{
	@Override
	public void dispatch( EventReceiver receiver )
	{
		receiver.receiveQuitEvent( this );
	}

}
