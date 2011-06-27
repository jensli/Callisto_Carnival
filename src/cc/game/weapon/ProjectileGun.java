package cc.game.weapon;

import cc.event.game.CreateEvent;
import cc.event.handlers.EventHandler;
import cc.game.GameFactory;
import cc.game.GameObject;
import cc.game.PhysicalModel;
import cc.util.math.Vec;
import cc.util.resources.Name;
import cc.util.resources.ResourceHandler;
import cc.util.sound.Sound;

/**
 * Weapon that fire small circular shots.
 *
 * This is the standard weapon in Callisto Carneval.
 *
 */
public class ProjectileGun extends Weapon
{
	Sound sound;

	private ShotCreator shotCreator;

	private double
		refireTime = 0.1,
		timeToFire = refireTime;

	public ProjectileGun( ShotCreator shotCreator )
	{
		super.ammo = 100.0;
		this.shotCreator = shotCreator;
		sound = ResourceHandler.get().getSound( Name.LASER_SOUND );
	}

//	public ProjectileGun()
//	{
//		sound = ResourceHandler.getInstance().getSound( ResourceHandler.LASER_SOUND );
//		super.ammo = 100.0;
//	}

	@Override
    public void update( GameObject controller, double dT )
    {
		timeToFire -= dT;
    }

	@Override
    public void fire( GameObject controller )
    {
		if ( timeToFire > 0.0 ) {
			return;
		}
		timeToFire = shotCreator.refireTime();

		if ( super.ammo <= 0.0 ) {
			return;
		}
		super.ammo -= shotCreator.dAmmo();

		PhysicalModel physical = controller.getPhysModel();

		// Get a SpaceObject with bahaviors and all
//		GameObject shot = GameCreator.getInstance().createBallShot();
		GameObject shot = shotCreator.createShot();

		// Calculate the velocity and directon
		Vec
			contrForw = new Vec( physical.getForward() ),
			shotPos = new Vec( contrForw ),
			shotVel;

		// Sets posision, making sure the shot and the shooter don't collide.
		// Note: if the shoter accelarate to fast they will collide
//		double shooterVel = physical.getVelocity().length();

		shotPos.scale( physical.getRadius() + shot.getPhysModel().getRadius()
				+  1 );
		shotPos.add( physical.getPos() );
		shot.getPhysModel().setPos( shotPos );

		// Speed = shot speed + controlled speed

		shotVel = contrForw;
		shotVel.scale( shotCreator.speed() );
		shotVel.add( physical.getVel() );

		shot.getPhysModel().setVelocity( shotVel );

		shotVel.normalize();
		shot.getPhysModel().setForward( shotVel );

		EventHandler.get().postEvent( new CreateEvent( shot ) );

		sound.play( 0.8f, 0.1f );
    }



	public static abstract class ShotCreator {
		protected double refireTime,
			speed,
			dAmmo;

		public abstract GameObject createShot();
		public double speed() {
			return speed;
		}
		public double refireTime() {
			return refireTime;
		}
		public double dAmmo() {
			return dAmmo;
		}
	}

	public static class PlasmaBallCreator extends ShotCreator {
		{
			refireTime = 0.15;
			speed = 800;
			dAmmo = 1;
		}
		@Override
        public GameObject createShot(){
			return GameFactory.get().createPlasmaShot();
		}
	}

	public static class LaserCreator extends ShotCreator {
		{
			refireTime = 0.08;
			speed = 1600;
			dAmmo = 0.31;
		}
		@Override
        public GameObject createShot(){
			return GameFactory.get().createLaserShot();
		}

	}


}
