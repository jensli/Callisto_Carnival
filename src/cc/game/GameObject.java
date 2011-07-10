package cc.game;

import j.util.util.Util;
import cc.event.Event;
import cc.event.game.CollisionEvent;
import cc.event.game.KillEvent;
import cc.event.handlers.EventReceiver;
import cc.event2.EventGlobals;
import cc.event2.EventGroups;
import cc.game.behaviors.Behavior;
import cc.game.collision.Collider;
import cc.gui.Drawable;
import cc.gui.models.GraphicalModel;
import cc.util.math.Vec;


public class GameObject extends EventReceiver implements Drawable
{
	public enum CollideGroup {
		GROUP_1,
		GROUP_2,
		GROUP_3,
	}
	public static final int
		// Can't collide with anything
		COLLIDE_GROUP_0 = 0,
		// Can never collide with something in the same group, e.g. planets, moons
		COLLIDE_GROUP_1 = 1,
		// Can collide with anything
		COLLIDE_GROUP_2 = 2;

	private static int nextID = 1;

	private String name;  // Name to identify the obj

	// Obj that contains pos, velocity, mass itd.
	private PhysicalModel physicalModel = new PhysicalModel();

	// Object that draws this object
	private GraphicalModel graphicalModel;

	// Obj used to make actions to objects that collids with this.
	private Collider collider;

	@SuppressWarnings( "unused" )
	// TODO: Start using this?
    private Health health = new ShieldedHealth();

	// Behavior that decides how this obj moves
	private Behavior movementBehavior = Behavior.getEmpty();

	private Behavior extraBehavior = Behavior.getEmpty();

	private ObjectCathegory cathegory = ObjectCathegory.DEFAULT;

	private int collideGroup = GameObject.COLLIDE_GROUP_2;

	private double
		life = 1.0,
		maxLife = 100.0;

	private boolean
		alive = true,
		killable = true;

	private int	objectID;


	public GameObject( String name )
	{
		this.name = name;
		objectID = GameObject.getNewID();
	}

	public void addExtraBehavior( Behavior behavior )
	{
		extraBehavior = extraBehavior.makeGroupWith( behavior );

		// This logic moved to makeGroupWith(...)
//		if ( extraBehavior.isEmpty() ) {
//			extraBehavior = behavior;
//		} else if ( !extraBehavior.isGroup()  ) {
//			extraBehavior = new BehaviorGroup( extraBehavior );
//		}
//
//		extraBehavior.add( behavior );
	}

	public void addExtraGraphModel( GraphicalModel model )
	{
		graphicalModel = graphicalModel.makeGroupWith( model );
	}

	public void update( double dT )
	{
		if ( life <= 0.0 ) {
			EventGlobals.getHandler().post( EventGroups.KILL, new KillEvent( this.getID() ) );
		}

		movementBehavior.perform( this, dT );
		extraBehavior.perform( this, dT );
		physicalModel.update( this, dT );
		graphicalModel.update( dT );
	}

	protected void onCreate() {}

	protected void onDeath()
	{
		setAlive( false );
	}

	@Override
    public void receiveEvent( Event event ) {
		event.dispatch(  this );
	}

	public void draw()
	{
		graphicalModel.draw( getPos(), getForward() );
	}

	@Override
	public void receiveKillEvent( KillEvent event )
	{
		setAlive( false );
	}

	@Override
	public void receiveCollisionEvent( CollisionEvent event )
	{
//		collider.collideMe( this, event.getObject() );
		collide( event.getObject() );
	}

	/**
	 * This method should call a collideXXX method on the collider of the obj passed
	 * in as other. That method should be called with 'this' pointer as 'other' argument,
	 * and the obj passed in as 'me' argument.
	 */
	public void collide( GameObject other )
	{
		other.getCollider().collideDefault( this, other );
	}

	private static int getNewID()
	{
		return nextID++;
	}

	public void changeLife( double dL )
	{
//		life = Math.min( maxLife, life + dL );
		life += dL;
		if ( life < 0.0 ) life = 0.0;
	}

	public void changeLifeClamped( double dL )
	{
		life = Util.clamp( life + dL, 0, maxLife );
	}

	/*
	 * Getters and setters, absolutely no logic.
	 */
	public double getLife() { return life; }
	public int getID() { return objectID; }
	public int getCollideGroup() { return collideGroup; }
	public void setCollideGroup(int collideGroup) { this.collideGroup = collideGroup; }
	public boolean isAlive() { return alive; }
	public void setAlive( boolean alive ) { this.alive = alive; }
	public boolean isKillable() { return killable; }
	public void setKillable( boolean killable ) { this.killable = killable; }
	public GraphicalModel getGraphicalModel() { return graphicalModel; }
	public void setGraphicalModel( GraphicalModel graphicalModel ) { this.graphicalModel = graphicalModel; }
	public String getName() { return name; }
	public void setName( String name ) { this.name = name; }
	public Behavior getMovementBehavior() { return movementBehavior; }
	public void setMovementBehavior( Behavior movementBehavior ) { this.movementBehavior = movementBehavior; }
	public PhysicalModel getPhysModel() { return physicalModel; }
	public void setCollider( Collider collider ) { this.collider = collider; }
	public Collider getCollider() { return collider; }
	public void setLife( double life ) { this.life = life; }
	public double getMaxLife() { return maxLife; }
	public void setMaxLife( double maxLife ) { this.maxLife = maxLife; }
	public ObjectCathegory getCathegory() { return cathegory; }
	public void setCathegory( ObjectCathegory cathegory ) { this.cathegory = cathegory; }


	/**
	 * Shortcuts to corresponding method in physicalModel
	 */
	public final Vec getPos() { return getPhysModel().getPos(); }
	/**
	 * Shortcuts to corresponding method in physicalModel
	 */
	public final Vec getForward() { return getPhysModel().getForward(); }
	/**
	 * Shortcuts to corresponding method in physicalModel
	 */
	public final double getRadius() { return getPhysModel().getRadius(); }

}

