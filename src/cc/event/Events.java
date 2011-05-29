package cc.event;

import java.util.Map;

/**
 * Factory for anonymous event classes and for event objects from those classes.
 * Written to make standard events without having a large number of named event
 * classes bloating the applicatoin.
 * 
 * Depends on a enum class, EventType, to define the different event classes.
 * 
 * @author jens
 */

public class Events<TYPE_TYPE>
{
	private Map<TYPE_TYPE, StandardEvent> eventMap;
	@SuppressWarnings("unchecked")
	private Map<TYPE_TYPE, StandardValueEvent> valueEventMap;
//	private Map<EventType, StandardEvent> eventMap = new EnumMap<EventType, StandardEvent>( EventType.class );
//	@SuppressWarnings("unchecked")
//	private Map<EventType, StandardValueEvent> valueEventMap = new EnumMap<EventType, StandardValueEvent>( EventType.class );

//	public enum Type {
//		HOST_GAME, START_SINGELPLAYER,
//		ZOOM, PAUSE,
//		EXIT_PROGRAM
//	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public Events( Map<TYPE_TYPE, StandardEvent> eventMap, Map<TYPE_TYPE, StandardValueEvent> valueEventMap )
    {
	    super();
	    this.eventMap = eventMap;
	    this.valueEventMap = valueEventMap;
    }
	
	
	public StandardEvent makeEvent( TYPE_TYPE c )
	{
		return eventMap.get( c ).clone();
	}
	public <T> StandardValueEvent<T> makeEvent( TYPE_TYPE type, T value )
	{
		@SuppressWarnings("unchecked")
		StandardValueEvent<T> event = valueEventMap.get( type ).clone();
		event.setValue( value );
		return event;
	}
	
	public void makeEventType( TYPE_TYPE evType, Event.Cathegory type, StandardEvent event ) {
		event.setType( type );
		eventMap.put( evType, event );
	}
	
	@SuppressWarnings("unchecked")
	public void makeEventType( TYPE_TYPE type, Event.Cathegory cat, StandardValueEvent event ) {
		event.setType( cat );
		valueEventMap.put( type, event );
	}
	
	public static Event cloneEvent( Event event ) {
		return event.clone();
	}
}






//
//public class Events
//{
////	private static Events instance = new Events();
////	
////	public Events get() {
////		return instance;
////	}
//	
//	private static Map<EventType, StandardEvent> eventMap = new EnumMap<EventType, StandardEvent>( EventType.class );
//	@SuppressWarnings("unchecked")
//	private static Map<EventType, StandardValueEvent> valueEventMap = new EnumMap<EventType, StandardValueEvent>( EventType.class );
//
////	public enum Type {
////		HOST_GAME, START_SINGELPLAYER,
////		ZOOM, PAUSE,
////		EXIT_PROGRAM
////	}
//	
//	public static StandardEvent makeEvent( EventType c )
//	{
//		return eventMap.get( c ).clone();
//	}
//	public static <T> StandardValueEvent<T> makeEvent( EventType type, T value )
//	{
//		@SuppressWarnings("unchecked")
//		StandardValueEvent<T> event = valueEventMap.get( type ).clone();
//		event.setValue( value );
//		return event;
//	}
//	
//	public static void makeEventType( EventType evType, Event.Cathegory type, StandardEvent event ) {
//		event.setType( type );
//		eventMap.put( evType, event );
//	}
//	
//	@SuppressWarnings("unchecked")
//	public static void makeEventType( EventType type, Event.Cathegory cat, StandardValueEvent event ) {
//		event.setType( cat );
//		valueEventMap.put( type, event );
//	}
//	
//	public static Event cloneEvent( Event event ) {
//		return event.clone();
//	}
//
//	static {
////		valueEventMap = new EnumMap<Type, StandardValueEvent>( EventType.class );
////		eventMap = new EnumMap<EventType, StandardEvent>( EventType.class );
//	}
//}
