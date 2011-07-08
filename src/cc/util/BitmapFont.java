package cc.util;

import org.lwjgl.opengl.GL11;

/**
 * A simple implementation of fonts in OpenGL. All this actually
 * does it draw a bunch of quad's when the characters for the string
 * should be. It then textures those quads with a specially created
 * texture containing all the characters from a font. The texture
 * coordinates are chosen so that the appropriate bit of the texture
 * is shown on each quad for the characters.
 *
 * Theres a really good tool for building textures containing fonts
 * called Bitmap Font Builder (http://www.lmnopc.com/bitmapfontbuilder/)
 *
 * The tool actually produces textures containing two textures, one at the
 * bottom and one at the top, which is what this class is based on
 *
 * @author Kevin Glass
 */
public class BitmapFont {

	public static final int FONT1 = 0, FONT2 = 1;
	/** The number of characters across the texture */
	private int charactersAcross;

	/** The width of each character in pixels */
	private int characterWidth;
	/** The height of each character in pixels */
	private int characterHeight;

	/** The width of each character in terms of texture coordinates */
	private float characterWidthInTexture;
	/** The height of each character in terms of the texture coordinates */
	private float characterHeightInTexture;

	/** The texture containing the font characters */
	private Texture texture;
	/** The number of pixels we're going to move across for each characeter */
	private int spacing;

	private float fontSize = 0.001f;
	private int font = FONT1;

	/**
	 * Create a new font based on specific texture cut up into a specific
	 * collection of characters
	 *
	 * @param texture The texture containing the characters
	 * @param characterWidth The width of the characters on the sheet (in pixels)
	 * @param characterHeight The height of the characters on the sheet (in pixels)
	 */
	public BitmapFont(int font, Texture texture, int characterWidth, int characterHeight) {
		this.texture = texture;
		this.characterWidth = characterWidth;
		this.characterHeight = characterHeight;
		this.font = font;

		// calculate how much of the texture is taken up with each character
		// by working out the proportion of the texture size that the character
		// size in pixels takes up
		characterWidthInTexture = texture.getWidth() / (texture.getImageWidth() / characterWidth);
		characterHeightInTexture = texture.getHeight() / (texture.getImageHeight() / characterHeight);

		// work out the number of characters that fit across the sheet
		charactersAcross = texture.getImageWidth() / characterWidth;

		// chosen an arbitary value here to move the letters a bit
		// closer together when rendering them
		spacing = characterWidth - 5;

	}

	/**
	 * Draw a string to the screen as a set of quads textured in the
	 * appropriate way to show the string.
	 *
	 * @param font The index of the font to draw. 0 means the font
	 * at the top, 1 the font at the bottom.
	 * @param text The text to be draw to the screen.
	 * @param x The x coordinate to draw the text at (int pixels)
	 * @param y The y coordinate to draw the text at (in pixels)
	 */
	public float drawString(CharSequence t1, CharSequence t2, float x, float y) {
		float dx = drawString( t1, x, y );
		return drawString( t2, x, y, dx );
	}

	public float drawString(CharSequence t1, CharSequence t2, CharSequence t3, float x, float y) {
		float dx = drawString( t1, t2, x, y );
		return drawString( t3, x, y, dx );
	}

	public float drawString(CharSequence t1, CharSequence t2, CharSequence t3, CharSequence t4, float x, float y) {
		float dx = drawString( t1, t2, t3, x, y );
		return drawString( t4, x, y, dx );
	}

	public float drawString(CharSequence text, float x, float y ) {
		return drawString( text, x, y, 0 );
	}


	public float drawString(CharSequence text, float x, float y, float dx) {
		// bind the font text so we can render quads with the characters
		// on
		texture.bind();

		// turn blending on so characters are displayed above the
		// scene
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		// cycle through each character drawing a quad to the screen
		// mapped to the right part of the texture


		GL11.glPushMatrix();

			GL11.glTranslatef(x, y, 0);
			GL11.glScalef( fontSize, fontSize, fontSize );

			GL11.glBegin(GL11.GL_QUADS);

				for ( int i = 0; i < text.length(); i++ ) {
					// get the index of the character baesd on the font starting
					// with the space character
					int c = text.charAt(i);
//					int c = text.charAt(i) - ' ';

					// work out the u,v texture mapping coordinates based on the
					// index of the character and the amount of texture per
					// character
					float u = ((c % charactersAcross) * characterWidthInTexture);
					float v = 1 - ((c / charactersAcross) * characterHeightInTexture);
					v -= font * 0.5f;

					// setup the quad
					GL11.glTexCoord2f( u, v );
					GL11.glVertex2f( dx, characterHeight );

					GL11.glTexCoord2f( u, v - characterHeightInTexture );
					GL11.glVertex2f( dx, 0 );

					GL11.glTexCoord2f( u + characterWidthInTexture, v - characterHeightInTexture );
					GL11.glVertex2f( dx + characterWidth, 0 );

					GL11.glTexCoord2f( u + characterWidthInTexture, v );
					GL11.glVertex2f( dx + characterWidth, characterHeight );

//					GL11.glTexCoord2f( u + characterWidthInTexture, v );
//					GL11.glVertex2f( x + dx + characterWidth, y + characterHeight );
//
//					GL11.glTexCoord2f( u, v );
//					GL11.glVertex2f( x + dx, y + characterHeight );
//
//					GL11.glTexCoord2f( u, v - characterHeightInTexture );
//					GL11.glVertex2f( x + dx, y );
//
//					GL11.glTexCoord2f( u + characterWidthInTexture, v - characterHeightInTexture );
//					GL11.glVertex2f( x + dx + characterWidth, y );
//
//					GL11.glTexCoord2f( u + characterWidthInTexture, v );
//					GL11.glVertex2f( x + dx + characterWidth, y + characterHeight );

					dx += spacing;
				}

			GL11.glEnd();

		GL11.glPopMatrix();

		// reset the blending
		GL11.glDisable(GL11.GL_BLEND);

		return dx;
	}

	public float getFontSize()
    {
    	return fontSize;
    }

	public void setFontSize( float fontSize )
    {
    	this.fontSize = fontSize;
    }



}
