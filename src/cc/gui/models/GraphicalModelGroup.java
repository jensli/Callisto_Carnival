package cc.gui.models;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cc.game.GameObject;
import cc.util.Color;
import cc.util.Util;
import cc.util.math.Vec;


public class GraphicalModelGroup extends GraphicalModel
{
	private List<GraphicalModel> modelList = new LinkedList<GraphicalModel>();

	public GraphicalModelGroup(GameObject obj )
	{
		super( obj );
	}

	@Override
	public void draw( Vec pos, Vec forward )
	{
		for ( GraphicalModel model : modelList ) {
			model.draw( pos, forward );
		}

	}

	@Override
    public void update( double dT )
    {
		Iterator<GraphicalModel> itr = modelList.iterator();

		while ( itr.hasNext() ) {

			GraphicalModel model = itr.next();

			model.update( dT );

			if ( model.isFinished() ) {
				itr.remove();
			}
		}

//		for ( GraphicalModel model : modelList ) {
//			model.update( dT );
//
//		}

    }



	@Override
    public Color getColor() {
	    // TODO Auto-generated method stub
	    return modelList.get( 0 ).getColor();
    }

	@Override
    public GraphicalModelGroup makeGroupWith( GraphicalModel model )
	{
		add( model );
		return this;
    }

	public void add( GraphicalModel... models )
	{
		for ( GraphicalModel model : models ) {

			Util.verifyArg( !modelList.contains( model ), "Cannot add a model to a group twise" );
			Util.verifyArg( model != this, "Cannot add a model group to itself" );

			modelList.add( model );
		}
	}



}
