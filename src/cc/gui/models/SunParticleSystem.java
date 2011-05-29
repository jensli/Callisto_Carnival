package cc.gui.models;


import org.lwjgl.opengl.GL11;

import cc.game.GameObject;
import cc.game.collision.BurningCollider;
import cc.util.Random;
import cc.util.Texture;
import cc.util.math.Vec;

/**
 * A very simple collection of particles that simply fade out. 
 * This isn't really much of an engine (hence the name group) since
 * it doesn't do much for the particle. It does however manage them.
 * 
 * @author Kevin Glass
 */
public class SunParticleSystem extends GraphicalModel 
{
	private float deapthOffset = -0.3f;

	private BurningCollider controller;
	
	private float[][] pos;  /** The positions of the particles being rendered */
	private float[][] vel;
	private float[] life;  /** The life left in each particle being rendered */ 
	private float[] size;  /** The current size of each particle */
	private float[] alpha; /** The alpha value of each particle */
	
	/** The index of the next particle to be used */
	private int next;

	private float r = 1.0f, // Color of the particles
		g = 0.8f,
		b = 0.0f;
	
	private Texture texture;
	
	private float initialSize = 30f, /** The initial size of each particle */
		initialLife = 0.10f,
		speed = 800f,
		sizeDec = 10,
		alphaDec = 10;
	
	private float timeBetweenParticles = 0.005f,
		timeToParticle = 0.0f; 
	
	
//	private Random random = new Random();
	
	/**
	 * Create a new group of particles 
	 * 
	 * @param count The number of particles to be rendered
	 * @param fadeOut The amount of time it takes for particles to fade
	 * @param r The red component of the colour of each particle
	 * @param g The green component of the colour of each particle
	 * @param b The blue component of the colour of each particle
	 */
	public SunParticleSystem( GameObject obj, Texture texture, 
			BurningCollider controller, int count)
	{
		super( obj );
		this.texture = texture;
		this.controller = controller;
		
		pos = new float[count][2];
		life = new float[count];
		size = new float[count];
		alpha = new float[count];
		vel = new float[count][2];
	}
	
	
	/**
	 * Render the particles in the group
	 */
	@Override
    public void draw( Vec pos2, Vec forward2 )
	{
		texture.bind();

//		GL11.glDisable( GL11.GL_DEPTH_TEST );
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

		GL11.glBegin( GL11.GL_QUADS );

			for ( int i = 0; i < life.length; i++ ) {
				
				if ( life[i] <= 0 ) {
					continue;
				}
				
				float scalar = alpha[i] / 3;
				GL11.glColor4f( ( r * ( 1 - scalar ) ) + scalar, 
						( g * ( 1 - scalar ) ) + scalar, 
						( b * ( 1 - scalar ) ) + scalar, 
						alpha[i] );
				
				float x = pos[i][0],
					y = pos[i][1],
					partSize = size[i];
				
				GL11.glTexCoord2f( 0, 0 );

				GL11.glVertex3f( x - partSize, y - partSize, deapthOffset );
				
				GL11.glTexCoord2f( 1, 0 );
				GL11.glVertex3f( x + partSize, y - partSize, deapthOffset );
				
				GL11.glTexCoord2f( 1, 1 );
				GL11.glVertex3f( x + partSize, y + partSize, deapthOffset );
				
				GL11.glTexCoord2f( 0, 1 );
				GL11.glVertex3f( x - partSize, y + partSize, deapthOffset );
			}
			
		GL11.glEnd();
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f( 1, 1, 1, 1 );
	}
	
	/**
	 * Update the particles state, i.e. fade them out over time
	 * 
	 * @param delta The amount of time thats passed since last 
	 * update
	 */
	@Override
	public void update( double dT ) 
	{	
		
		// cycle through every particle, aging it. It the particle
		// is still alive this frame then fade it and shrink it
		for ( int i = 0; i < life.length; i++ ) {
			if (life[i] >= 0) {
				life[i] -= dT;
				
				pos[i][0] += vel[i][0] * dT;
				pos[i][1] += vel[i][1] * dT;
				
				size[i] *= (1 - dT*sizeDec);
				alpha[i] *= (1 - dT*alphaDec);
				
			}
		}
		
//		if ( controller.isThrustOn() ) {
//			timeToParticle -= dT;
//			if ( timeToParticle <= 0.0 ) {
//				this.addParticle();
//				timeToParticle = timeBetweenParticles * (random.nextFloat() + 0.5f)*1.5f;
//			}
//		}
		
		for ( GameObject obj : controller.getBurningList() ) {
			timeToParticle -= dT;
			if ( timeToParticle <= 0.0 ) {
				this.addParticle( obj );
				timeToParticle = timeBetweenParticles * ( Random.getNormalRandom().nextFloat() + 0.2f)*2f;
			}
			
		}
		controller.getBurningList().clear();
	}
	
	/**
	 * Add a new particle to the engine
	 * 
	 * @param x The x coordinate of the particle
	 * @param y The y coordinate of the particle
	 * @param initialSize The initial size of the particle
	 * @param life The time the particle will last for (in milliseconds)
	 */
	public void addParticle( GameObject obj)
	{
		
//		GameObject obj = super.getObject();
		
		Vec objPos = obj.getPhysModel().getPos(),
			objVel = obj.getPhysModel().getVel(),
//			objFor = obj.getPhysicalModel().getForward(),
			sunPos = this.getObject().getPhysModel().getPos(),
			dir = new Vec();
		
		double distance = objPos.distance( sunPos );
			
		dir.setSub( objPos, sunPos );
		dir.normalize();
		
		vel[next][0] = (float) (dir.x *speed  + objVel.x );
		vel[next][1] = (float) (dir.y *speed + objVel.y );

		pos[next][0] = (float)objPos.x;
		pos[next][1] = (float)objPos.y;
		
//		size[next] = initialSize ;
//		life[next] = initialLife;
		
		size[next] = (float)(initialSize / distance * 200);
		life[next] = (float)(initialLife / distance * 300);

		alpha[next] = (float)(1 / distance * 700 );
		
		next++;
		if ( next >= life.length ) {
			next = 0;
		}
	}


	public void setController( BurningCollider controller )
    {
    	this.controller = controller;
    }
	
}
