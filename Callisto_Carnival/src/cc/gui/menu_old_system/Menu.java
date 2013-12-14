package cc.gui.menu_old_system;


import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.lwjgl.opengl.GL11;

import cc.gui.Graphics;
import cc.util.math.Vec;


// None of this is acctually used, I think.

public class Menu
{
	private List<MenuEntry> entryList = new ArrayList<MenuEntry>();

	private Vec pos;
	private String title;
	private MenuSettings settings = new MenuSettings();

	private int activeIndex = 0;

	enum Key {
		ENTER, BACKSPACE, ESC, SPACE
	}
//	public double rowHeight = 0.02,
//		width = 0.3;

	public Menu( String title )
    {
	    super();
	    this.title = title;
    }

	public void update( double dT )
	{
		for ( MenuEntry entry : entryList ) {
			entry.update( dT );
		}
	}

	public void draw()
	{
		GL11.glPushMatrix();
		Graphics.get().enterOrthoProjection();

		GL11.glTranslated( pos.x, pos.y, 0 );

		GL11.glScalef( settings.size, settings.size, settings.size );

		GL11.glColor3d( 0.4, 1, 1 );
		settings.font.drawString( title, 0, 0 );

		GL11.glTranslated( 0.01, -settings.rowHeight*1.2, 0 );

		ListIterator<MenuEntry> itr = entryList.listIterator();
		while ( itr.hasNext() ) {
			MenuEntry entry = itr.next();

			if ( itr.previousIndex() == activeIndex ) {
				GL11.glColor3d( 1, 1, 1 );
			} else {
				GL11.glColor3d( settings.color.r, settings.color.g, settings.color.r );
			}
			// Draw passive
			entry.draw();

			GL11.glTranslated( 0, -settings.rowHeight, 0 );
		}

		Graphics.get().leaveOrthoProjection();
		GL11.glPopMatrix();
	}

	public List<MenuEntry> getEntryList()
    {
    	return entryList;
    }

	public void setEntryList( ArrayList<MenuEntry> entryList )
    {
    	this.entryList = entryList;
    }

	public int getActiveIndex()
    {
    	return activeIndex;
    }

	public void setActiveIndex( int activeIndex )
    {
    	this.activeIndex = activeIndex;
    }

	public void selectUp() {
		activeIndex = ( activeIndex + entryList.size() - 1 ) % entryList.size();
	}
	public void selectDown() {
		activeIndex = ( activeIndex + 1 ) % entryList.size();
	}


//		activeIndex++;
//		if ( activeIndex >= entryList.size() ) {
//			activeIndex = 0;
//		}

//		activeIndex--;
//		if ( activeIndex < 0 ) {
//			activeIndex = entryList.size() - 1;
//		}
	public MenuEntry getActive() {
		return entryList.get( activeIndex );
	}

	public void goBack()
	{}
	public void select() {
		entryList.get( activeIndex ).select();
	}
	public void charInput( char ch ) {
		getActive().charInput( ch );
	}
	public void backspacePress() {
		getActive().backspacePress();
	}
	public void enterPress() {
		getActive().enterPress();
	}
	public void spacePress() {
		getActive().spacePress();
	}


	public void addEntry( MenuEntry entry )
	{
		entry.setSettings( settings );
		entryList.add( entry );
	}

	protected void setPos( Vec pos ) {
    	this.pos = pos;
    }

}


