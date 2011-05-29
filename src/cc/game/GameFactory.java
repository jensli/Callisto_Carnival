package cc.game;

import static java.lang.Math.PI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cc.game.behaviors.Behavior;
import cc.game.behaviors.GeneratePowerupBehavior;
import cc.game.behaviors.OrbitBehavior;
import cc.game.collision.BurningCollider;
import cc.game.objects.Ship;
import cc.game.objects.Explosion;
import cc.game.objects.LaserShot;
import cc.game.objects.Planet;
import cc.game.objects.PlasmaShot;
import cc.game.objects.Powerup;
import cc.game.objects.SpaceStation;
import cc.gui.models.CometModel;
import cc.gui.models.GraphicalModel;
import cc.gui.models.GraphicalModelGroup;
import cc.gui.models.Lamps;
import cc.gui.models.SunParticleSystem;
import cc.gui.models.TextureSquare;
import cc.util.Random;
import cc.util.Texture;
import cc.util.math.Vec;
import cc.util.math.VecMath;
import cc.util.resources.Name;
import cc.util.resources.ResourceHandler;




/**
 * Sets up the Game's map with all its SpaceObjects.
 * Takes care of the players' respawn and its universe.
 * This class is a soup.
 * 
 */
public class GameFactory
{
	private static GameFactory instance = new GameFactory();
	private ResourceHandler resources;
	
	// All planets get properties relative to those
	private double
		ROTATION_SCALE = 1.0,
		RADIUS_SCALE = 1.0,
		earthRevolvTime = 4 * 365.24,
		earthDistance = 3049.121797,
		earthMass = 300.0;
	
	// To get a texture from a color
	private Map<Player.Color, String> colorImageMap = new TreeMap<Player.Color, String>();
	
	// Start points for respawning players
	private List<Vec> startPosList = new ArrayList<Vec>();
	private int lastPosUsed = -1; // Number of start point to use next
	
	private Map<String, GameObject> namedObjects = new HashMap<String, GameObject>();
	
	private Vec[] startPosArray = new Vec[] { 
			new Vec( -1450.0, 0.0 ),
			new Vec( -1350.0, -500.0 ),
			new Vec( -1550.0, 1400.0 ),
			new Vec( +1450.0, +450.0 ),
			new Vec( +1450.0, -1000.0 ),
			new Vec( +1000.0, -900.0 ),
			new Vec( -850.0, -850.0 ),
			new Vec( +2450.0, -100.0 ) };

	
	/**
	 * Sets the players starting points.
	 *
	 */
	private GameFactory()
	{
		resources = ResourceHandler.get();
		
		colorImageMap.put( Player.Color.BLUE, Name.SHIP_BLUE );
		colorImageMap.put( Player.Color.RED, Name.SHIP_RED );
		colorImageMap.put( Player.Color.GREEN, Name.SHIP_GREEN );
		colorImageMap.put( Player.Color.YELLOW, Name.SHIP_YELLOW );
		
		startPosList = Arrays.asList( startPosArray );
		
		// Create objs and throw away to load textures
		for ( String filename : Name.images ) {
			resources.getTexture( filename );
		}
		
		resources.getTextures( Name.LARGE_EXP_ANIM );
	}
	
	
	public GameObject getObject( String name )
	{
		return namedObjects.get( name );
	}
	
	
	public static GameFactory get() {
		return instance;
	}
	
	
	public static void reset()
	{
		instance = new GameFactory();
	}
	
	
	/**
	 * Returns a planet as a SpaceObject and set the planet's position on the GameMap. The planet's coordinates
	 * are used to put it on a specific spot on the GameMap.
	 * @param mass_ratio The planet's mass ratio compared to the earth's mass.
	 * @param radius The planet's radius.
	 * @param rotation The rotation speed of which it rotate around its own axis.
	 * @param imageFilename The planet's image.
	 * 
	 * @return A planet with these specific parameters.
	 * @see GameObject
	 */
	public GameObject createPlanet( 
			String name,  
			double mass_ratio, 
			double radius, 
			double rotation, 
			String imageFilename )
	{
		Texture t = resources.getTexture( imageFilename );

		GameObject planet = new Planet( name, t  );
		planet.getPhysModel().setMass( mass_ratio * earthMass );
		planet.getPhysModel().setRadius( radius*RADIUS_SCALE );
		planet.getPhysModel().setRotation( rotation );
		
		namedObjects.put( name, planet );
		
		return planet;
	}
	
//	public GameObject createPlanet( 
//			String name,  
//			double mass_ratio, 
//			double radius, 
//			double rotation, 
//			String imageFilename )
//	{
//		Texture t = resources.getTexture( imageFilename );
//		
//		GameObject planet = new GameObject( name );
//		
//		planet.getPhysModel().setMass( mass_ratio * earthMass );
//		planet.getPhysModel().setRadius( radius );
//		planet.getPhysModel().setRotation( rotation );
//		planet.setKillable( false );
//		planet.setCollider( new Collider() );
//		
//		TextureSquare ts = new TextureSquare( planet, t);
//		planet.setGraphicalModel(  ts );
//		
//		return planet;
//	}

	
	public void setPlanetRevole( GameObject planet, GameObject motherObject, 
			double revolveTime, double revolveDistance, double startAngel )
	{
		OrbitBehavior revolve = 
				new OrbitBehavior( 
						motherObject, 
						revolveTime*earthRevolvTime, 
						revolveDistance*earthDistance, 
						startAngel );
	
		planet.setMovementBehavior( revolve );
	}
	
	
	public List<GameObject> createPlanets()
	{
		List<GameObject> list = new ArrayList<GameObject>();
		
		//  The sun
		GameObject sun = createPlanet( "Sun", 
//				/*mass:*/ 5.0, 
				/*mass:*/ 3.0, 
				/*radius:*/ 1000, //183,
				/*rotation*/ 0 , 
				/*image*/ Name.SUN
			);
		sun.setMovementBehavior( Behavior.getEmpty() );
		sun.setCollider( new BurningCollider( 0.3, 183 ) );
		((TextureSquare) sun.getGraphicalModel()).scale( 4.0 );
		GraphicalModelGroup g = new GraphicalModelGroup( sun );
		g.add(
				new SunParticleSystem( sun,
			resources.getTexture( Name.FLAME_PARTICLE ), 
			(BurningCollider) sun.getCollider(), 200 ) 
		);
		g.add( sun.getGraphicalModel() );
		sun.setGraphicalModel( g );
		
		
		
		list.add( sun );
		
		//  Mercury
		GameObject mercury = createPlanet( "Mercury",
				///*mass:*/ 0.0055, 
				/*mass:*/ 0.055, 
				/*radius:*/ 48,
				/*rotation*/ 0.0, 
				/*image*/ Name.MERCURY
			);
		setPlanetRevole( mercury, sun, 2.41, 0.387, 12  );
		mercury.addExtraBehavior( new GeneratePowerupBehavior( 0.3, PowerupType.FUEL ) );
		list.add( mercury );
		
		//  Venus
		GameObject venus = createPlanet( "Venus", 
				/*mass:*/ 0.8149, /*radius:*/ 118.5,
				/*rotation*/ 0.0, 
				/*image*/ Name.VENUS
			);
		setPlanetRevole( venus, sun, 6.15, 0.723, 3 );
		venus.addExtraBehavior( new GeneratePowerupBehavior( 0.3, PowerupType.LIFE ) );
		
		list.add( venus );
		
		//  Earth
		GameObject earth = createPlanet( "Tellus", 
				/*mass:*/ 1, /*radius:*/ 130,
				/*rotation*/ 0.15, 
				/*image*/ Name.EARTH
			);
		setPlanetRevole( earth, sun, 10, 1.0, 5 );
		list.add( earth );
		
				//  Earth's moon Luna
				GameObject moon = createPlanet( "Luna",  
						/*mass:*/ 0.0123, 
						/*radius:*/ 40,
						/*rotation*/ 0.01, 
						/*image*/ Name.MOON
					);
				setPlanetRevole( moon, earth, 0.4, 0.20, 1 );
				list.add( moon );
				
		 //  Mars
		GameObject mars = createPlanet( "Mars",  
				/*mass:*/ 0.1074, 
				/*radius:*/ 66.5,
				/*rotation*/ 0.1, 
				/*image*/ Name.MARS
			);
		setPlanetRevole( mars, sun, 7.88, 1.523, 7 );
		mars.addExtraBehavior( new GeneratePowerupBehavior( 0.3, PowerupType.AMMO ) );

		
		list.add( mars );
				
				//  Mars's moon Phobos 
				GameObject phobos = createPlanet( "Phobos",  
						/*mass:*/ 0.0023, 
						/*radius:*/ 12,
						/*rotation*/ 1, 
						/*image*/ Name.PHOBOS
					);
				setPlanetRevole( phobos, mars, 0.2, 0.065, 1 );
				list.add( phobos );
		
		/** OBS OBS OBS */
				
				
		GameObject comet = createComet( sun );
		list.add( comet );

		list.add( createMir( earth ) );
		list.add( createNewConstantinople( sun ) );

//		Powerup p = createPowerup( venus );
//		list.add( p );
		
		return list;
	}
	
	public GameObject createMir( GameObject mother )
	{
		
		SpaceStation mir = new SpaceStation( 
				"Mir", resources.getTexture( Name.STATION1 ) );

		mir.setMovementBehavior( new OrbitBehavior( mother,  150, 280, 23 ) );
		GraphicalModelGroup mg = new GraphicalModelGroup( mir );
		mg.add( mir.getGraphicalModel() );
		
		Lamps lamps = new Lamps( mir, ResourceHandler.get()
				.getTexture( Name.LIGHT_01  ) );

		double blinkSpeed = 4.0;
		lamps.addLamp( 0.9, new Vec(-11, 49), blinkSpeed );
		
		mg.add( lamps );
		mir.setGraphicalModel( mg );
		
		
		return mir;
	}
	
	public GameObject createNewConstantinople( GameObject mother )
	{
		Texture t = resources.getTexture( Name.STATION2 );
		t.setHeight( 200 );
		t.setWidth( 200 );

		SpaceStation station = new SpaceStation( 
				"New Constantinople", t );
		PhysicalModel phy = station.getPhysModel();
		
		phy.setRadius( 70.0 );
		phy.setRotation( 0.04 );
		phy.setMass( 0 );

		Vec v = new Vec( -1, 0 );
		v.rotateNormalized( Math.PI*1.1 );
		phy.setForward( v );
		
		GraphicalModelGroup mg = new GraphicalModelGroup( station );
		mg.add( station.getGraphicalModel() );

		Lamps lamps = new Lamps( station, ResourceHandler.get()
				.getTexture( Name.LIGHT_01  ) );

		double blinkSpeed = 2.0;
		lamps.addLamp( 0.4, new Vec( 22, 8 ), blinkSpeed );
		lamps.addLamp( 0.6, new Vec( 23, -5 ), blinkSpeed );
		lamps.addLamp( 0.8, new Vec( 24, -18 ), blinkSpeed );
		lamps.addLamp( 1, new Vec( 25, -31 ), blinkSpeed );

		lamps.addLamp( 0.2, new Vec( -17, 31 ), blinkSpeed );
		lamps.addLamp( 0.4, new Vec( -35, 32 ), blinkSpeed );
		lamps.addLamp( 0.6, new Vec( -50, 32 ), blinkSpeed );
		lamps.addLamp( 0.8, new Vec( -70, 35 ), blinkSpeed );
		
		mg.add( lamps );
		
		station.setGraphicalModel( mg );
		
		this.setPlanetRevole( station, mother, 8, 0.5, 3 );
		
		return station;
	}
	

	public GameObject createLaserShot()
	{
		return new LaserShot( "Laser beam", resources.getTexture( Name.LASER_BEAM_1 ), 
				null
				);
	}
	
	
	public GameObject createPlasmaShot()
	{
		return new PlasmaShot( "Plasma ball", resources.getTexture( Name.SHOT_01 ), 
				resources.getSound( Name.SMALL_EXPLOSION_SOUND ) 
				);
	}

//	public GameObject createShip(Color color)
//	{
//		GameObject ship = new GameObject("Small ship");
//		ship.setMass( 3.0 );
//		ship.setRadius( 10.0 );
//		ship.setDamage( 1.0 );
//		
////		ship.addBehavior( Behavior.Type.MOVEMENT, new FreeFloatBehavior() );
//		ship.addBehavior( new FreeFloatBehavior() );
////		ship.addBehavior( Behavior.Type.COLLISION, new ShipCollisionBehavior( 100 ) );
//		ship.addBehavior( new ShipCollisionBehavior( 100 ) );
//		ship.setLife( 100.0 );
//		
//		TextureSquare ts = new TextureSquare( ship, 
//				resources.getTexture( colorImageMap.get( color ) ), 32, 32 );
//		ts.setDirectionOffset( 0.5 * Math.PI );
//		ship.setGraphicalModel( ts );
//		ship.setCollider( new ShipCollider() );
//		
//		return ship;
//	}
	
	public Ship createShip( String name, Player.Color color )
	{
		Texture texture  = resources.getTexture( colorImageMap.get( color ) ),
			flameTexture = resources.getTexture( Name.FLAME_PARTICLE ) ;
		Ship ship = new Ship( name,  texture, flameTexture );
		
		return ship;
	}
	
	public Ship createPlayerShip( String name, Player.Color color )
	{
		Ship ship = createShip( name, color );
		
		ship.setName( name );
		ship.getPhysModel().setPos( nextStartPos() );
//		Vector2d v = earth.getPosition();
//		v.add( new Vector2d(500, 500 ) );
//		ship.setPosition( v );

		ship.getPhysModel().setForward( VecMath.makeUnit( lastPosUsed ) );
		ship.getPhysModel().setRotation( 40.0 );
		
		return ship;
	}
	
	private Vec nextStartPos()
	{
		lastPosUsed++;
		lastPosUsed %= startPosList.size();
		
		return startPosList.get( lastPosUsed );
	}
	
	
	public Explosion createExplosion()
	{
		return new Explosion( "Explosion",
				resources.getTextures( Name.LARGE_EXP_ANIM ),
				resources.getSound( Name.LARGE_EXPLOSION_SOUND ) );
	}
	
	public GameObject createSpaceStation( GameObject mother )
	{
		SpaceStation station = new SpaceStation(
				"Mir", resources.getTexture( Name.STATION1 ) );
		
		station.setMovementBehavior(
				new OrbitBehavior( mother,  150, 280, 23 ) );
		
		return station;
	}
	
	
	public Planet createComet( GameObject mother )
	{
		Planet comet = new Planet( "Haleys comet", resources.getTexture( Name.PHOBOS ) );
		
		comet.getPhysModel().setRadius( 15 );
		GraphicalModel m = comet.getGraphicalModel();
		comet.setGraphicalModel( new CometModel( comet, mother ) );
		comet.addExtraGraphModel( m );

		comet.getPhysModel().setRotation( 10*ROTATION_SCALE );
		
		OrbitBehavior revolve = 
			new OrbitBehavior( 
					mother,
//					99999999999999.0,
					0.2*earthRevolvTime, 
					0.1*earthDistance,  
					1*earthDistance,
					0,
//					Math.random(),
//					Vec.makeRight(),
					VecMath.makeUnit( 0.75*Math.PI ), 
					false); 
		
		comet.setMovementBehavior( revolve );
		
		return comet;
	}
	
	public OrbitBehavior makeRandomOrbit( GameObject mother )
	{
		final double
			r = mother.getRadius(),
			perihelionDist = r*Random.gameDouble( 1.5, 3.0 ),
			aphelionDist = r*Random.gameDouble( 1.5, 3.0 ),
			startingAngle = Random.gameDouble( 0, 2*PI ),
			meanDist = (perihelionDist + aphelionDist) / 2,
			revolvTime = meanDist*meanDist*0.3 / mother.getPhysModel().getMass();
		
		final Vec dir = VecMath.makeUnit( Random.gameDouble( 0, 2*PI ) ); 
		
		return new OrbitBehavior( mother, revolvTime, perihelionDist, aphelionDist, startingAngle, dir, false );
	}
	
	public Powerup createPowerup( GameObject mother, PowerupType type )
	{
		OrbitBehavior orbit = makeRandomOrbit( mother );
		
		Powerup p = new Powerup( "Powerup",
				orbit,
				type,
				resources.getTexture( Name.POWERUP ),
				resources.getSound( Name.SMALL_EXPLOSION_SOUND ) );
		
		return p;
	}
	

}
