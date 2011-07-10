package cc.app;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import cc.app.StartArgs.CommandLineError;
import cc.event2.EventGlobals;
import cc.game.GameFactory;
import cc.gui.Graphics;
import cc.util.CcUtil;
import cc.util.logger.LogLevel;
import cc.util.logger.LogPlace;
import cc.util.logger.LogType;
import cc.util.logger.Logger;


public class Bootstrapper
{
	private RunMode runMode = RunMode.NORMAL;

	// For the fullscreen query popup dialog
	public final DoFullscreen STANDARD_FULLSCREEN_OPT = DoFullscreen.NO;
	private boolean queryFullscreen = false;


	private enum DoFullscreen {
		CANCEL, YES, NO,
	}

	private enum RunMode {
		DEBUG, NORMAL,
	}


	private String[] args;

	private int
		screenWidth = 1024,
		screenHeight = 768;

	public static final String NO_FULLSCREEN_ARG = "-no_fullscreen";


	public Bootstrapper( String[] args )
	{
		this.args = args;
	}

	public void initProgram( boolean isFullscreen, int screenWidth, int screenHeight )
	{
		EventGlobals.init();

		Logger.get().log( LogPlace.APP, LogLevel.HIGH, "Starting program" );

		// The order here is important! First of all the Display has to be initialized.
		Graphics.get().initDisplay( "Callisto Carnival", isFullscreen, screenWidth, screenHeight );
		Graphics.get().initGL();
	}

	public DoFullscreen queryFullscreen()
	{
		if ( !queryFullscreen ) {
			return STANDARD_FULLSCREEN_OPT;
		}

		switch ( JOptionPane.showConfirmDialog( null,
				"Do you want to run the game in fullscreen mode (recommended)?",
				"Callisto Carnival", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE ) ) {
			case JOptionPane.NO_OPTION: return DoFullscreen.NO;
			case JOptionPane.YES_OPTION: return DoFullscreen.YES;
			default: return DoFullscreen.CANCEL;
		}

	}

	public void resetGlobalState()
	{
		GameFactory.reset();
	}

	public ClientApp runNoCatch() throws CommandLineError
	{
		Logger.initTerminalLogger( LogLevel.LOW );

		StartArgs appArgs = new StartArgs();
		appArgs.parseCommandListArgs( args );

		switch ( queryFullscreen() ) {
			case CANCEL: return null;
			case YES: appArgs.setFullscreen( true ); break;
			case NO: appArgs.setFullscreen( false ); break;
		}

		initProgram( appArgs.isFullscreen(), screenWidth, screenHeight );

		ClientApp app = null;

		while ( true ) {

			app = new ClientApp( appArgs );
			ProgramStateCode code = app.run();

			if ( code.isStopping() ) {
				break;
			}

			resetGlobalState();
		}

		return app;
	}


	public void runCatch()
	{
		ClientApp app = null;

		try {

			app = runNoCatch();

		} catch ( StartArgs.CommandLineError exc ) {
			Logger.get().log( LogPlace.APP, LogType.ERROR, LogLevel.HIGH, exc.getMessage() + " Exiting." );
		} catch ( Throwable tbl ) {
			System.err.println( "The program as encountered an irrecoverable error and will exit.\n Details:" );
			tbl.printStackTrace();
        } finally {
        	CcUtil.dispose( app );
		}
	}

	public void run() throws Exception
	{
		switch ( runMode ) {
			case DEBUG:
				runNoCatch();
				break;
			case NORMAL:
				runCatch();
				break;
		}
	}

	public static void main(String[] args) throws Exception
	{
		Bootstrapper b = new Bootstrapper( args );

		b.run();
	}

}


class StartArgs
{
	private boolean isFullscreen = false;

	public void parseCommandListArgs( String[] args ) throws CommandLineError
    {
    	List<String> errorList = new LinkedList<String>();

    	for ( String str : args ) {
    		if ( str.equals( Bootstrapper.NO_FULLSCREEN_ARG ) ) {
    			this.setFullscreen( false );
    		} else {
    			errorList.add( str );
    		}
    	}

    	if ( !errorList.isEmpty() ) {
    		throw new CommandLineError( errorList );
    	}
    }

	public void setFullscreen( boolean isFullscreen ) {
	    this.isFullscreen = isFullscreen;
    }

	public boolean isFullscreen() {
	    return isFullscreen;
    }

	public class CommandLineError extends Exception
	{
        private static final long serialVersionUID = 1L;

		public CommandLineError( List<String> errors )
		{
			super( "Error parsing commandline args. Illegal args: " + errors );
		}
	}
}


