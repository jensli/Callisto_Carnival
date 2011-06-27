package cc.game.behaviors;


import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import cc.event.Event;
import cc.game.GameObject;
import cc.util.Util;


public class BehaviorGroup extends Behavior
{
	private Collection<Behavior> behaviorList = new LinkedList<Behavior>();

	public BehaviorGroup( Behavior... behaviors )
	{
		for ( Behavior b : behaviors ) {
			add( b );
		}
	}

	@Override
	public void perform( GameObject controlled, double dT )
	{
		Iterator<Behavior> itr = behaviorList.iterator();

		while ( itr.hasNext() ) {

			Behavior behavior = itr.next();

			behavior.perform( controlled, dT );

			if ( behavior.isFinished() ) {
				itr.remove();
			}
		}
	}

	@Override
	public void receiveEvent( Event event )
	{
	    for ( Behavior behavior : behaviorList ) {
	    	behavior.receiveEvent( event );
	    }
    }

	public void add( Behavior b )
	{
		Util.verifyNotNull( b, "Behavior.add: b"  );
		Util.verifyArg( !b.isEmpty(), "Can not add the empty behavior to BehaviorGroup" );

		behaviorList.add( b );
    }

//	public boolean isGroup() {
//		return true;
//	}

	@Override
    public Behavior makeGroupWith( Behavior b )
	{
		add( b );
	    return this;
    }

}
