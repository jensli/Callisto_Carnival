package cc.comm;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cc.event.Event;

public final class CommUtil {

	private CommUtil() {}

	public static List<Event> lazyReadThread( InputThread in )
	{
		String line = in.readLine();
		if ( line == null ) return Collections.emptyList();

		String line2 = in.readLine();
		if ( line2 == null ) return Collections.singletonList( Event.make( line ) );

		List<Event> rec = new LinkedList<Event>();

		rec.add( Event.make( line ) );
		rec.add( Event.make( line2 ) );

		while ( ( line = in.readLine() ) != null ) {
			rec.add( Event.make( line ) );
		}

		return rec;
	}

}
