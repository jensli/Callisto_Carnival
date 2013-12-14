package cc.util.logger;

public class PlacedLogger extends Logger
{
	private Logger logger;
	private LogPlace place;
	private LogLevel level;

	public PlacedLogger( Logger logger, LogPlace place ) {
		this( logger, place, LogLevel.MED );
    }

	public PlacedLogger( Logger logger, LogPlace place, LogLevel level ) {
	    this.logger = logger;
	    this.place = place;
	    this.level = level;
    }

	public void info( String message )
	{
		logger.log( place, LogType.INFO, level, message );
	}
	public void warn( String message )
	{
		logger.log( place, LogType.WARNING, level, message );
	}
	public void error( String message )
	{
		logger.log( place, LogType.ERROR, level, message );
	}

	@Override
    protected void printLogging( LogPlace place, LogType type, String str ) {
	    logger.printLogging( place, type, str );
    }

}
