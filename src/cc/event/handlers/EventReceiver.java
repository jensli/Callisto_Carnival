package cc.event.handlers;


import cc.event.Event;
import cc.event.GuiResetEvent;
import cc.event.JoinEvent;
import cc.event.QuitEvent;
import cc.event.StandardEvent;
import cc.event.StandardValueEvent;
import cc.event.TickEvent;
import cc.event.game.CollisionEvent;
import cc.event.game.CreateEvent;
import cc.event.game.FireEvent;
import cc.event.game.KillEvent;
import cc.event.game.RotateEvent;
import cc.event.game.ThrustEvent;


/**
 * Event receiver superclass. Every class that wants to receive events must extend this
 * class and override the .reciveEvent() method.
 * 
 * It also have to override one or more execute???Event() method(s), corresponding
 * to the events it wants to receive.
 */
public abstract class EventReceiver
{
	abstract public void receiveEvent(Event event);
	
	public void receiveJoinEvent( JoinEvent event ) {} 
	public void receiveKillEvent( KillEvent event )	{}
	public void receiveCreateEvent( CreateEvent event ) {}
	public void receiveCollisionEvent( CollisionEvent event ) {}
	public void receiveRotateEvent( RotateEvent event ) {}
	public void receiveTickEvent( TickEvent event ) {}
	public void receiveFireEvent( FireEvent event ) {}
	public void receiveThrustEvent( ThrustEvent event ) {}
	public void receiveGuiResetEvent( GuiResetEvent event ) {}
	public void receiveQuitEvent( QuitEvent event ) {}
	
	
	public void receiveJoinMultiplayerEvent( StandardValueEvent<String> event ) {}
	public void receiveZoomEvent( StandardValueEvent<Integer> event ) {}
	public void receiveRequestEvent( StandardValueEvent<Event> event ) {}
	public void receiveJoinNotification( StandardValueEvent<String> event ) {}
	
	public void receiveStartMultiplayerEvent( StandardEvent event ) {}
	public void receiveExitProgramEvent( StandardEvent event ) {}
	public void receiveResetEvent( StandardEvent event ) {}
	public void receivePauseEvent( StandardEvent event ) {}
	public void receiveHostGameEvent( StandardEvent event ) {}
	public void receiveCancelHostGameEvent( StandardEvent event ) {}
	public void receiveCancelJoinMultiplayerEvent( StandardEvent event ) {}
	public void receiveStartSingelplayerEvent( StandardEvent event ) {}
}


