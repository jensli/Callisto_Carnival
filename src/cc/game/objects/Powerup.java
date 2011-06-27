package cc.game.objects;

import cc.game.GameObject;
import cc.game.ObjectCathegory;
import cc.game.PhysicalModel;
import cc.game.PowerupType;
import cc.game.behaviors.FadeInBehavior;
import cc.game.behaviors.FreeFloatBehavior;
import cc.game.behaviors.OrbitBehavior;
import cc.game.behaviors.TimerBehavior;
import cc.gui.models.TextureSquare;
import cc.util.Color;
import cc.util.Texture;
import cc.util.sound.Sound;

public class Powerup extends GameObject
{


	private TimerBehavior deathTimerBehavior;

    {
    	setCathegory( ObjectCathegory.POWERUP );
    }

	public Powerup( String name, OrbitBehavior orbit, PowerupType type, Texture texture, Sound sound )
	{
		this( name, texture, type.getColor(), sound );

		setMovementBehavior( orbit );
		setCollider( type.getCollider() );
		getGraphicalModel().setColor( type.getColor() );
		getGraphicalModel().getColor().a = 0;
	}


	public Powerup( String name, Texture texture, Color color, Sound sound )
	{
		super( name );
//		this.sound = sound;
		PhysicalModel pm = getPhysModel();

		pm.setMass( 0.0 );
		pm.setRadius( 10 );
		pm.setRotation( 5 );

		setMovementBehavior( FreeFloatBehavior.make() );

		setCollideGroup( GameObject.COLLIDE_GROUP_2 );
		setGraphicalModel( new TextureSquare( this, texture, 0.3 ) );
		addExtraBehavior( new FadeInBehavior( 2 ) );

		deathTimerBehavior = TimerBehavior.makeDeathFader( 5.0, 0.2 );
    }


	@Override
	public void update( double dT )
	{
		super.update( dT );
		deathTimerBehavior.perform( this, dT );
    }

	@Override
    public void collide( GameObject other )
	{
		other.getCollider().collideDefault( this, other );
    }


	public void takeDataFrom( Powerup other )
	{
		getGraphicalModel().setColor( other.getGraphicalModel().getColor() );

	}


//	@Override
//	protected void die()
//	{
//	    super.die();
//		sound.play( 1, 0.7f );
//  }

}
