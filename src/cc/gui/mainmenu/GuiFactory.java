package cc.gui.mainmenu;

import org.fenggui.Button;
import org.fenggui.Label;
import org.fenggui.appearance.TextAppearance;
import org.fenggui.binding.render.Font;
import org.fenggui.binding.render.text.ITextRenderer;
import org.fenggui.composite.Window;
import org.fenggui.decorator.background.PlainBackground;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.text.content.factory.simple.TextStyle;
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
		setFont( button.getAppearance(), getFont1() );
//		button.getAppearance().setFont( getFont1() );
		button.addButtonPressedListener( listener );
		return button;
	}


	public static void setFont( TextAppearance appearance, Font font ) {
	    TextStyle def = new TextStyle();
//	    def.getTextStyleEntry(TextStyle.DEFAULTSTYLEKEY).setColor(color);
	    appearance.addStyle(TextStyle.DEFAULTSTYLEKEY, def);
	    ITextRenderer renderer = appearance.getRenderer(ITextRenderer.DEFAULTTEXTRENDERERKEY).copy();
	    renderer.setFont(font);
	    appearance.addRenderer(ITextRenderer.DEFAULTTEXTRENDERERKEY, renderer);

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
		setFont( label.getAppearance(), getFont1() );
//		label.getAppearance().setFont( getFont1() );
//		label.getAppearance().setTextColor( Color.WHITE );

		return label;
	}

	public Window makeDialogWindow( String title, Point pos )
	{
		Window window = new Window( false, false, false, false );
//		window.getTitleLabel().getAppearance().setFont( getTitleFont() );
		setFont( window.getTitleLabel().getAppearance(), getTitleFont() );

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
