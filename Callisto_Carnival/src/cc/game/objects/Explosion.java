package cc.game.objects;


import java.util.List;

import cc.game.GameObject;
import cc.game.PhysicalModel;
import cc.game.behaviors.TimerBehavior;
import cc.gui.models.Animation;
import cc.util.Random;
import cc.util.Texture;
import cc.util.math.Vec;
import cc.util.sound.Sound;

public class Explosion extends GameObject
{
	public TimerBehavior deathTimer;
	public Sound sound;

	public Explosion( String name, List<Texture> textureList, Sound sound )
	{
		super( name );

		this.sound = sound;
		PhysicalModel pm = this.getPhysModel();

		pm.setMass( 0.0 );
		pm.setRadius( 5.0 );
		pm.setRotation( 10f * (Random.getNormalRandom().nextFloat() - 0.5) );
		double randomAngel = Random.getNormalRandom().nextFloat() * 2 * Math.PI;
		pm.setForward(
				new Vec( Math.cos( randomAngel ), Math.sin( randomAngel ) ) );

		deathTimer = new TimerBehavior( 0.3 );

		this.setCollideGroup( GameObject.COLLIDE_GROUP_0 );

//		List<Texture> coll = resources.getTextures( ResourceHandler.LARGE_EXP_ANIM );

		Animation anim = new Animation(this, textureList, 0.3);
//		anim.setRepeatStyle( Animation.REPEAT );
		this.setGraphicalModel( ( anim ) );


	}

	@Override
	public void update( double dT )
	{
		super.update( dT );
		deathTimer.perform( this, dT );
    }

	@Override protected void onCreate() {
		sound.play( 1, 0.7f );
    }


}
