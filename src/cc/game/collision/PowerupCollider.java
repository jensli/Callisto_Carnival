package cc.game.collision;

import cc.game.GameObject;
import cc.game.objects.Ship;

/**
 * Collider that gives a collidee some life ammo or fuel when collided
 */
public class PowerupCollider extends DefaultCollider
{
	private double
		extraLife,
		extraAmmo,
		extraFuel;

	public PowerupCollider( double extraLife, double extraAmmo, double extraFuel )
	{
	    this.extraLife = extraLife;
	    this.extraAmmo = extraAmmo;
	    this.extraFuel = extraFuel;
    }

	@Override
    public void collideActor( Ship other, GameObject me )
	{
		other.changeLifeClamped( extraLife );
		other.getControlledBehavior().getWeapon().changeAmmo( extraAmmo );
		other.getControlledBehavior().changeFuelLevel( extraFuel );
    }
}
