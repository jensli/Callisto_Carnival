package cc.game.weapon;

import cc.game.GameObject;

/**
 * Class owned by a {@link GameObject} for creating a new projectile from that object.
 * Subclasses contain logic for specific kinds of weapons, e.g. reload times, how and
 * where the projectile is created e.t.c.
 *
 */
public abstract class Weapon
{
//	protected GameObject controller;
	protected String name;
	protected double ammo;
	protected double maxAmmo = 100.0;

	public Weapon()
	{

	}

	/**
	 * Called each game loop
	 * @param controlled TODO
	 * @param dT
	 */
	public abstract void update( GameObject controlled, double dT );

	/**
	 * Called when someone wants to fire the weapon. If this is allowed a projectile
	 * is created.
	 * @param controller TODO
	 */
	public abstract void fire( GameObject controller );

	public double getAmmo() {
		return ammo;
	}
	public void setAmmo( double ammo ) {
		this.ammo = ammo;
	}
	public void changeAmmo( double dAmmo ) {
		ammo = Math.min( maxAmmo, ammo + dAmmo );
	}
	public double getMaxAmmo() {
		return maxAmmo;
	}
	public void setMaxAmmo( double maxAmmo ) {
		this.maxAmmo = maxAmmo;
	}
}
