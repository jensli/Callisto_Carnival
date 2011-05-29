package cc.gui.models;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;

import cc.game.GameObject;
import cc.game.behaviors.ControlledBehavior;
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
public class ParticleSystem extends GraphicalModel 
{
	private ControlledBehavior controller;
	
	private float[][] pos;  /** The positions of the particles being rendered */
	private float[][] vel;
	private float[] life;  /** The life left in each particle being rendered */ 
	private float[] size;  /** The current size of each particle */
	private float[] alpha; /** The alpha value of each particle */
	
	/** The index of the next particle to be used */
	private int next;

	private float r = 1.0f, // Color of the particles
		g = 0.6f,
		b = 0.0f;
	
	private Texture texture;
	
	private float initialSize = 10f, /** The initial size of each particle */
		initialLife = 0.15f,
		speed = 800f,
		lagRate = 0.5f,
		sizeDec = 10,
		alphaDec = 10;
	
	private float timeBetweenParticles = 0.002f,
		timeToParticle = 0.0f; 
	
	
	
	/**
	 * Create a new group of particles 
	 * 
	 * @param count The number of particles to be rendered
	 * @param fadeOut The amount of time it takes for particles to fade
	 * @param r The red component of the colour of each particle
	 * @param g The green component of the colour of each particle
	 * @param b The blue component of the colour of each particle
	 */
	public ParticleSystem( GameObject obj, Texture texture, int count)
	{
		super( obj );
		this.texture = texture;
		
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

		glPushAttrib( GL_DEPTH_BUFFER_BIT );

		GL11.glDisable( GL11.GL_DEPTH_TEST );
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
				GL11.glVertex3f( x - partSize, y - partSize, -0.3f );
				
				GL11.glTexCoord2f( 1, 0 );
				GL11.glVertex3f( x + partSize, y - partSize, -0.3f );
				
				GL11.glTexCoord2f( 1, 1 );
				GL11.glVertex3f( x + partSize, y + partSize, -0.3f );
				
				GL11.glTexCoord2f( 0, 1 );
				GL11.glVertex3f( x - partSize, y + partSize, -0.3f );
			}
			
		GL11.glEnd();
		
		glPopAttrib();
		
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
		
		if ( controller.isThrustOn() ) {
			timeToParticle -= dT;
			if ( timeToParticle <= 0.0 ) {
				this.addParticle();
				timeToParticle = timeBetweenParticles * ( Random.getNormalRandom().nextFloat() + 0.5f)*1.5f;
			}
		}
		
	}
	
	/**
	 * Add a new particle to the engine
	 * 
	 * @param x The x coordinate of the particle
	 * @param y The y coordinate of the particle
	 * @param initialSize The initial size of the particle
	 * @param life The time the particle will last for (in milliseconds)
	 */
	public void addParticle()
	{
		
		GameObject obj = super.getObject();
		
		Vec objPos = obj.getPos(),
			objVel = obj.getPhysModel().getVel(),
			objFor = obj.getForward();
		
		vel[next][0] = (float) ( -objFor.x * speed + objVel.x * lagRate );
		vel[next][1] = (float) ( -objFor.y * speed + objVel.y * lagRate );

		pos[next][0] = (float)( objPos.x - objFor.x*obj.getRadius());
		pos[next][1] = (float)( objPos.y - objFor.y*obj.getRadius());
		
		size[next] = initialSize;
		life[next] = initialLife;
		alpha[next] = 1;
		
		next++;
		next %= life.length;
	}


	public void setController( ControlledBehavior controller )
    {
    	this.controller = controller;
    }
	
}
