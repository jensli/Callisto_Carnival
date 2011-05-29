package cc.util.logger;


import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.EnumSet;

/**
 * Singleton class used to log program info, either to stdout, a file or something else.
 * Messages logged either as info, warning or error.
 * an init() method must be called before use, finish() method should be called before program exit.
 * 
 * @author jens
 */

public abstract class Logger
{
	private static Logger instance;
	private static boolean initPerformed = false;
	
	// How much logging should performed, how important a message should be
	// to be printed.
	private LogLevel logLevel = LogLevel.LOW;
	private EnumSet<LogPlace> cathegorys = EnumSet.allOf( LogPlace.class );
	
	// Log level levels
//	public static final int LOW = 1, MED = 2, HIGH = 3;
//	public static final int ALL = Integer.MAX_VALUE, NET = 1, APP = 2, GAME = 4, GUI = 8, LOAD = 16;
//	public static final String
//		ERROR = "Error: ",
//		WARNING = "Warning: ",
//		INFO = "Info: ";
		
	/**
	 * Init the logger with a Logger object allready created.
	 * Tought to be called from the other init() methods.
	 */
	private static void init( Logger logger, LogLevel level ) 
	{
		if ( initPerformed ) {
			logger.dispose();
			throw new Error( "Init allready performed." );
		}
		logger.logLevel = level;
		instance = logger;
		initPerformed = true;
	}
	/**
	 * Init the logger to write to the file streamFilename
	 */
	public static void initFileLogger( String streamFilename, LogLevel level )
	{
		try {
	        init( new StreamLogger( new PrintStream( streamFilename ) ), level );
        } catch ( FileNotFoundException e ) {
        	initTerminalLogger( level );
        	Logger.get().log( LogPlace.APP, LogType.ERROR, "Log file could not be created, logging to stdout" );
        }
	}
	/**
	 * Init the logger to write to stdout
	 */
	public static void initTerminalLogger( LogLevel level )
	{
		init( new StreamLogger( System.out ), level );
	}
	
	public void setCathegors( LogPlace... t1 )
	{
		cathegorys = EnumSet.copyOf( Arrays.asList( t1 ) );
	}
	/**
	 * Init the logger to not do anything when it is told to log things
	 */
	public static void initDisabledLogger() {
		init( new EmptyLogger(), LogLevel.DISABLED );
	}
	
	public static Logger get()
	{
		if ( !initPerformed ) {
			throw new IllegalStateException( "Init not performed, call init() method first." );
		}
		return instance;
	}
	
	
	protected abstract void printLogging( LogPlace place, LogType type, String str );
	
	/**
	 * Logges str as type type and level level 
	 */
	public void log( LogPlace place, LogType type, LogLevel level, String str )
	{
			if ( level.ordinal() >= logLevel.ordinal() && cathegorys.contains( place ) ) {
				printLogging( place, type, str ); // Call prototype method in subclass
			}
	}
	
	/**
	 * Log function with different default values 
	 */
	public void log( LogPlace place, LogType type, String str ) {
		log( place, type, LogLevel.MED, str );
	}
	public void log( LogPlace place, LogLevel level, String str ) {
		log( place, LogType.INFO, level, str );
	}
	public void log( LogPlace place, String str ) {
		log( place, LogType.INFO, LogLevel.LOW, str );
	}
	
//	public void info( String message ) 
//	{
//		log( LogPlace.APP, LogType.INFO, LogLevel.MED, message );
//	}
	
	public void info( LogPlace place, String message ) 
	{
		log( place, LogType.INFO, LogLevel.MED, message );
	}
	public void warn( LogPlace place, String message ) 
	{
		log( place, LogType.WARNING, LogLevel.MED, message );
	}
	public void error( LogPlace place, String message ) 
	{
		log( place, LogType.ERROR, LogLevel.MED, message );
	}

	public PlacedLogger createPlaced( LogPlace place ) {
		return new PlacedLogger( this, place );
	}
	
	public void setLogLevel( LogLevel level ) {
		this.logLevel = level;
	}
	
	/**
	 * To e.g. close stream to log file 
	 */
	public void dispose() 
	{}
}

/**
 * Writes log messages to a stream. stdout or a file stream.
 * @author jens
 */
class StreamLogger extends Logger
{
	private PrintStream stream; 

//		StreamLogger( String streamFilename, int level ) throws FileNotFoundException {
//			stream = new PrintStream( new File( streamFilename ) );
//		}
	StreamLogger( PrintStream outStream ) {
		stream = outStream;
	}

	@Override
    public void dispose() 
	{
		if ( stream != System.out && stream != System.err ) {
			stream.close();
		}
	    super.dispose();
    }

	@Override
    protected void printLogging( LogPlace place, LogType type, String str )
	{
		stream.println( place + " " + type + ": " + str );
	}
	

}



/**
 * Logger that dont acctually do anything, if we dont want logging
 * @author jens
 */
class EmptyLogger extends Logger
{
    @Override
    public void printLogging( LogPlace place, LogType type, String str ) {
    	; // Empty, dont write any log messages
    }
}

