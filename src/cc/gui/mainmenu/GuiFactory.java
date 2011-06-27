package cc.gui.mainmenu;

import org.fenggui.Button;
import org.fenggui.Label;
import org.fenggui.background.PlainBackground;
import org.fenggui.composites.Window;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.render.Font;
import org.fenggui.util.Color;
import org.fenggui.util.Point;
import org.fenggui.util.Spacing;

public class GuiFactory
{
	private Font
		buttonFont,
		titleFont;

	public GuiFactory( Font titleFont, Font buttonFont ) {
	    this.buttonFont = buttonFont;
	    this.titleFont = titleFont;
    }

	public Button makeButton(  String title, IButtonPressedListener listener )
	{
		Button button = new Button( title );
		button.getAppearance().setFont( getFont1() );
		button.addButtonPressedListener( listener );
		return button;
	}

	public Font getTitleFont() {
		return titleFont;
	}

	public Font getFont1() {
    	return buttonFont;
    }

	public Label makeLabel( String text )
	{
		Label label = new Label( text );
		label.getAppearance().setFont( getFont1() );
		label.getAppearance().setTextColor( Color.WHITE );

		return label;
	}

	public Window makeDialogWindow( String title, Point pos )
	{
		Window window = new Window( false, false, false, false );
		window.getTitleLabel().getAppearance().setFont( getTitleFont() );
		window.getTitleLabel().getAppearance().add( new PlainBackground( new Color(145, 0, 100, 255 ) ) );
		window.setTitle( title );
		window.setPosition( pos );
		window.getContentContainer().getAppearance().setPadding( new Spacing( 15, 15 ) );
		window.getContentContainer().getAppearance().add( new PlainBackground( new Color(145, 0, 100, 128 ) ) );

		// Window has to be in the widget tree for this
//		window.setResizable( false );
//		window.setMovable( false );

		return window;
	}


}
