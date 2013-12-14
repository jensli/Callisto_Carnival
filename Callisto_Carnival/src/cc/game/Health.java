package cc.game;

/**
 * Objects of this class reacts to collision, one object gets the other objs
 * Health object and invoke a method on it.
 *
 * If isAlive() returns true the
 * game engine removes the GameObject from the game.
 */
public interface Health
{
	public boolean isAlive();
	public void normalDamage( double d );
	public void energyDamage( double d );
	public void radiationDamage( double d );
	public void kill();
	public void update( double dT );
}
