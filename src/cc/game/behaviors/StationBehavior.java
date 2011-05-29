package cc.game.behaviors;


import java.util.LinkedList;
import java.util.List;

import cc.event.Event;
import cc.game.GameObject;
import cc.game.objects.Ship;
import cc.game.objects.SpaceStation;

public class StationBehavior extends Behavior
{
//	private Ship collider = null;
	private boolean hasCollided = false;
//	private double realRadius;
	
	private List<GameObject> dockedObjectList = new LinkedList<GameObject>();

	private static Behavior.Type type  = Behavior.Type.STATION;
	private double dockSpeed = 500.0;
	

	public StationBehavior( double realRadius )
    {
//	    this.realRadius = realRadius;
    }

	@Override
	public void perform( GameObject controlled, double dT )
	{
		
//		if ( !this.hasCollided() ) {
//			return;
//		}
//		this.clearCollider();
//		
//		if ( this.collider.getPhysicalModel().getMass() < 1.0 ) {
//			return;
//		}
//		
//		Vector2d 
////			contPos = controlled.getPosition(),
////			colPos = collider.getPosition(),
//			dVel = new Vector2d();
//		
//		dVel.sub( controlled.getPhysicalModel().getVelocity(), collider.getPhysicalModel().getVelocity() );
//		
//		if ( dVel.length() < 300 && !dockedObjectList.contains( collider ) ) {
//			
//			dockedObjectList.add( collider );
//			
////			dPos.sub( collider.getPosition(), controlled.getPosition() );
////			collider.setVelocity( new Vector2d(0, 0) );
////			
////			collider.receiveEvent( new ThrustEvent(0, Event.SWITCH_OFF ) );
////			collider.setRotation( 0.0 );
//			
//			
//			DockedBehavior db = new DockedBehavior();
//			db.dock( (Ship) collider, (SpaceStation) controlled );
//			
//			
////			List<Behavior> list = new LinkedList<Behavior>();
////			list.add(  new DockedBehavior( ( Ship ) collider,
////					controlled, dPos, 
////					this, collider.getForward() ) );
////			
////			collider.setBehaviorList( list );
//		} 
		
	}

//	@Override
//    public void receiveCollisionEvent( CollisionEvent event )
//	{
//		collider = event.getObject();
//		hasCollided = true;
//    }
	
	public void dock( Ship ship, GameObject me )
	{
		dockedObjectList.add( ship );
		
		DockedBehavior db = new DockedBehavior();
		db.dock( ship, (SpaceStation) me );

		
//		collider = ship;
//		hasCollided = true;
	}
	
	public boolean isDocked( GameObject obj ) {
		return dockedObjectList.contains( obj );
	}
	public boolean hasCollided() {
		return hasCollided;
	}
	public void clearCollider() {
		hasCollided = false;
	}

	@Override 
	public void receiveEvent( Event event ) {
		event.dispatch( this );
	}
	public void undock( GameObject obj ) {
		dockedObjectList.remove( obj );
	}

	@Override
    public Behavior.Type getType() {
    	return type;
    }
	
	public double getDockSpeed() {
		return dockSpeed;
	}
	
	
	
}
