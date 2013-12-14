
package cc.gui.models;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import cc.game.GameObject;
import cc.game.behaviors.ControlledBehavior;
import cc.util.Texture;
import cc.util.math.Vec;

/**
 * A very simple collection of particles that simply fade out.
 * This isn't really much of an engine (hence the name group) since
 * it doesn't do much for the particle. It does however manage them.
 *
 * @author Kevin Glass
 */
public class ParticleSystemNew extends GraphicalModel
{
	private ControlledBehavior controller;

	private float[][] pos;  /** The positions of the particles being rendered */
	private float[][] vel;
	private float[] life;  /** The life left in each particle being rendered */
	private float[] size;  /** The current size of each particle */
	private float[] alpha; /** The alpha value of each particle */

	private Collection<Particle> particleList = new ArrayList<Particle>();

	/** The index of the next particle to be used */
	private int next;

//	private float r = 1.0f, // Color of the particles
//		g = 0.6f,
//		b = 0.0f;

	private Texture texture;

	private float initialSize = 10f, /** The initial size of each particle */
		initialLife = 0.15f,
		speed = 800f,
		lagRate = 0.5f,
		sizeDec = 10,
		alphaDec = 10;

	private float timeBetweenParticles = 0.002f,
		timeToParticle = 0.0f;


	private Random random = new Random();

	/**
	 * Create a new group of particles
	 *
	 * @param count The number of particles to be rendered
	 * @param fadeOut The amount of time it takes for particles to fade
	 * @param r The red component of the colour of each particle
	 * @param g The green component of the colour of each particle
	 * @param b The blue component of the colour of each particle
	 */
	public ParticleSystemNew( GameObject obj, Texture texture, int count)
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

		for ( Particle particle : particleList ) {
			particle.draw();
		}

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
				this.addParticle( );
				timeToParticle = timeBetweenParticles * (random.nextFloat() + 0.5f)*1.5f;
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

		Vec objPos = obj.getPhysModel().getPos(),
			objVel = obj.getPhysModel().getVel(),
			objFor = obj.getPhysModel().getForward();

		vel[next][0] = (float) ( -objFor.x * speed + objVel.x * lagRate );
		vel[next][1] = (float) ( -objFor.y * speed + objVel.y * lagRate );

		pos[next][0] = (float)( objPos.x - objFor.x*obj.getPhysModel().getRadius());
		pos[next][1] = (float)( objPos.y - objFor.y*obj.getPhysModel().getRadius());

		size[next] = initialSize;
		life[next] = initialLife;
		alpha[next] = 1;

		next++;
		if ( next >= life.length ) {
			next = 0;
		}
	}


	public void setController( ControlledBehavior controller )
    {
    	this.controller = controller;
    }

}

