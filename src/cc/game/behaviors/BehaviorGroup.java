package cc.game.behaviors;


import j.util.lists.IterCacheList;

import java.util.ArrayList;

import cc.event.Event;
import cc.game.GameObject;
import cc.util.Util;


public class BehaviorGroup extends Behavior
{
//	private List<Behavior> behaviorList = new ArrayList<Behavior>();
	private IterCacheList<Behavior> behaviorList =
		new IterCacheList<Behavior>( new ArrayList<Behavior>( 3 ) );


	public BehaviorGroup( Behavior... behaviors )
	{
		for ( Behavior b : behaviors ) {
			add( b );
		}
	}

	@Override
	public void perform( GameObject controlled, double dT )
	{
		for ( Behavior behavior : behaviorList ) {

			behavior.perform( controlled, dT );

			if ( behavior.isFinished() ) {
				behaviorList.removeCurrent();
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
