//
//Is this kind of event a good idea? To be sent by, tex, Weapond, CollisionBehav...
//
package cc.event;

import cc.event.handlers.EventReceiver;


public class SoundEvent extends Event
{
	private static final Cathegory type = Cathegory.GUI;

	@Override
    public void dispatch( EventReceiver receiver )
    {
//		receiver.receiveSoundEvent( this );
    }

	@Override
    public Cathegory getType()
    {
    	return type;
    }
	
	
}
