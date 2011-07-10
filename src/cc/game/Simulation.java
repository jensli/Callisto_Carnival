package cc.game;


import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cc.event.game.CollisionEvent;
import cc.event2.EventGlobals;
import cc.event2.EventGroups;
import cc.game.behaviors.Behavior;
import cc.gui.Drawable;
import cc.util.math.Vec;
import cc.util.math.VecMath;


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

	// The main list of GameObjects in the game
	private Map<Integer, GameObject> objectMap = new HashMap<Integer, GameObject>();

	// Cache to make iteration throught the objects faster. It turned out that
	// HashMap iterators what the largest part of the garbage.
	private GameObject[] objectCache = new GameObject[0];


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


	private double totalRunTime = 0;

	// Temp vector to save an allocation when calculating the gravity.
	private static final Vec tempGrav = new Vec();

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

		for ( GameObject obj : objectCache ) {

			// Event store not used. (Why?)
//			if ( eventStore.containsKey( obj ) ) {   // Send events to object
//				for ( Event e : eventStore.remove( obj ) ) {
//					obj.receiveEvent( e );
//				}
//			}

			obj.update( dT );

			// Check is object should be killed
			if ( !obj.isAlive() ) {
				toRemoveList.add( obj );
			}
		}

		// Remove dead objects
		for ( GameObject obj : toRemoveList ) {
			obj.onDeath();
			objectMap.remove( obj.getID() );
		}

		// Collisions
		List<CollisionEvent> collisions = checkCollisions( objectCache );
		EventGlobals.getHandler().post( EventGroups.COLLISION, collisions );

		// Add created objects
		for ( GameObject objectToCreate : toCreateList ) {
			objectMap.put( objectToCreate.getID(), objectToCreate );
			objectToCreate.onCreate();
		}

		Collection<GameObject> objects = objectMap.values();

		// Recreate the cache array if somethings has changed.
		// This is probably faster that iterating through the map.
		if ( !toRemoveList.isEmpty() || !toCreateList.isEmpty() ) {
			objectCache = objects.toArray( new GameObject[ objects.size() ]  );
		}

		toRemoveList.clear();
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
	public static List<CollisionEvent> checkCollisions( GameObject[] objs )
	{
		// All detected collsions are stored here and sent when checking of all
		// objects has finished.
		List<CollisionEvent> eventsToSend = Collections.emptyList();

		GameObject obj1, obj2;

		for ( int i = 0; i < objs.length; i++ )  {
			// Placed here to make sure we quit in time, considering itr2 assignmet below.
			// We want to quit this loop when there is ONE elemtent LEFT in the list.
			obj1 = objs[ i ];

			// Skip if objects belong to collide groups which cant collide
			if ( obj1.getCollideGroup() == GameObject.COLLIDE_GROUP_0
			        || obj1.getCollideGroup() == GameObject.COLLIDE_GROUP_1 ) {
				continue;
			}

			for ( int j = i + 1; j < objs.length; j++ ) {

				obj2 = objs[ j ];
				// Skip if objects belong to collide groups which cant collide
				if ( obj2.getCollideGroup() == GameObject.COLLIDE_GROUP_0 ) {
					continue;
				}

				if ( hasCollided( obj1, obj2 ) ) {

					if ( eventsToSend.isEmpty() ) {
						eventsToSend = new LinkedList<CollisionEvent>();
					}

					eventsToSend.add( new CollisionEvent( obj1.getID(), obj2.getID(), obj2 ) );
					eventsToSend.add( new CollisionEvent( obj2.getID(), obj1.getID(), obj1 ) );
				}

			} // End of second for

		} // End of first for


		return eventsToSend;
	}


	public void calcGravitation( GameObject obj, Vec v )
	{
		calcGravitation( obj, objectCache, v );
	}

	public static void calcGravitation( GameObject obj, GameObject[] objects, Vec result )
	{
		double
			maxDistanceSqr = 1.0,
			maxForce = 0.0;

		Vec objPos = obj.getPhysModel().getPos();

		result.zero();

		// First calculate a value so we can compare the magnitude of
		// the grav forces
		for ( GameObject listObj : objects ) {

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
				result.set( tempGrav );
			}
		}

		// Second scale the largest of the grav forces to the right size
		result.scale( GRAVITATION_CONST * maxForce / sqrt( maxDistanceSqr ) );  // divide by distance to normalize
	}

}

