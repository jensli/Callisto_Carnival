package cc.gui.game_display;

import org.fenggui.Display;

import cc.gui.Graphics;
import cc.util.math.Vec;

public class NoticeArea {

	private Vec pos = new Vec();


	public NoticeArea()
	{
		pos.set( 1, 2 );
	}

	public void draw( Display display )
	{
		Graphics.get().enterOrthoProjection();
		display.display();
		Graphics.get().leaveOrthoProjection();
	}

	public void showMessage( Message m ) {

	}
}
