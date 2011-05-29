package cc.util.logger;

import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * Singleton class used to log program info, either to stdout, a file or something else.
 * Messages logged either as info, warning or error.
 * an init() method must be called before use, finish() method should be called before program exit.
 * 
 * @author jens
 */

public abstract class Logger_old
{
	private static Logger_old instance;
	private static boolean initPerformed = false;
	
	// How much logging should performed, how important a message should be
	// to be printed.
	private int logLevel = MED;
	
	// Log level levels
	public static final int LOW = 1, MED = 2, HIGH = 3;
	public static final int ALL = Integer.MAX_VALUE, NET = 1, APP = 2, GAME = 4, GUI = 8, LOAD = 16;
	
	
	public static final String
		ERROR = "Error: ",
		WARNING = "Warning: ",
		INFO = "Info: ";
		
	/**
	 * Init the logger with a Logger object allready created.
	 * Tought to be called from the other init() methods.
	 */
	private static void init( Logger_old logger ) 
	{
		if ( initPerformed ) {
			logger.finish();
			throw new Error( "Init allready performed." );
		}
		instance = logger;
		initPerformed = true;
	}
	/**
	 * Init the logger to write to the file streamFilename
	 */
	public static void initFileLogger( String streamFilename, int level )
	{
		try {
	        init( new StreamLogger_old( new PrintStream( streamFilename ), level ) );
        } catch ( FileNotFoundException e ) {
        	initTerminalLogger( level );
        	Logger_old.get().log( Logger_old.ERROR, "Log file could not be created, logging to stdout" );
        }
	}
	/**
	 * Init the logger to write to stdout
	 */
	public static void initTerminalLogger( int level )
	{
		init( new StreamLogger_old( System.out, level ) );
	}
	
	/**
	 * Init the logger to not do anything when it is told to log things
	 */
	public static void initDisabledLogger() {
		init( new EmptyLogger_old() );
	}
	
	public static Logger_old get()
	{
		if ( !initPerformed ) {
			throw new IllegalStateException( "Init not performed, call init() method first." );
		}
		return instance;
	}
	
	protected abstract void printLogging( String type, String str );
	
	/**
	 * Logges str as type type and level level 
	 */
	public void log( String type, int level, String str )
	{
		if ( level >= logLevel ) {
			printLogging( type, str ); // Call prototype method in subclass
		}
	}
	
	/**
	 * Log function with different default values 
	 */
	public void log( String type, String str ) {
		log( type, Logger_old.MED, str );
	}
	public void log( int level, String str ) {
		log( Logger_old.INFO, level, str );
	}
	public void log( String str ) {
		log( Logger_old.INFO, Logger_old.MED, str );
	}
	public void setLogLevel( int level ) {
		this.logLevel = level;
	}
	
	/**
	 * To e.g. close stream to log file 
	 */
	public void finish() 
	{}
}

/**
 * Writes log messages to a stream. stdout or a file stream.
 * @author jens
 */
class StreamLogger_old extends Logger_old
{
	private PrintStream stream; 

//	StreamLogger( String streamFilename, int level ) throws FileNotFoundException {
//		stream = new PrintStream( new File( streamFilename ) );
//	}
	StreamLogger_old( PrintStream outStream, int level ) {
		stream = outStream;
	}
	
	@Override
    public void finish() 
	{
		if ( stream != System.out && stream != System.err ) {
			stream.close();
		}
	    super.finish();
    }

	@Override
    protected void printLogging( String type, String str )
	{
		stream.println( type + str );
	}

}

/**
 * Logger that dont acctually do anything, if we dont want logging
 * @author jens
 */
class EmptyLogger_old extends Logger_old
{
    @Override
    public void printLogging( String type, String str ) {
    	; // Empty, dont write any log messages
    }
}






