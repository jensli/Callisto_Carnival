package cc.gui.models;

import cc.util.math.Vec;

public final class EmptyGraphicalModel extends GraphicalModel
{
	protected EmptyGraphicalModel() {
		super( null );
    }

	@Override
    public GraphicalModel makeGroupWith( GraphicalModel model )
	{
	    return model;
    }

	@Override
	public void draw( Vec pos, Vec forward ) {
	// TODO Auto-generated method stub

	}

}
