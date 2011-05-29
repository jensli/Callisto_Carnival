package cc.game.behaviors;


import cc.event.Event;
import cc.event.game.FireEvent;
import cc.event.game.RotateEvent;
import cc.event.game.ThrustEvent;
import cc.game.GameObject;
import cc.game.weapon.ProjectileGun;
import cc.game.weapon.Weapon;
import cc.util.Random;



/**
 * Makes this GameObject which this behavior is attached to
 * controllable by the player. It receives RotateEvents and ThrustEvents
 * and executes them on the GameObject  
 */
public class ControlledBehavior extends Behavior
{
	private Weapon weapon;
	
	private boolean 
		thrustOn = false,
		rotateOn = false, 
		fireOn = false,
		rotateClockwise = false;
	
	private final double 
		rotationValue = 5.0,
		thrustValue = 600.0,
		rotationDampening = 8.0;
	
	private double fuelLevel = 10.0,
		maxFuel = 10.0,
		fuelRegeneratoin = 0.2;

	private static Behavior.Type type = Behavior.Type.CONTROLLED;
	
	
	public ControlledBehavior()
	{
		weapon = new ProjectileGun( new ProjectileGun.PlasmaBallCreator() );
		
		weapon = new ProjectileGun( Random.getGameRandom().nextBoolean()
				? new ProjectileGun.LaserCreator()
				: new ProjectileGun.PlasmaBallCreator() );
	}
	
	@Override
    public void receiveEvent( Event event )
	{
		event.dispatch( this );
	}
	
	
	@Override
	public void perform( GameObject controlled, double dT )
	{
		weapon.update( controlled, dT );
		
		if ( fireOn ) {
			weapon.fire( controlled );
		}
		
		this.updateThrust( dT );
		
		if ( thrustOn ) {
			controlled.getPhysModel().accelerateForward( thrustValue );
		} 
		
		if ( rotateOn ) {
			if ( Math.abs( controlled.getPhysModel().getRotation() ) < rotationValue ) {
				controlled.getPhysModel().setRotation( rotateClockwise ? rotationValue : -rotationValue );
			}
		}
		
		controlled.getPhysModel().dampenRotation( rotationDampening, dT );
	}
	
	private void updateThrust( double dT )
	{
		if ( fuelLevel <= 0.0 ) {
			thrustOn = false;
		}
		if ( thrustOn ) {
			fuelLevel -= dT;
		}
		if ( fuelLevel < maxFuel ) {
			fuelLevel += dT*fuelRegeneratoin;
		}
	}
	
	@Override
    public void receiveFireEvent( FireEvent event )
    {
		fireOn = event.isSwitchOn();
    }

	@Override
    public void receiveRotateEvent( RotateEvent event )
    {
		boolean tempOn = event.isSwitchOn(),
			tempClockwise = event.isRotateClockwise();
		
		// Make sure not to switch rotation off if this event
		// says to switch off, but for the wrong direction
		if ( tempOn || tempClockwise == rotateClockwise ) {
			rotateOn = tempOn;
			rotateClockwise = tempClockwise;
		}
		
    }

	@Override
    public void receiveThrustEvent( ThrustEvent event )
    {
		if ( fuelLevel > 0.0 ) {
			thrustOn = event.isSwitchOn();
		}
    }
	
	public Weapon getWeapon() {
		return weapon;
	}
	public boolean isThrustOn() {
		return thrustOn;
	}
	public void setThrustOn( boolean thrustOn ) {
		this.thrustOn = thrustOn;
	}
	public double getFuelLevel() {
		return fuelLevel;
	}
	public double getMaxFuel() {
		return maxFuel;
	}
	public void changeFuelLevel( double dFuel ) {
		fuelLevel = Math.min( maxFuel, fuelLevel + dFuel );
//		fuelLevel += dFuel;
	}
	@Override
    public Behavior.Type getType() {
		return type;
	}
	public void setFuelLevel( double fuelLevel ) {
    	this.fuelLevel = fuelLevel;
    }
	
}
