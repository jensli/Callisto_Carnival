package cc.game;

import cc.util.math.Vec;


public class PhysicalModel
{
	private final Vec 
		position = new Vec(), 
		forward = new Vec(0.0, 1.0),
		velocity = new Vec(),
		acceleration = new Vec();

	private double 
		rotation = 0.0,
		radius = 0.0,
		mass = 0.0;
	
	// Vector used as temp variable to reduce garbage
	// 
//	private static Vec tempVector = new Vec();
	
	public final void update( GameObject obj, double dT )
	{
		this.accelerate( dT );
		this.move( dT );
		this.rotate( dT );
		
		acceleration.set( 0, 0 );
	}
	
	protected void accelerate( double dT )
	{
//		tempVector.scale( dT, acceleration );
//		velocity.add( tempVector );
		velocity.addScaled( acceleration, dT );
	}

	protected void move( double dT )
	{
//		tempVector.scale( dT, velocity );
//		position.add( tempVector );
		position.addScaled( velocity, dT );
	}
	
	protected void rotate( double dT )
	{
		forward.rotate( rotation * dT );
	}
	
	public final void dampenRotation( double value, double dT )
	{
		rotation /= ( 1 + Math.abs( value ) * dT );
	}
	
	
	//	
	////	
	//////	
	////////	
	//////////	SETTERS AND GETTERS
	////////	
	//////	
	////	
	//	
	
	public final void accelerate( Vec v ) {
		acceleration.add( v );
	}
	public final void accelerateForward( double value ) {
		acceleration.addScaled( forward, value );
	}
	public final void addRotatio( double rotation ) {
		this.rotation += rotation;
	}
	public final void addVelocity( Vec velocity ) {
		this.velocity.add( velocity );
	}
	public final Vec getForward() {
		return forward;
//		return new Vec( forward );
	}
	public final void setForward( Vec forward ) {
		this.forward.set( forward );
	}
	public final double getMass() {
		return mass;
	}
	public final void setMass( double mass ) {
		this.mass = mass;
	}
	public final Vec getPos() {
		return position;
//		return new Vec( position );
	}
	public final void setPos( Vec position ) {
		this.position.set( position );
	}
	public final double getRadius() {
		return radius;
	}
	public final void setRadius( double radius ) {
		this.radius = radius;
	}
	public final double getRotation() {
		return rotation;
	}
	public final void setRotation( double rotation ) {
		this.rotation = rotation;
	}
	public final Vec getVel() {
		return velocity;
//		return new Vector2d( velocity );
	}
	public final void setVelocity( Vec velocity ) {
		this.velocity.set( velocity );
	}

}
