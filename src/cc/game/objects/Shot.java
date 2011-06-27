package cc.game.objects;

import cc.game.GameObject;
import cc.game.ObjectCathegory;

public class Shot extends GameObject
{
	// The obj firing this shot, to give points the that player
	private GameObject firingObject;

    {
    	setCathegory( ObjectCathegory.SHOT );
    }

	public Shot( String name )
	{
	    super( name );
    }

	public GameObject getFireingObject() {
    	return firingObject;
    }

//	@Override
//    public void receiveKillEvent( KillEvent event ) {
//		addExtraBehavior( DeathFaderBehavior.make() );
////	    super.receiveKillEvent( event );
//    }

}
