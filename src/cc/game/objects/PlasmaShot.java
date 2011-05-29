package cc.game.objects;

import cc.game.GameObject;
import cc.game.PhysicalModel;
import cc.game.behaviors.FreeFloatBehavior;
import cc.game.behaviors.TimerBehavior;
import cc.game.collision.BounceCollider;
import cc.gui.models.TextureSquare;
import cc.util.Texture;
import cc.util.sound.Sound;

public class PlasmaShot extends Shot 
{
	private TimerBehavior deathTimerBehavior;
	private Sound dieSound;

	public PlasmaShot( String name, Texture texture, Sound dieSound ) 
	{
		super( name );
		
		this.dieSound = dieSound;
		
		PhysicalModel pm = super.getPhysModel();
		pm.setMass( 0.0 );
		pm.setRadius( 5.0 );
		pm.setRotation( 80.0 );
		
		deathTimerBehavior = TimerBehavior.makeDeathFader( 8.0, 0.2 );
		this.setMovementBehavior( FreeFloatBehavior.make() );
		this.setCollideGroup( GameObject.COLLIDE_GROUP_2 );
		this.setGraphicalModel( new TextureSquare( this, texture ) );
		
		this.setCollider( new BounceCollider( 36.0 ) ); 
	}
	
	@Override 
	public void update( double dT )
	{
		super.update( dT );
		deathTimerBehavior.perform( this, dT );
    }
	
	@Override 
	protected void onDeath()
	{
	    super.onDeath();
		dieSound.play( 1, 0.7f );
    }
	
//	public static Action1<GameObject> 
//		DEATH_ACTION = new Action1<GameObject>() {
//			public void run( GameObject arg ) {
//				EventHandler.get().postEvent( new KillEvent( arg.getID() ) );
//			}
//		};
//		public static Action1<GameObject> FADE_ACTION = new Action1<GameObject>() {
//			public void run( GameObject arg ) {
//				arg.addExtraBehavior( new DeathFaderBehavior( 0.02 ) );
//			}
//		};	

	
}
