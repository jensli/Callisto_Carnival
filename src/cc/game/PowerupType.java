/**
 * 
 */
package cc.game;

import cc.game.collision.Collider;
import cc.game.collision.PowerupCollider;
import cc.util.Color;

public enum PowerupType
{
	LIFE( Color.GREEN, new PowerupCollider( 50, 0, 0 ) ),
	AMMO( Color.RED, new PowerupCollider( 0, 50, 0 ) ),
	FUEL( Color.BLUE, new PowerupCollider( 0, 0, 50 ) );
	
	
	
	private PowerupType( Color color, Collider collider ) {
        this.color = color;
        this.collider = collider;
    }
	
	
	public final Color color;
	public final Collider collider;

	public Color getColor() {
    	return color;
    }
	public Collider getCollider() {
    	return collider;
    }
}