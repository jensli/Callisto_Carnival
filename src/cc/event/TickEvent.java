package cc.event;

import j.util.eventhandler.GroupName;
import cc.event.handlers.EventReceiver;
import cc.event2.EventGroups;

public class TickEvent extends Event
{
	public static void setDt( double dT ) {
		staticDt = dT;
	}

	private static double staticDt = 0.01;

	private double dT;


	public TickEvent() {
		super();
		this.dT = staticDt;
	}

	public TickEvent( double dT )
	{
		this.dT = dT;
	}

	@Override
    public void dispatch( EventReceiver receiver ) {
		receiver.receiveTickEvent( this );
	}

	public double getDt() {
		return dT;
	}


//    @Override
//	public void toStringBuilder( StringBuilder b ) {
//		No data sent, dT is statically set.
//	}


	@Override
	public GroupName getReceiverGroup() {
		return EventGroups.TICK;
	}

	@Override public String getName() {
		return Event.TICK;
	}
}
