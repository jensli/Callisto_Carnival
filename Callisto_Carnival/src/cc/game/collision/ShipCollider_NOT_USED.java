package cc.game.collision;


public class ShipCollider_NOT_USED extends DefaultCollider
{
//	private boolean rotationDir = true;

//	@Override
//    public void collideMe( GameObject me, GameObject other )
//	{
//		other.getCollider().collideShip( (Ship) me, other );
//	}


//	@Override public void receiveEvent( Event event ) {
//	   event.dispatch( this );
//    }


//	@Override
//    public void collideShip( GameObject other, GameObject me )
//    {
//		other.changeLife( -this.getDamage() );
//
//		if ( other.getLife() > 0.0 ) {
//			this.bounceOther( me, other );
//		}
//    }

//	private void bounceOther( GameObject me, GameObject other )
//	{
//		Vector2d punch = me.getVelocity();
//		punch.sub( other.getVelocity() );
//		punch.scale( 0.3 );
//
//		other.addVelocity( punch );
//		other.impulseAccelerate( punch );
//
//		other.setRotation( (rotationDir = !rotationDir) ? 20.0 : -20.0 );
//	}


}
