package cc.event;

import java.util.Collection;

import cc.event.handlers.EventReceiver;
import cc.game.GameObject;
import cc.game.Player;
import cc.gui.Drawable;


/**
 * Event sent when its time to reset the view to a new space object,
 * mostley when the game starts or when the player respawns.
 */

public class GuiResetEvent extends Event
{
	private static final Cathegory type = Cathegory.GUI;

	private Player focusPlayer;
	// This is just thee different way to do the same thing,
	// experimenting
	private Collection<GameObject> objectList;
	private Collection<? extends Drawable> objectList2;




	public GuiResetEvent( Player focusPlayer, Collection<GameObject> objectList,
			Collection<? extends Drawable> objectList2 )
    {
	    super();
	    this.focusPlayer = focusPlayer;
	    this.objectList = objectList;
	    this.objectList2 = objectList2;

    }

	@Override
    public Cathegory getType()
    {
		return type;
    }

	@Override
    public void dispatch( EventReceiver receiver )
    {
		receiver.receiveGuiResetEvent( this );
    }

	public Player getFocusPlayer()
    {
    	return focusPlayer;
    }

	public Collection<GameObject> getObjectList()
    {
    	return objectList;
    }
	public Collection<? extends Drawable> getObjectList2()
	{
		return objectList2;
	}


}
