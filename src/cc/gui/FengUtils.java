package cc.gui;

import j.util.util.Util;

import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import org.fenggui.render.Font;
import org.fenggui.util.fonttoolkit.FontFactory;


public class FengUtils
{
	private FengUtils() {} // Static class
	
	public static Font loadFont( String filename, float size )
	{
		java.awt.Font javaFont = null;
		Font fengFont = null;
		InputStream inputStream = Thread.currentThread().getContextClassLoader()
										.getResourceAsStream( filename );

		if ( inputStream == null ) {
			Util.fileNotFound( "Font file", filename );
		}
		
		try {
			javaFont = java.awt.Font.createFont(  java.awt.Font.TRUETYPE_FONT,  inputStream ) ;
		} catch ( FontFormatException e ) {
			throw new RuntimeException( "Font Format Exception", e );
		} catch ( IOException e ) {
			throw new RuntimeException( "IO Exception while loading font",  e );
		}
		
		javaFont = javaFont.deriveFont( size );
		fengFont = FontFactory.renderStandardFont( javaFont );
		
		return fengFont;
	}

}
