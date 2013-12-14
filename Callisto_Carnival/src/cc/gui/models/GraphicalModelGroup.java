package cc.gui.models;


import j.util.lists.IterCacheList;

import java.util.ArrayList;
import java.util.List;

import cc.game.GameObject;
import cc.util.Color;
import cc.util.math.Vec;


public class GraphicalModelGroup extends GraphicalModel
{
	private List<GraphicalModel> modelList = new ArrayList<GraphicalModel>( 2 );
	private IterCacheList<GraphicalModel> modelIterable = new IterCacheList<GraphicalModel>( modelList );
//	private IndexIterator<GraphicalModel> itr = new IndexIterator<GraphicalModel>( modelList );

	public GraphicalModelGroup(GameObject obj )
	{
		super( obj );
	}

	@Override
	public void draw( Vec pos, Vec forward )
	{
		for ( GraphicalModel model : modelIterable ) {
			model.draw( pos, forward );
		}
	}

	@Override
    public void update( double dT )
    {
//		Iterator<GraphicalModel> itr = modelList.iterator();
//		itr.setToStart();
//
//		while ( itr.hasNext() ) {
//
//			GraphicalModel model = itr.next();
//
//			model.update( dT );
//
//			if ( model.isFinished() ) {
//				itr.remove();
//			}
//		}

		for ( GraphicalModel model : modelIterable ) {
			model.update( dT );

			if ( model.isFinished() ) {
				modelIterable.removeCurrent();
			}
		}

    }


	/**
	 * TODO: Hack warning! Should fix this.
	 */
	@Override
    public Color getColor() {
	    return modelList.get( 0 ).getColor();
    }

	@Override
    public GraphicalModelGroup makeGroupWith( GraphicalModel model )
	{
		add( model );
		return this;
    }


	public void add( GraphicalModel model ) {
		modelList.add( model );
	}

	public void add( GraphicalModel... models )
	{
		for ( GraphicalModel model : models ) {
			add( model );
		}
	}



}
