package cc.event;

import j.util.eventhandler.GroupName;
import cc.event.handlers.EventReceiver;
import cc.event2.EventGroups;

/**
 * Singnals that a player should be added to the game
 */
public class JoinEvent extends Event
{

	String nick = "no nick";
	boolean isBot = false;
	boolean isMe = false;
	private static final Cathegory type = Cathegory.NETWORK;

	public JoinEvent( int playerID, String nick, boolean isBot, boolean isMe )
	{
		super( playerID );
		setName( Event.JOIN );

		this.nick = nick;
		this.isBot = isBot;
		this.isMe = isMe;
	}

	@Override
	public void deserialize( String parameters )
	{
		super.deserialize(parameters);
		String parameter[] = parameters.split(" ");
		this.nick = parameter[3];
		this.isBot = Boolean.valueOf(parameter[4]);
		this.isMe = Boolean.valueOf(parameter[5]);
	}

//	public Object clone()
    @Override
    public JoinEvent clone()
    {
        JoinEvent copy = (JoinEvent)super.clone();
//        copy.nick = this.nick;
//        copy.isMe =
//        copy.isBot = this.isBot;
        return copy;
    }

	@Override
    public String serialize()
	{
		return super.serialize() + " " + this.nick + " " + this.isBot + " " + this.isMe;
	}

	@Override
    public void dispatch( EventReceiver receiver )
    {
	    receiver.receiveJoinEvent( this );
    }

	@Override
    public Cathegory getType() {
		return type;
	}
	public String getNick() {
		return nick;
	}
	public boolean isBot() {
		return isBot;
	}
	public boolean isMe() {
		return isMe;
	}
	public void setMe( boolean isMe ) {
		this.isMe = isMe;
	}


	@Override
	public GroupName getReceiverGroup() {
		return EventGroups.JOIN;
	}
}
