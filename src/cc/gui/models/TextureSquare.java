package cc.gui.models;

import static org.lwjgl.opengl.GL11.*;
import cc.game.GameObject;
import cc.util.Color;
import cc.util.GraphicsUtil;
import cc.util.Texture;
import cc.util.math.Vec;
import cc.util.resources.TextureLoader;

public class TextureSquare extends GraphicalModel
{
	private Texture texture;
	private float 
		halfWidth, 
		halfHeight;
	
	private double directionOffset = 0.0f;  // Default rotation for the texture
	
	
	private final Color color = Color.getWhite();
	
	public TextureSquare( GameObject obj, Texture texture, float width, float height, Color color )
    {
		super( obj );
	    this.texture = texture;
	    halfWidth = width / 2;
	    halfHeight = height / 2; 
		setColor( color );
    }
	
	public TextureSquare( GameObject obj, Texture texture, float width, float height)
	{
		this( obj, texture, width, height, Color.getWhite() );
	}

	
	public TextureSquare( GameObject obj, Texture texture, double scale )
	{
		this( obj, texture, 
			(float)scale*TextureLoader.get2Fold( texture.getImageWidth() ) , 
			(float)scale*TextureLoader.get2Fold( texture.getImageHeight() ),
			Color.getWhite() );
		
	}

	
	public TextureSquare( GameObject obj, Texture image)
	{
		this( obj, image, 
				TextureLoader.get2Fold( image.getImageWidth() ) , 
				TextureLoader.get2Fold( image.getImageHeight() ),
				Color.getWhite() );
	}
	
	public TextureSquare( GameObject obj, Texture texture, Color color )
	{
		this( obj, texture, 
				TextureLoader.get2Fold( texture.getImageWidth() ) , 
				TextureLoader.get2Fold( texture.getImageHeight() ),
				color );
		
	}
	

	@Override
    public void draw( Vec pos2, Vec forward2 )
    {
		final Vec 
			pos = getObject().getPos(), 
			forward  = getObject().getForward();
		
		texture.bind();
		
		glPushMatrix();
		
		GraphicsUtil.applyTranslation( pos );
		GraphicsUtil.applyRotation( forward.angleNormalized() + directionOffset );
		GraphicsUtil.setColor( color );
		
		glBegin(GL_QUADS);
			glTexCoord2f(0,0);
			glVertex2f(-halfWidth, -halfHeight);
			glTexCoord2f(0,1);
			glVertex2f(-halfWidth, halfHeight);
			glTexCoord2f(1,1);
			glVertex2f( halfWidth, halfHeight);
			glTexCoord2f(1,0);
			glVertex2f( halfWidth, -halfHeight);
		glEnd();

//		// Draw a red squerer around the image
//		glPushAttrib( GL_ENABLE_BIT | GL_CURRENT_BIT );
//
//			glDisable( GL_TEXTURE_2D );
//			glDisable( GL_LIGHTING );
//			
//			glColor3f( 1.0f, 0.0f, 0.0f );
//			
//			glBegin(GL_LINE_LOOP);
//				glVertex2f(-halfWidth, -halfHeight);
//				glVertex2f(-halfWidth, halfHeight);
//				glVertex2f( halfWidth, halfHeight);
//				glVertex2f( halfWidth, -halfHeight);
//			glEnd();
//			
//			glPointSize( 3 );
//			glColor3f( 0, 0, 1.0f );
//			glBegin(GL_POINTS);
//				glVertex2f(0, 0);
//			glEnd();
//		glPopAttrib();

		glPopMatrix();
    }


	public void scale( double factor ) {
		halfWidth *= factor;
		halfHeight *= factor;
	}
	
	public void setDirectionOffset( double directionOffset ) {
		this.directionOffset = directionOffset;
	}
	public void setTexture( Texture texture ) {
		this.texture = texture;
	}
	@Override
    public void setColor( Color color ) {
		this.color.set( color );
	}
//	public void scaleColor( double scale ) {
//		color = color.scaled( scale ); 
//	}

	@Override
    public Color getColor() {
    	return color;
    }
	
}
	