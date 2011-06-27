package cc.event.game;

import cc.event.Event;
import cc.event.handlers.EventReceiver;
import cc.game.GameObject;


public class CreateEvent extends Event
{
	private GameObject objectToCreate = null;

	// Future versions may use this, right now the variable abow is used
	@SuppressWarnings("all")
	private String serializedObject = null;


	private final Cathegory type = Cathegory.GAME;


	public CreateEvent(int newReceiverID)
	{
		super( newReceiverID );
	}

	public CreateEvent(GameObject newObjectToCreate)
	{
		this( Event.NO_RECEIVER );
		objectToCreate = newObjectToCreate;
	}

	public GameObject getObject()
    {
    	return objectToCreate;
    }

	public void setSerializedObject( String serializedObject )
    {
    	this.serializedObject = serializedObject;
    }

	@Override
	public void dispatch( EventReceiver receiver )
	{
		receiver.receiveCreateEvent( this );
	}

	@Override
    public Cathegory getType()
    {
    	return type;
    }
}