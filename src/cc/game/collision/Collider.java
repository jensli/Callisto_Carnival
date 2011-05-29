package cc.game.collision;

import cc.game.GameObject;
import cc.game.objects.Ship;
import cc.game.objects.Planet;
import cc.game.objects.SpaceStation;

public interface Collider
{
	
	/**
	 * Does nothing, an invincible (e.g. a planet) is not affected by collisions
	 */
	public void collideInvincible( Planet other, GameObject me );
	
	/**
	 * Same as collideInvincible but accepts GameObject
	 */
	public void collideIgnore( GameObject other, GameObject me );
	
	public void collideFragile( GameObject other, GameObject me );
	
	public void collideStation( SpaceStation other, GameObject me );
	
	/**
	 * Most stuff can use this, dec life of the obj colliding me
	 */
	public void collideDefault( GameObject other, GameObject me );
	
	/**
	 * By default a ship is collided in the with collideDefaults
	 */
	public void collideActor( Ship other, GameObject me );
	
	/**
	 *  Gets how much damage this obj does to one it collides with
	 */
	public double getDamage();
	
	public void setDamage( double damage );
	
}
