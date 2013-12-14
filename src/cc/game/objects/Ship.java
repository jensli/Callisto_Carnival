package cc.game.objects;

import cc.event.Event;
import cc.event.game.CreateEvent;
import cc.event2.EventGlobals;
import cc.event2.EventGroups;
import cc.game.GameFactory;
import cc.game.GameObject;
import cc.game.ObjectCathegory;
import cc.game.behaviors.Behavior;
import cc.game.behaviors.BehaviorGroup;
import cc.game.behaviors.ControlledBehavior;
import cc.game.behaviors.FadeInBehavior;
import cc.game.behaviors.FreeFloatBehavior;
import cc.game.collision.DefaultCollider;
import cc.gui.models.GraphicalModelGroup;
import cc.gui.models.ParticleSystem;
import cc.gui.models.TextureSquare;
import cc.util.Texture;

public class Ship extends GameObject
{
	private ControlledBehavior controlledBehavior;
	private BehaviorGroup extraBehaviors = new BehaviorGroup();

    {
    	setCathegory( ObjectCathegory.ACTOR );
    }


	public Ship( String name, Texture texture, Texture flameTexture )
	{
		super( name );
		makeShip( texture, flameTexture );
	}

	private void makeShip( Texture texture, Texture flameTexture )
	{
		getPhysModel().setMass( 3.0 );
		getPhysModel().setRadius( 10.0 );
//		this.setDamage( 1.0 );

		setMovementBehavior( FreeFloatBehavior.make() );

		setLife( 100.0 );
		setCollider( new DefaultCollider() );
//		setCollider( new ShipCollider() );
		setControlledBehavior( new ControlledBehavior() );

		// Graphicas model
		GraphicalModelGroup group = new GraphicalModelGroup( this );

		TextureSquare ts = new TextureSquare( this, texture, 32, 32 );
		ts.setDirectionOffset( 0.5*Math.PI );
		ts.getColor().a = 0;
		group.add( ts );

		ParticleSystem ps = new ParticleSystem( this, flameTexture, 500 );
		ps.setController( controlledBehavior );
		group.add( ps );

		group.add( new FadeInBehavior( this, 2 ) );
//		addExtraBehavior( new FadeInBehavior( 2 ) );


		setGraphicalModel( group );
	}

	@Override
	public void update( double dT )
	{
		super.update( dT );
		controlledBehavior.perform( this, dT );
		extraBehaviors.perform( this, dT );
    }

	@Override
	public void receiveEvent( Event event )
	{
		super.receiveEvent( event );
		controlledBehavior.receiveEvent( event );
		extraBehaviors.receiveEvent( event );
    }

	@Override
	public void collide( GameObject other )
	{
		other.getCollider().collideActor( this, other );
    }

	@Override
	public void onDeath()
	{
		super.onDeath();
		GameObject exp = GameFactory.get().createExplosion();
		exp.getPhysModel().setPos( getPos() );

		EventGlobals.getHandler().post( EventGroups.CREATE, new CreateEvent( exp ) );
	}

	public ControlledBehavior getControlledBehavior() {
		return controlledBehavior;
	}

	public void setControlledBehavior( ControlledBehavior controlledBehavior ) {
		this.controlledBehavior = controlledBehavior;
	}

	@Override
    public void addExtraBehavior( Behavior behavior ) {
		extraBehaviors.add( behavior );
	}

	public BehaviorGroup getExtraBehaviors() {
		return extraBehaviors;
	}

	public void setExtraBehaviors( BehaviorGroup extraBehaviors ) {
		this.extraBehaviors = extraBehaviors;
	}
}
