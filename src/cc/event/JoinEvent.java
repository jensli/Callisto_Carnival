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

	public JoinEvent( int playerID, String nick, boolean isBot, boolean isMe )
	{
		super( playerID );

		this.nick = nick;
		this.isBot = isBot;
		this.isMe = isMe;
	}

    @Override
	public void toStringBuilder( StringBuilder b ) {
    	super.toStringBuilder( b );
    	b.append( " " )
    	.append( nick ).append( " " )
    	.append( isBot ).append( " " )
    	.append( isMe );
	}

	@Override
	protected int setFields( String[] data ) {
		int i = super.setFields( data );
		this.nick = data[ i++ ];
		this.isBot = Boolean.parseBoolean( data[ i++ ] );
		this.isMe = Boolean.parseBoolean( data[ i++ ] );
		return i;
	}


	@Override
    public void dispatch( EventReceiver receiver )
    {
	    receiver.receiveJoinEvent( this );
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

	@Override public String getName() {
		return Event.JOIN;
	}

}
