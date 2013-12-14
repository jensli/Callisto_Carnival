package cc.game.objects;

import cc.event.Event;
import cc.game.GameObject;
import cc.game.behaviors.StationBehavior;
import cc.game.collision.StationCollider;
import cc.gui.models.TextureSquare;
import cc.util.Texture;

public class SpaceStation extends GameObject
{
	private StationBehavior stationBehavior;

	public SpaceStation( String name, Texture texture )
	{
		super( name );

		this.getPhysModel().setMass( 3.0 );
		this.getPhysModel().setRadius( 34.0 );
		this.getPhysModel().setRotation( -0.66 );
		this.setKillable( false );
//		this.setDamage( 0 );
//		this.setDamage( Double.MAX_VALUE );
		TextureSquare ts = new TextureSquare( this, texture );

		this.setGraphicalModel( ts );
		StationBehavior sb = new StationBehavior( 10 );
		this.setStationBehavior( sb );
		this.setCollider( new StationCollider( sb ) );

	}

	@Override
	public void receiveEvent( Event event )
	{
	    super.receiveEvent( event );
	    stationBehavior.receiveEvent( event );
    }

	@Override public void collide( GameObject other ) {
		other.getCollider().collideStation( this, other );
    }

	@Override
	public void update( double dT )
	{
	    super.update( dT );
	    stationBehavior.perform( this, dT );
    }

	public StationBehavior getStationBehavior() {
    	return stationBehavior;
    }
	public void setStationBehavior( StationBehavior stationBehavior ) {
    	this.stationBehavior = stationBehavior;
    }

}
