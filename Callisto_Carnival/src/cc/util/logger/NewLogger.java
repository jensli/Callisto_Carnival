package cc.util.logger;

public class NewLogger implements ILogger
{
	private String placeStr;
	private Logger logger;


	@Override
	public void error( String message ) {
		logger.log( LogPlace.APP, LogLevel.HIGH, placeStr + ": " + message );
	}

	@Override
	public void info( String message ) {

	}

	@Override
	public void warning( String message ) {

	}

}
