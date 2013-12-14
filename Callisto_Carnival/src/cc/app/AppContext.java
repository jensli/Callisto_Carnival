package cc.app;

import j.util.eventhandler.EventHandler;
import cc.util.logger.Logger;

/**
 * The objects are passed around between the application objects, carrying
 * data that is common to them. That could be global but is here instead.
 */
public class AppContext
{
	private EventHandler eventHandlerNew;
	private Logger logger;

	public AppContext( EventHandler eventHandlerNew, Logger logger ) {
	    this.eventHandlerNew = eventHandlerNew;
	    this.logger = logger;
    }

	public EventHandler getEventHandler() {
    	return eventHandlerNew;
    }

	public Logger getLogger() {
    	return logger;
    }
}
