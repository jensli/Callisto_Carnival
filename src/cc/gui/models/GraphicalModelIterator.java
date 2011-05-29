package cc.gui.models;


import java.util.Collection;
import java.util.Iterator;

import cc.game.GameObject;

public class GraphicalModelIterator implements Iterator<GraphicalModel>
{
	private Iterator<GameObject> itr;
	Collection<GameObject> objectList;
	private boolean lastIsValid = false;
	
	public GraphicalModelIterator( Collection<GameObject> objectList ) {
		this.objectList = objectList;
	}
	
	public boolean hasNext() {
		return itr.hasNext();
	}
	
	public GraphicalModel next() {
		lastIsValid = itr.hasNext();
		return lastIsValid ? itr.next().getGraphicalModel() : null;
	}
	
	public void remove() {
		throw new RuntimeException( "Remove not supported." );
	}
	
	public boolean isValid() {
		return lastIsValid;
	}
	
	public GraphicalModel first()
	{
 		itr = objectList.iterator();
 		return this.next();
	}
	
}
