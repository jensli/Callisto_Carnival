package cc.game;


import j.util.functional.Fun1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cc.event.Event;
import cc.event.game.CollisionEvent;
import cc.event.handlers.EventHandler;
import cc.event.handlers.IEventHandler;
import cc.game.behaviors.Behavior;
import cc.gui.Drawable;
import cc.util.math.Vec;
import cc.util.math.VecMath;

import static java.lang.Math.*;


/**
 * Class that performs the physical simulation. The most important component of this class is
 * its list of SpaceObject objects, which is the core of the game. All objects in this list
 * are updated when update() is called.
 */

public class Simulation
{
	private static final double 
		DIST_THRESHOLD = 0.1,
		MASS_THRESHOLD = 0.1,
		GRAVITATION_CONST = 200000.0;
	
	private Map<Integer, GameObject> objectMap = new HashMap<Integer, GameObject>();
//	private List<SpaceObject> objectList = new LinkedList<SpaceObject>();
	
	// Objects to create is cached until next update to avoid concurrent modifications
	// of the objectMap
	private List<GameObject> 
		toCreateList = new ArrayList<GameObject>(),
		toRemoveList = new ArrayList<GameObject>();
	
	// Here the events are stored when they are received, then they are
	// passed to the GameObjects when they are update():ed
	// NOT USED!!! (Why?)
//	Map<GameObject, Collection<Event>> eventStore = new HashMap<GameObject, Collection<Event>>();
	
//	Map<GameObject, Collection<Event>> eventStore 
//	= new TreeMap<GameObject, Collection<Event>>( new Comparator<GameObject>() {
//		public int compare( GameObject obj1, GameObject obj2 ) {
//			return obj1 == obj2 ? 0 : obj1.hashCode() < obj2.hashCode() ? 1 : -1;
//		}
//	} );
	
	private final Fun1<GameObject, Vec> gravCalcer = new Fun1<GameObject, Vec>() {
		@Override public Vec run( GameObject arg ) {
			return calcGravitation( arg );
		}
	};
	
	/**
	 * Returns a Func1 wich objects can use to calc their gravity
	 */
	public Fun1<GameObject, Vec> getGravCalcer() {
    	return gravCalcer;
    }

	private double totalRunTime = 0;
	
	public Simulation()
	{
		Behavior.setSimulation( this );
	}
		
	/**
	 * Returns a reference to Simulations list of {@link GameObject}s. 
	 * @return 
	 */	 
	public Collection<GameObject> getObjectList()
	{
		return objectMap.values();
	}

	public Collection<? extends Drawable> getDrawableList()
	{
		return objectMap.values();
	}
	
	/**
	 * Adds an object to the list of GameObject.
	 * @param obj
	 */
	public void addSpaceObject(GameObject obj)
	{
		toCreateList.add( obj );
	}
	
	public void addSpaceObjectList(List<GameObject> list)
	{
		toCreateList.addAll( list );
	}
	
	
	/**
	 * Updates the state of the simulation, calling the update methods of all {@link GameObject}s
	 * in the simulation, and removing the ones that is not alive.
	 * @param dT timeslice since last update
	 */
	public void update( double dT )
	{
		for ( GameObject obj : objectMap.values() ) {
			
			// Event store not used. (Why?)
//			if ( eventStore.containsKey( obj ) ) {   // Send events to object
//				for ( Event e : eventStore.remove( obj ) ) {
//					obj.receiveEvent( e );
//				}
//			}
			
			obj.update( dT );
			
			if ( !obj.isAlive() ) {  // Kill object
				obj.onDeath();
				toRemoveList.add( obj );
			}
		}
		
		// Remove dead objects
		for ( GameObject obj : toRemoveList ) {
			objectMap.remove( obj.getID() );
		}
		
		toRemoveList.clear();
		
		checkCollisions();

		// Add created objects
		for ( GameObject objectToCreate : toCreateList ) {
			
			objectMap.put( objectToCreate.getID(), objectToCreate );
			objectToCreate.onCreate();
		}
		toCreateList.clear();
		
		
		totalRunTime += dT;
	}
	
	
	// NOT USED!!! (Why?)
//	public void storeEvent( Event event )
//	{
//		GameObject obj = this.getObject( event.getReceiverID() );
//		if ( !eventStore.containsKey( obj ) ) {
//			eventStore.put( obj, new LinkedList<Event>() );
//		}
//		eventStore.get( obj ).add( event );
//		
//	}
	
	/**
	 * Returns a GameObject with the specified ID, or null if there is no object with that ID.
	 */
	public GameObject getObject( int objectID )
	{
		return objectMap.get( objectID );
	}

	/**
	 * Sets a objects alive property to false if there is an object with the specified ID.
	 * This removes the object at the next update.
	 */
	public void killObject( int objectID )
	{
		GameObject obj = getObject(objectID);
		
		if (obj != null) {
			obj.setAlive( false );
		}
	}
	
	/**
	 * Determine if obj1 and obj2 have collided with each other.
	 */
	public static boolean hasCollided( GameObject obj1, GameObject obj2 )
	{
		final Vec
			pos1 = obj1.getPos(),
			pos2 = obj2.getPos();
		
		double collDist = obj1.getRadius() + obj2.getRadius();
		
		if ( pos1.x - pos2.x > collDist || pos2.x - pos1.x > collDist ||
				pos1.y - pos2.y > collDist || pos2.y - pos1.y > collDist ) {
			return false;
		}
		
		if ( pos1.distance( pos2 ) > collDist ) {
			return false;
		}
		
		// Passed all tests
		return true;
	}
	
	
	/**
	 * Checks if any of the GameObjects in the simulation have collided
	 */
	public void checkCollisions()
	{
		// All detected collsions are stored here and sent when checking of all
		// objects has finished.
		List<Event> eventsToSend = new ArrayList<Event>();
		
		GameObject obj1, obj2;
		Iterator<GameObject> itr1,	itr2;

		Collection<GameObject> objectCollection = objectMap.values();  
		itr1 = objectCollection.iterator();

		if ( !itr1.hasNext() ) {
			return;
		}
		
		while ( true ) {
			// Placed here to make sure we quit in time, considering itr2 assignmet below.
			// We want to quit this loop when there is ONE elemtent LEFT in the list.
			obj1 = itr1.next();

			if ( !itr1.hasNext() ) {
				break;
			}
			// Skip if objects belong to collide groups which cant collide
			if ( obj1.getCollideGroup() == GameObject.COLLIDE_GROUP_0
			        || obj1.getCollideGroup() == GameObject.COLLIDE_GROUP_1 ) {
				continue;
			}
			
			itr2 = objectCollection.iterator();
			// Loop to the right pos in the list, skipping objects that this obj 
			// already has been checked against
			while ( itr2.next() != obj1 ) ;
			
			while ( itr2.hasNext() ) {
				obj2 = itr2.next();
				// Skip if objects belong to collide groups which cant collide
				if ( obj2.getCollideGroup() == GameObject.COLLIDE_GROUP_0 ) {
					continue;
				}
				
				if ( hasCollided( obj1, obj2 ) ) {

					eventsToSend.add( new CollisionEvent( obj1.getID(), obj2.getID(), obj2 ) );
					eventsToSend.add( new CollisionEvent( obj2.getID(), obj1.getID(), obj1 ) );
				}
			} // End of second while
			
		} // End of first while
		
		
		// TODO: Shoundnt this just pass the events to the GameEngine objects?
		// No need to use the EventHandler?
		IEventHandler handler = EventHandler.get();
		for ( Event event : eventsToSend ) {
			handler.postEvent( event );
		}
	}
	
	
	public Vec calcGravitation( GameObject obj )
	{
		return calcGravitation( obj, objectMap );
	}
	
	public static Vec calcGravitation( GameObject obj, Map<Integer, GameObject> objectMap )
	{
		double
			maxDistanceSqr = 1.0,
			maxForce = 0.0;
		
		final Vec
			tempGrav = new Vec(),
			objPos = obj.getPhysModel().getPos(),
			gravitation = new Vec();
		
		// First calculate a value so we can compare the magnitude of
		// the grav forces
		for ( GameObject listObj : objectMap.values() ) {
			
			// Skip calculating for this object if its to small of if listObj and obj are to close  
			if ( abs( listObj.getPhysModel().getMass() ) < MASS_THRESHOLD ||
					VecMath.equals( objPos, listObj.getPos(), DIST_THRESHOLD ) ) {
				continue;
			}
			
			tempGrav.setSub( listObj.getPos(), objPos );
			
			final double 
				distanceSqr = tempGrav.lengthSquared(),
				forceMagnetude = listObj.getPhysModel().getMass() / distanceSqr;
			
			// If this was the lagest force, save it
			if ( forceMagnetude > maxForce ) {
				maxDistanceSqr = distanceSqr;
				maxForce = forceMagnetude;
				gravitation.set( tempGrav );
			}
		}
		
		// Second scale the largest of the grav forces to the right size
		gravitation.scale( GRAVITATION_CONST * maxForce / sqrt( maxDistanceSqr ) );  // divide by distance to normalize
		
		return gravitation;
	}

	

}


// Gravity without "only strongest" calculation (?)
//public Vector2d gravitatioFor( GameObject obj )
//{
//	double
//		distance,
//		forceMagnetude,
//		listObjMass,
//		maxForce = 0.0;
//	
//	Vector2d
//		dPos,
//		gravitation  = new Vector2d();
//	
//	for ( GameObject listObj : objectMap.values() ) {
//		
//		// Skip calculating for this object if its to small or to near  
//		listObjMass = listObj.getMass();
//		if ( Math.abs( listObjMass ) < 0.1 ) {
//			continue;
//		}
//		dPos = listObj.getPosition();
//		if ( dPos.epsilonEquals( obj.getPosition(), 0.1 ) ) {
//			continue;
//		}
//		
//		dPos.sub( obj.getPosition() );
//		distance = dPos.lengthSquared();
//		forceMagnetude = ( GRAVITATION_CONST * listObjMass ) / ( distance );
//
//		if ( forceMagnetude > maxForce ) {
//			maxForce = forceMagnetude;
//			dPos.scale( forceMagnetude / Math.sqrt( distance ) );  // divide by distance to normalize
//			gravitation.set( dPos );
//		}
//		
////		dPos.scale( forceMagnetude / distance );  // divide by distance to normalize
////		gravitation.add( dPos );
//	}
//	
//	return gravitation;
//}
