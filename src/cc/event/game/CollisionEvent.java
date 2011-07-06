package cc.event.game;

import cc.event.Event;
import cc.event.handlers.EventReceiver;
import cc.game.GameObject;

public class CollisionEvent extends Event
{
	private double collisionPower = 20.0;

	private GameObject object;

	public CollisionEvent(int i, int j, GameObject object)
	{
		super.setReceiverID( i );
		super.setSenderID( j );
		this.object = object;
	}

	public double getCollisionPower()
    {
    	return collisionPower;
    }

	public void setCollisionPower( double collisionPower )
    {
    	this.collisionPower = collisionPower;
    }

	@Override
    public void dispatch( EventReceiver receiver )
    {
		receiver.receiveCollisionEvent( this );
    }

	public GameObject getObject()
    {
    	return object;
    }


}
