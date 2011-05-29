package cc.gui.models;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cc.game.GameObject;
import cc.util.Texture;

/**
 * This is a tex squere that changes tex at an interval
 * @author jens
 *
 */
public class Animation extends TextureSquare
{
	private List<Texture> imageList = new ArrayList<Texture>();
	
	public static final int NO_REPEAT = 0, REPEAT = 1, BACK_AND_FORTH = 2;
	
	private int repeatStyle = NO_REPEAT;
	private boolean dirForward = true;  // for BACK_AND_FORTH
	
	private int currentImage = 0;
	private double changeTime,
		timeToChange;
	
	
	public Animation( GameObject object, List<Texture> imageList, double lifeTime )
	{
		super( object,  imageList.get( 0 ));
		
		if ( imageList.size() < 2 ) {
			throw new RuntimeException("Cant have animation with less than 2 images.");
		}
		
		this.imageList = imageList;
		this.changeTime = lifeTime / imageList.size();
		timeToChange = changeTime;
		currentImage = 1;
	}
	
	@Override
    public void update( double dT )
    {
		timeToChange -= dT;

		if ( timeToChange > 0.0 ) {
			return;
		}
		
		timeToChange += changeTime;

		// Bläää, ugly!
		switch ( repeatStyle ) {
			case NO_REPEAT:
				if (currentImage < imageList.size() - 1) {
					currentImage++;
				}
				break;
			case REPEAT:
				if (currentImage >= imageList.size() - 1) {
					currentImage = 0;
				} else {
					currentImage++;
				}
				break;
			case BACK_AND_FORTH:
				if ( dirForward ) {
					if (currentImage >= imageList.size() - 1) {
						currentImage = imageList.size() - 2;
						dirForward = false;
					} else {
						currentImage++;
					}
				} else {
					if (currentImage <= 0) {
						currentImage = 1;
						dirForward = true;
					} else {
						currentImage--;
					}
				}
				break;
		}
		
		super.setTexture( imageList.get( currentImage ) );
    }


	public void addTextures(Collection<Texture> coll )
	{
		imageList.addAll( coll );
	}

	public void setRepeatStyle( int repeatStyle )
    {
    	this.repeatStyle = repeatStyle;
    }
	
	
}
