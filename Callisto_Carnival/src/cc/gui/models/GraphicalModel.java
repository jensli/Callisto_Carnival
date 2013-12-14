package cc.gui.models;

import cc.game.GameObject;
import cc.util.Color;
import cc.util.math.Vec;

public abstract class GraphicalModel
{
	private GameObject object;
	private boolean isFinished = false;

	public static final GraphicalModel EMPTY = new EmptyGraphicalModel();

	public GraphicalModel( GameObject object )
    {
	    this.object = object;
    }


	public abstract void draw( Vec pos, Vec forward );


	// NOT USED YET!!!
	public void drawMini( Vec pos, Vec forward )
	{
		// If subclass not override this, it is not painted on radar
	}

	public GameObject getObject()
    {
    	return object;
    }

	public void setObject( GameObject object )
    {
    	this.object = object;
    }

	/**
	 * Emptyp update function for models that dont need no updatin'.
	 */
	public void update( double dT )
	{
		;
	}

	public boolean isFinished()
	{
		return isFinished;
	}

//	public Vec getForward()
//    {
//	    return object.getPhysModel().getForward();
//    }

	public void setFinished( boolean isFinished ) {
		this.isFinished = isFinished;
	}


	public Vec getPosition()
    {
	    return object.getPhysModel().getPos();
    }

	public GraphicalModel makeGroupWith( GraphicalModel model )
	{
		GraphicalModelGroup group = new GraphicalModelGroup( getObject() );
		group.add( this );
		group.add( model );
		return group;
	}

	public double getRadius()
    {
	    return object.getPhysModel().getRadius();
    }

	public static GraphicalModel getEmpty() {
    	return EMPTY;
    }

	/**
	 * Makes the model more or less transparent
	 */
	public Color getColor() {
		return Color.getWhite();
	}

	/**
	 * Sets the color on the model IF IT HAS ONE. Otherwise no effect.
	 */
	public void setColor( Color c ) {
	}

}
