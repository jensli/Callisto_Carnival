package cc.app;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import cc.event.EventFactory;
import cc.event.handlers.EventHandler;
import cc.event2.EventGlobals;
import cc.game.GameFactory;
import cc.gui.Graphics;
import cc.util.logger.LogLevel;
import cc.util.logger.LogPlace;
import cc.util.logger.LogType;
import cc.util.logger.Logger;


public class Bootstrapper
{
	private boolean queryFullscreen = false;

	// For the fullscreen querry popup dialog
	public final int CANCEL = 0, FULLSCREEN = 1, NO_FULLSCREEN = 2;
	public final int STANDARD_FULLSCREEN_OPT = NO_FULLSCREEN;

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

		EventFactory.makeEventTypes();

		Logger.get().log( LogPlace.APP, LogLevel.HIGH, "Starting program" );

		// The order here is important! First of all the Display has to be initialized.
		Graphics.get().initDisplay( "Callisto Carnival", isFullscreen, screenWidth, screenHeight );
		Graphics.get().initGL();
	}

	public int queryFullscreen()
	{
		if ( !queryFullscreen ) {
			return STANDARD_FULLSCREEN_OPT;
		}

		switch ( JOptionPane.showConfirmDialog( null,
				"Do you want to run the game in fullscreen mode (recommended)?",
				"Callisto Carnival", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE ) ) {
			case JOptionPane.NO_OPTION: return NO_FULLSCREEN;
			case JOptionPane.YES_OPTION: return FULLSCREEN;
			default: return CANCEL;
		}

	}

	public void resetGlobalState()
	{
		EventHandler.reset();
		GameFactory.reset();
	}

	public void run()
	{
		ClientApp app = null;

		try {

			Logger.initTerminalLogger( LogLevel.LOW );

			StartArgs appArgs = new StartArgs();
			appArgs.parseCommandListArgs( args );

			switch ( queryFullscreen() ) {
				case CANCEL: return;
				case FULLSCREEN: appArgs.setFullscreen( true ); break;
				case NO_FULLSCREEN: appArgs.setFullscreen( false ); break;
			}

			initProgram( appArgs.isFullscreen(), screenWidth, screenHeight );

			while ( true ) {

				app = new ClientApp( appArgs );
				ProgramStateCode code = app.run();

				if ( code.isStopping() ) {
					break;
				}

				resetGlobalState();
			}

		} catch ( StartArgs.CommandLineError exc ) {
			Logger.get().log( LogPlace.APP, LogType.ERROR, LogLevel.HIGH, exc.getMessage() + " Exiting." );
        }
		// Comment out to avoid catching exceptions while debugging
//		finally {
//        	Util.dispose( app );
//		}

	}

	public static void main(String[] args)
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


