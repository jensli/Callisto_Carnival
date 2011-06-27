package cc.event;

import cc.event.handlers.EventReceiver;

public class QuitEvent extends Event
{
	private static final Cathegory type = Cathegory.APPLICATION;

	@Override
	public Cathegory getType()
	{
		return type;
	}

	@Override
	public void dispatch( EventReceiver receiver )
	{
		receiver.receiveQuitEvent( this );
	}

}
