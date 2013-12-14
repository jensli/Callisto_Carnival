package cc.event;

import j.util.eventhandler.GroupName;
import cc.event.handlers.EventReceiver;
import cc.event2.EventGroups;

public class TickEvent extends Event
{
	private static double staticDt = Double.NaN;

//	private double dT;

	public static void setDt( double dT ) {
		staticDt = dT;
	}


	public TickEvent() {
		super();
	}

//	public TickEvent( double dT )
//	{
//		dT = dT;
//	}

	@Override
    public void dispatch( EventReceiver receiver ) {
		receiver.receiveTickEvent( this );
	}

	public double getDt() {
		return staticDt;
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
