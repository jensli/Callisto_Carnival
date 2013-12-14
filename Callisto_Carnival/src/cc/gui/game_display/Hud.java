package cc.gui.game_display;


import j.util.util.Util;

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

//	private String x;
//	private String y;
//	private String vel;
//	private int fuel;
//	private int ammo;
//	private int armor;

	private StringBuilder x = new StringBuilder();
	private StringBuilder y = new StringBuilder();
	private StringBuilder vel = new StringBuilder();
	private StringBuilder fuel = new StringBuilder();
	private StringBuilder ammo = new StringBuilder();
	private StringBuilder armor = new StringBuilder();

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



		GL11.glColor3f( 0.4f, 1, 1 );

		updateData( focusPlayer );

		font.drawString( "Galactic Coordinates: ",
				x, ",",
				y,
				0.28f, 0.95f );
//		String stringToDraw = "Galactic Coordinates: " + (int) focusPoint.x + "," + (int) focusPoint.y;
//		font.drawString( stringToDraw, 0.28f, 0.95f );

//		coordsString.setSeq( 1, Integer.toString( (int) focusPoint.x ),
//				3, Integer.toString( (int) focusPoint.y ) );
//
//		font.drawString( coordsString, 0.28f, 0.95f );

		font.drawString( "Velocity: ", vel, " parsec/eon" , 0.3f, 0.92f );
		font.drawString( "Fuel: ", fuel, "%" , 0.32f, 0.89f );
		font.drawString( "Ammo: ",  ammo, "%" , 0.33f, 0.86f );
		font.drawString( "Armor: ", armor, "%" , 0.34f, 0.83f );

		Graphics.get().leaveOrthoProjection();
	}

//	private void updateData( Player focusPlayer )
//	{
//		final Ship controlledObject = focusPlayer.getControlledObject();
//		Vec focusPoint = controlledObject.getPos();
//		double velocity = controlledObject.getPhysModel().getVel().length();
//		final ControlledBehavior behavior = controlledObject.getControlledBehavior();
//		x = Integer.toString( (int) focusPoint.x );
//		y = Integer.toString( (int) focusPoint.y );
//		vel = Integer.toString( (int)velocity );
//		fuel = (int) ( behavior.getFuelLevel() / behavior.getMaxFuel() * 100 );
//		ammo = (int) ( behavior.getWeapon().getAmmo() );
//		armor = (int) (controlledObject.getLife() / controlledObject.getMaxLife() * 100);
//	}


	private void updateData( Player focusPlayer )
	{
		final Ship controlledObject = focusPlayer.getControlledObject();
		Vec focusPoint = controlledObject.getPos();
		double velocity = controlledObject.getPhysModel().getVel().length();
		final ControlledBehavior behavior = controlledObject.getControlledBehavior();

		Util.replaceStringBuilder( x, (int) focusPoint.x );
		Util.replaceStringBuilder( y, (int) focusPoint.y );
		Util.replaceStringBuilder( vel, (int)velocity );
		Util.replaceStringBuilder( fuel, (int) ( behavior.getFuelLevel() / behavior.getMaxFuel() * 100 ) );
		Util.replaceStringBuilder( ammo, (int) ( behavior.getWeapon().getAmmo() ) );
		Util.replaceStringBuilder( armor, (int) (controlledObject.getLife() / controlledObject.getMaxLife() * 100) );
	}

}


