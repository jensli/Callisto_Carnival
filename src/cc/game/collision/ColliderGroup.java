package cc.game.collision;

import java.util.ArrayList;
import java.util.List;

public class ColliderGroup extends DefaultCollider
{
	List<DefaultCollider> colliderList = new ArrayList<DefaultCollider>();
	
//	public void collideMe( GameObject me, GameObject other )
//	{
//		for ( Collider collider : colliderList ) {
//			collider.collideMe( me, other );
//		}
//	}

	public boolean add( DefaultCollider e )
    {
	    return colliderList.add( e );
    }
}
