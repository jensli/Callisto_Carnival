package cc.util;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.fenggui.render.ITexture;
import org.fenggui.theme.xml.IXMLStreamableException;
import org.fenggui.theme.xml.InputOutputStream;
import org.lwjgl.opengl.GL11;

/**
 * A texture to be bound within LWJGL. This object is responsible for 
 * keeping track of a given OpenGL texture and for calculating the
 * texturing mapping coordinates of the full image.
 * 
 * Since textures need to be powers of 2 the actual texture may be
 * considerably bigged that the source image and hence the texture
 * mapping coordinates need to be adjusted to matchup drawing the
 * sprite against the texture.
 *
 * @author Kevin Glass
 * @author Brian Matzon
 */
public class Texture implements ITexture 
{
    /** The GL target type */
    private int target; 
    /** The GL texture ID */
    private int textureID;
    /** The height of the image */
    private int imageHeight;
    /** The width of the image */
    private int imageWidth;
    /** The width of the texture */
    private int textureWidth;
    /** The height of the texture */
    private int textureHeight;
    /** The ratio of the width of the image to the texture */
    private float widthRatio;
    /** The ratio of the height of the image to the texture */
    private float heightRatio;
    
    
    /**
     * Create a new texture
     *
     * @param target The GL target 
     * @param textureID The GL texture ID
     */
    public Texture(int target,int textureID) {
        this.target = target;
        this.textureID = textureID;
    }
    
    /**
     * Bind the specified GL context to a texture
     *
     * @param gl The GL context to bind to
     */
    public void bind() {
      GL11.glBindTexture(target, textureID); 
    }
    
    /**
     * Set the height of the image
     *
     * @param height The height of the image
     */
    public void setHeight(int height) {
        this.imageHeight = height;
        setHeight();
    }
    
    /**
     * Set the width of the image
     *
     * @param width The width of the image
     */
    public void setWidth(int width) {
        this.imageWidth = width;
        setWidth();
    }
    
    /**
     * Get the height of the original image
     *
     * @return The height of the original image
     */
    public int getImageHeight() {
        return imageHeight;
    }
    
    /** 
     * Get the width of the original image
     *
     * @return The width of the original image
     */
    public int getImageWidth() {
        return imageWidth;
    }
    
    /**
     * Get the height of the physical texture
     *
     * @return The height of physical texture
     */
    public float getHeight() {
        return heightRatio;
    }
    
    /**
     * Get the width of the physical texture
     *
     * @return The width of physical texture
     */
    public float getWidth() {
        return widthRatio;
    }
    
    /**
     * Set the height of this texture 
     *
     * @param texHeight The height of the texture
     */
    public void setTextureHeight(int texHeight) {
        this.textureHeight = texHeight;
        setHeight();
    }
    
    /**
     * Set the width of this texture 
     *
     * @param texWidth The width of the texture
     */
    public void setTextureWidth(int texWidth) {
        this.textureWidth = texWidth;
        setWidth();
    }
    
    /**
     * Set the height of the texture. This will update the
     * ratio also.
     */
    private void setHeight() {
        if (textureHeight != 0) {
            heightRatio = ((float) imageHeight)/textureHeight;
        }
    }
    
    /**
     * Set the width of the texture. This will update the
     * ratio also.
     */
    private void setWidth() {
        if ( textureWidth != 0 ) {
			widthRatio = ( (float) imageWidth ) / textureWidth;
		}
    }

	public int getTextureWidth() {
    	return textureWidth;
    }

	public int getTextureHeight() {
    	return textureHeight;
    }
	
	/**
	 * Has the texture an apha channel? Every texture has right now.
	 */
	public boolean hasAlpha() {
		return true;
	}
//	public void setAlpha() {
//		;
//	}

    public int getID() {
	    // TODO Auto-generated method stub
	    return textureID;
    }
    public void texSubImage2D( int arg0, int arg1, int arg2, int arg3, ByteBuffer arg4 ) {
    	// TODO Auto-generated method stub
    }
    public void dispose() {
	    // TODO Auto-generated method stub
    }
    public String getUniqueName() {
	    return GENERATE_NAME;
    }
    public void process( InputOutputStream arg0 ) throws IOException, IXMLStreamableException {
	    // TODO Auto-generated method stub
	    
    }
	
}
