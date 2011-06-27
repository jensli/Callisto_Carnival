package cc.gui.menu_old_system;

import cc.util.BitmapFont;
import cc.util.Color;
import cc.util.resources.ResourceHandler;

public class MenuSettings
{
	public float width = 0.1f,
		rowHeight = 0.02f,
		size = 3.0f;

	public BitmapFont font = ResourceHandler.get().getFont( 0 );

	public Color selectionColor = new Color( 1, 1, 1 ),
		color = new Color( 0.4f, 1, 1 );
}
