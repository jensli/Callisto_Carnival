package cc.game.objects;

import cc.game.GameObject;
import cc.game.PhysicalModel;
import cc.game.behaviors.Behavior;
import cc.game.behaviors.TimerBehavior;
import cc.game.collision.DefaultCollider;
import cc.gui.models.TextureSquare;
import cc.util.Texture;
import cc.util.sound.Sound;

public class LaserShot extends Shot
{
	private TimerBehavior deathTimerBehavior;
//	private Sound sound;
	
	public LaserShot( String name, Texture texture, Sound sound ) 
	{
		super( name );
		
//		this.sound = sound;
		PhysicalModel pm = super.getPhysModel();
		
		pm.setMass( 0.0 );
		pm.setRadius( 2 );
		
		deathTimerBehavior = new TimerBehavior( 8.0 );
		this.setMovementBehavior( Behavior.getEmpty() );
		this.setCollideGroup( GameObject.COLLIDE_GROUP_2 );
		this.setGraphicalModel( new TextureSquare( this, texture ) );
		
		this.setCollider( new DefaultCollider( 22.0 ) ); 
	}
	
	@Override 
	public void update( double dT ) 
	{
		super.update( dT );
		deathTimerBehavior.perform( this, dT );
    }

//	@Override protected void die() {
//	    super.die();
//		sound.play( 1, 0.7f );
//
//    }
	
	
	
	
}
