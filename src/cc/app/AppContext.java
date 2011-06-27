package cc.app;

import j.util.eventhandler.EventHandler;
import cc.event.handlers.IEventHandler;
import cc.util.logger.Logger;

public class AppContext
{
	private IEventHandler eventHandler;
	private EventHandler eventHandlerNew;
	private Logger logger;



	public AppContext( IEventHandler eventHandler, EventHandler eventHandlerNew, Logger logger ) {
	    this.eventHandler = eventHandler;
	    this.eventHandlerNew = eventHandlerNew;
	    this.logger = logger;
    }
	public IEventHandler getEventHandler() {
    	return eventHandler;
    }
	public EventHandler getEventHandlerNew() {
    	return eventHandlerNew;
    }
	public Logger getLogger() {
    	return logger;
    }
}
