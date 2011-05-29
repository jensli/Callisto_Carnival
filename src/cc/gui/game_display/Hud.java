package cc.gui.game_display;


import org.lwjgl.opengl.GL11;

import cc.game.Player;
import cc.game.behaviors.ControlledBehavior;
import cc.game.objects.Ship;
import cc.gui.Graphics;
import cc.util.BitmapFont;
import cc.util.math.Vec;
import cc.util.resources.ResourceHandler;


public class Hud
{
	BitmapFont font;
	
	public Hud()
	{
		font = ResourceHandler.get().getFont( 0 );
//		font = new BitmapFont( BitmapFont.FONT1, ResourceHandler.getInstance().getTexture( ResourceHandler.FONT1 ), 32, 32 );
//		font.setFontSize( 0.0005f );
	}
	
	public void draw( Player focusPlayer )
	{
		if ( focusPlayer == null || focusPlayer.getControlledObject() == null ) {
			return;
		}
		
		Graphics.get().enterOrthoProjection();
		
		final Ship controlledObject = focusPlayer.getControlledObject();
		
		Vec focusPoint = controlledObject.getPos();
		double velocity = controlledObject.getPhysModel().getVel().length();
		
		GL11.glColor3f( 0.4f, 1, 1 );
		
		String stringToDraw = "Galactic Coordinates: " + (int) focusPoint.x + "," + (int) focusPoint.y;

		font.drawString( stringToDraw, 0.28f, 0.95f );
		font.drawString( "Velocity: " + (int)velocity + " parsec/eon" , 0.3f, 0.92f );
		
		final ControlledBehavior behavior = controlledObject.getControlledBehavior();
		
		int fuel = (int) ( behavior.getFuelLevel() / behavior.getMaxFuel() * 100 );
		font.drawString( "Fuel: " + fuel + "%" , 0.32f, 0.89f );
		
		int ammo = (int) ( behavior.getWeapon().getAmmo() );
		font.drawString( "Ammo: " + ammo + "%" , 0.33f, 0.86f );

		int armor = (int) (controlledObject.getLife() / controlledObject.getMaxLife() * 100);
		font.drawString( "Armor: " + armor + "%" , 0.34f, 0.83f );
		
		Graphics.get().leaveOrthoProjection();
	}

}


