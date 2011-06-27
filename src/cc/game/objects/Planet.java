package cc.game.objects;

import cc.game.GameObject;
import cc.game.ObjectCathegory;
import cc.game.collision.DefaultCollider;
import cc.gui.models.TextureSquare;
import cc.util.Texture;

public class Planet extends GameObject
{
	{
		setCathegory( ObjectCathegory.PLANET );
	}

	public Planet( String name )
	{
		super( name );
	}

	public Planet( String name, Texture texture )
	{
	    this( name );
	    makePlanet( texture );
    }

	private void makePlanet( Texture texture  )
	{
		TextureSquare ts = new TextureSquare( this, texture );
		setGraphicalModel( ts );
		setKillable( false );
		setCollider( new DefaultCollider( Double.MAX_VALUE ) );
	}

	@Override
	public void collide( GameObject other )
	{
		other.getCollider().collideInvincible( this, other );
    }

}
