package cc.game.collision;

import cc.game.GameObject;
import cc.game.objects.Ship;
import cc.game.objects.Planet;
import cc.game.objects.SpaceStation;

/**
 * The default collider, defines the default actions done to an object
 * when it collied with the owner of this collider.
 *
 * Special colliders extend this, overriding the methods for the kind of
 * objects it wants to do something special to.
 *
 * NOTE: The methods of the colliders is called from the object that collides with
 * the owner of the collider. When the call is made the OTHER object pass the this
 * pointer as the 'other' argument, and the owner of the collider as 'me' argument.
 */
public class DefaultCollider implements Collider
{
	 // How much damage this obj does to one it collides with
	private double damage = Double.MAX_VALUE;

	public DefaultCollider() {}

	public DefaultCollider( double damage )
    {
		this.damage = damage;
    }

//	public void collideMe( GameObject me, GameObject other ) {
//		System.out.println("Wrong!");
//		other.getCollider().collideDefault( me, other );
//	}

	/**
	 * Does nothing, an invincible (e.g. a planet) is not affected by collisions
	 */
	public void collideInvincible( Planet other, GameObject me ) {}

	public void collideIgnore( GameObject other, GameObject me ) {}

	public void collideFragile( GameObject other, GameObject me )
	{
		other.setAlive( false );
	}

	public void collideStation( SpaceStation other, GameObject me ) {}

	/**
	 * Most stuff can use this, dec life of the obj colliding me
	 */
	public void collideDefault( GameObject other, GameObject me )
	{
		other.changeLife( -getDamage() );
	}

	/**
	 * By default a ship is collided in the with collideDefaults
	 */
	public void collideActor( Ship other, GameObject me )
	{
		this.collideDefault( other, me );
	}

	/**
	  *  Gets how much damage this obj does to one it collides with
	  */
	public double getDamage() {
    	return damage;
    }

	public void setDamage( double damage ) {
    	this.damage = damage;
    }

}
