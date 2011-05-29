package cc.util.resources;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple class to store filename constants.
 * @author jens
 */

public class Name
{
	public static final String
		SETTINGS_FILE = "Game.ini",
		LAST_IP = "last_ip"
		;
	
	public static List<String> images = new ArrayList<String>();
	
	public static final String 
		FOLDER = "res/",
		PIC_TYPE1 = ".png",
	//	SHIP_YELLOW = makeImageName( "ship_yellow" ),
		SHIP_YELLOW = makeImageName( FOLDER,  "ship_yellow" , PIC_TYPE1 ),
		SHIP_GREEN = makeImageName( FOLDER,  "ship_green" , PIC_TYPE1 ),
		SHIP_BLUE = makeImageName( FOLDER,  "ship_blue" , PIC_TYPE1 ),
		SHIP_RED = makeImageName( FOLDER,  "ship_red" , PIC_TYPE1 ),
		VENUS = makeImageName( FOLDER,  "venus2" , PIC_TYPE1 ),
		MERCURY = makeImageName( FOLDER,  "mercury" , PIC_TYPE1 ),
		EARTH = makeImageName( FOLDER,  "earth_atm" , PIC_TYPE1 ),
		MARS = makeImageName( FOLDER,  "mars" , PIC_TYPE1 ),
		MOON = makeImageName( FOLDER,  "moon" , PIC_TYPE1 ),
		H_COMET = makeImageName( FOLDER,  "haleys_comet" , PIC_TYPE1, false ),
		PHOBOS = makeImageName( FOLDER,  "phobos" , PIC_TYPE1 ),
		STONE1 = makeImageName( FOLDER,  "asteroid1" , PIC_TYPE1 ),
		STONE2 = makeImageName( FOLDER,  "asteroid2" , PIC_TYPE1 ),
		STNOE3 = makeImageName( FOLDER,  "asteroid3" , PIC_TYPE1 ),
		SUN = makeImageName( FOLDER,  "sun" , PIC_TYPE1 ),
		SHOT_01 = makeImageName( FOLDER,  "shot_1" , PIC_TYPE1 ),
		LASER_BEAM_1 = makeImageName( FOLDER,  "laser_1" , PIC_TYPE1 ),
		FONT2 = makeImageName( FOLDER,  "font" , PIC_TYPE1 ),
		FONT1 = makeImageName( FOLDER,  "space_font" , PIC_TYPE1 ),
		RADAR = makeImageName( FOLDER,  "radar" , PIC_TYPE1 ),
		BACKGROUND_STARS = makeImageName( FOLDER,  "stars_seemless" , PIC_TYPE1 ),
		FLAME_PARTICLE = makeImageName( FOLDER,  "flame_particle" , PIC_TYPE1 ),
		LIGHT_01 = makeImageName( FOLDER,  "light01" , PIC_TYPE1 ),
		LARGE_EXP_ANIM = makeImageName( FOLDER,  "large_exp_*" , PIC_TYPE1, false ),
		STATION1 = makeImageName( FOLDER,  "mir3" , PIC_TYPE1 ),
		STATION2 = makeImageName( FOLDER,  "newconstantinople" , PIC_TYPE1 ),
		SPLASH = makeImageName( FOLDER,  "splash_screen", PIC_TYPE1 ),
		POWERUP = makeImageName( FOLDER,  "powerup", PIC_TYPE1 );

	public static String makeImageName( String dir, String name, String type )
	{
		return makeImageName( dir, name, type, true );
	}

	public static String makeImageName( String dir, String name, String type, boolean save )
	{
		String filename = dir + name + type;
		if ( save ) images.add( filename );
		return filename;
	}
	
//	public static final String 
//	FOLDER = "res/",
//	PIC_TYPE1 = ".png",
////	SHIP_YELLOW = makeImageName( "ship_yellow" ),
//	SHIP_YELLOW = FOLDER + "ship_yellow" + PIC_TYPE1,
//	SHIP_GREEN = FOLDER + "ship_green" + PIC_TYPE1,
//	SHIP_BLUE = FOLDER + "ship_blue" + PIC_TYPE1,
//	SHIP_RED = FOLDER + "ship_red" + PIC_TYPE1,
//	VENUS = FOLDER + "venus2" + PIC_TYPE1,
//	MERCURY = FOLDER + "mercury" + PIC_TYPE1,
//	EARTH = FOLDER + "earth_atm" + PIC_TYPE1,
//	MARS = FOLDER + "mars" + PIC_TYPE1,
//	MOON = FOLDER + "moon" + PIC_TYPE1,
//	H_COMET = FOLDER + "haleys_comet" + PIC_TYPE1,
//	PHOBOS = FOLDER + "phobos" + PIC_TYPE1,
//	STONE1 = FOLDER + "asteroid1" + PIC_TYPE1,
//	STONE2 = FOLDER + "asteroid2" + PIC_TYPE1,
//	STNOE3 = FOLDER + "asteroid3" + PIC_TYPE1,
//	SUN = FOLDER + "sun" + PIC_TYPE1,
//	SHOT_01 = FOLDER + "shot_1" + PIC_TYPE1,
//	LASER_BEAM_1 = FOLDER + "laser_1" + PIC_TYPE1,
//	FONT2 = FOLDER + "font" + PIC_TYPE1,
//	FONT1 = FOLDER + "space_font" + PIC_TYPE1,
//	RADAR = FOLDER + "radar" + PIC_TYPE1,
//	BACKGROUND_STARS = FOLDER + "stars_seemless" + PIC_TYPE1,
//	FLAME_PARTICLE = FOLDER + "flame_particle" + PIC_TYPE1,
//	LIGHT_01 = FOLDER + "light01" + PIC_TYPE1,
//	LARGE_EXP_ANIM = FOLDER + "large_exp_*" + PIC_TYPE1,
//	STATION1 = FOLDER + "mir3" + PIC_TYPE1,
//	STATION2 = FOLDER + "newconstantinople" + PIC_TYPE1,
//	SPLASH = FOLDER + "splash_screen" + PIC_TYPE1;

	
		
		
	public static final String
		FOLDER2 = "res/",
		SND_TYPE1 = ".ogg",
		LASER_SOUND = FOLDER2 + "laser" + SND_TYPE1,
		LARGE_EXPLOSION_SOUND = FOLDER2 + "explosion1" + SND_TYPE1,
		SMALL_EXPLOSION_SOUND = FOLDER2 + "hit" + SND_TYPE1;

	
}
