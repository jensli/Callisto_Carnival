package cc.gui.models;


import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;

import cc.game.GameObject;
import cc.game.PhysicalModel;
import cc.util.Texture;
import cc.util.math.InterpolatingDouble;
import cc.util.math.LinearInterpolatingDouble;
import cc.util.math.Vec;

public class Lamps extends GraphicalModel
{
	private List<Lamp> lampList = new ArrayList<Lamp>();
	private Texture texture;
	
	private float halfWidth = 5f, halfHeight = 5f;
	
	public Lamps( GameObject object, Texture texture )
    {
	    super( object );
	    this.texture = texture;
    }
	
	public void addLamp( double alpha , Vec relPos, double speed )
	{
		lampList.add( new Lamp( alpha, relPos, speed ) );
	}
	

	@Override public void update( double dT ) {
		for ( Lamp lamp : lampList ) {
			lamp.update( dT );
		}
    }

	@Override 
	public void draw( Vec pos, Vec forward ) 
	{
		PhysicalModel phy = this.getObject().getPhysModel();
		
		texture.bind();

		GL11.glPushMatrix();
		GL11.glTranslated( phy.getPos().x, phy.getPos().y, 0 );
		GL11.glRotatef( (float) Math.toDegrees( phy.getForward().angleNormalized() ), 0, 0, 1 );
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		
		for ( Lamp lamp : lampList ) {
//			GL11.glColor4f( 1, 0, 0, 1 );
			GL11.glColor4f( 1, 0.3f, 0.3f, lamp.getAlpha() );
			
			GL11.glPushMatrix();
			GL11.glTranslated( lamp.getRelPos().x, lamp.getRelPos().y, 0.0 );
			
			GL11.glBegin( GL11.GL_QUADS );
				GL11.glTexCoord2f(0,0);
				GL11.glVertex3f(-halfWidth, -halfHeight, 0.3f );
				GL11.glTexCoord2f(0,1);
				GL11.glVertex3f(-halfWidth, halfHeight, 0.3f );
				GL11.glTexCoord2f(1,1);
				GL11.glVertex3f( halfWidth, halfHeight, 0.3f );
				GL11.glTexCoord2f(1,0);
				GL11.glVertex3f( halfWidth, -halfHeight, 0.3f );
			GL11.glEnd();
			
			GL11.glPopMatrix();
		}
		
		GL11.glPopMatrix();
		GL11.glColor3f( 1, 1, 1 );
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		
	}
}

//class Lamp
//{
////	private InterpolatingDouble alpha;
//	private Vector2d relPos;
//	private double lowestValue = -1.0;
//	private double alpha;
//
//	public Lamp( double alpha, Vector2d relPos, double speed )
//	{
//		this.alpha = alpha;
//		this.relPos = relPos;
//	}
//	
//	public void update( double dT )
//	{
//		
//		if ( alpha.isAtTarget() ) {
//			alpha.changeValue( 1.0 - lowestValue );
//		}
//	}
//
//	public float getAlpha() {
//    	return alpha.floatValue();
//    }
//	public Vector2d getRelPos() {
//    	return relPos;
//    }
//}

class Lamp
{
	private InterpolatingDouble alpha;
	private Vec relPos;
	private double lowestValue = -1.0;
	
	public Lamp( double alpha, Vec relPos, double speed )
	{
		this.alpha = new LinearInterpolatingDouble( alpha, speed );
		this.alpha.setTargetValue( lowestValue );
		this.relPos = relPos;
	}
	
	public void update( double dT )
	{
		alpha.update( dT );
		if ( alpha.isAtTarget() ) {
			alpha.setValue( 1.0 );
		}
	}
	
	public float getAlpha() {
		return (float) alpha.value();
	}
	public Vec getRelPos() {
		return relPos;
	}
}








