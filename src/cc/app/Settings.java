package cc.app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import cc.util.logger.LogPlace;
import cc.util.logger.LogType;
import cc.util.logger.Logger;
import cc.util.resources.Name;



/**
 * Stores the game's settings in an external file.
 *
 * Gets, sets and messes about in a file "Game.ini", before pending changes to the file
 * are commited store needs to be called.
 */

public class Settings extends Properties
{

	private static final long serialVersionUID = 1;

	private static Settings instance = new Settings();

	private Settings()
	{
		try {

//			load( Thread.currentThread().getContextClassLoader().getResourceAsStream( Name.SETTINGS_FILE ) );

			load( new FileInputStream( Name.SETTINGS_FILE ) );
		} catch ( IOException e ) {
			setDefaults();
			store();
		}
	}

	/**
	 * Return an instance of the Settings class.
	 */
	public static Settings getInstance() {
		return instance;
	}

	/**
	 * Reset the settings to their default values.
	 */
	private void setDefaults()
	{
		setProperty( "last_ip", "127.0.0.1" );
	}

	/**
	 * Commit the pending changes and store the settings to a file "Game.ini".
	 *
	 * Note: This needs to be done every time a property has been set.
	 * It is the equivialent of flushing an output stream.
	 */
	public void store()
	{
		try {
			store( new FileOutputStream( Name.SETTINGS_FILE ), "" );
		} catch (Exception e) {
			Logger.get().log( LogPlace.APP, LogType.ERROR, "Error while saving settings" );
		}
	}

	/**
	 * Get value of property with boolean value
	 */
	public boolean getProperty( String key, boolean value )
	{
		return Boolean.valueOf( getProperty( key, ""+value ) ).booleanValue();
	}

	/**
	 * Get value of property with int value
	 */
	public int getProperty(String key, int value)
	{
		return Integer.valueOf( getProperty(key, ""+value) ).intValue();
	}

	/**
	 * Set's the property at "key" to "value"
	 */
	public void setProperty( String key, boolean value )
	{
		setProperty(key,""+value);
	}

	/**
	 * Set's the property at "key" to "value"
	 */
	public void setProperty(String key, int value )
	{
		setProperty(key,""+value);
	}

}
