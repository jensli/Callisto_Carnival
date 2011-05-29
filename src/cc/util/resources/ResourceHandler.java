package cc.util.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.util.BitmapFont;
import cc.util.Texture;
import cc.util.sound.Sound;
import cc.util.sound.SoundLoader;



/**
 * Singelton Resource mangager. Methods to load textures, sounds, fonts etc
 * @author jens
 *
 */
public class ResourceHandler
{
	private TextureLoader textureLoader;
	private SoundLoader soundLoader;
	private List<BitmapFont> fontList;
	
	private static ResourceHandler instance = new ResourceHandler();
	
	private ResourceHandler()
	{
		textureLoader = TextureLoader.get();
		soundLoader = SoundLoader.get();
		
		fontList = new ArrayList<BitmapFont>();
		fontList.add( new BitmapFont( BitmapFont.FONT1, this.getTexture( Name.FONT1 ), 32, 32 ) );
		fontList.get( 0 ).setFontSize( 0.0007f );
	}
	
	public BitmapFont getFont( int fontNr ) {
		return fontList.get( fontNr );
	}
	
	public static ResourceHandler get()
	{
		return instance;
	}
	
	public Texture getTexture( String filename )
	{
		Texture texture = null;
//		filename = path + filename;
		
		try {
	        texture = textureLoader.getTexture( filename );
//	        Logger.get().log( "Loading texture " + filename );
        } catch ( IOException e ) {
        	// Would be nice to uses those, but creates NullPointer
//        	Logger.get().log( Logger.WARNING, "Could not load texture: " + filename );
//        	Logger.get().log( Logger.WARNING, "Could not load texture: " + e.getMessage() );
			throw new RuntimeException( "Could not load texture: " + filename, e );
        }
        
        return texture;
	}
	
	public List<Texture> getTextures( String filename ) 
	{
		String[] arr = filename.split( "\\*" );
		List<Texture> list = new ArrayList<Texture>();
		
		if ( arr.length != 2 ) {
			throw new IllegalArgumentException( filename + " can not be used to load textues.");
		}
		
		int i = 1;
//		File f;
		while ( true ) {
			
			String nameWithNumber = arr[0] + i + arr[1];
			
//			f = new File( nameWithNumber );
			
			if ( !textureLoader.imageExists( nameWithNumber ) )  {
				break;
			}
			
			list.add( this.getTexture( nameWithNumber ) );
			i++;
		}
		
		if ( list.isEmpty() ) {
			throw new RuntimeException("Loding textures failed. No files matching \"" + filename + " found.");
		}
	
		return list;
	}
	
	
	public Sound getSound( String filename )
	{
		Sound sound;
		
		try {
	        sound = soundLoader.getOgg( filename );
//	        Logger.get().log( "Loading sound " + filename );
        } catch ( IOException e ) {
	        throw new RuntimeException("Could not load sound: " + filename, e );
        }
		
		return sound;
	}
	
	
}
