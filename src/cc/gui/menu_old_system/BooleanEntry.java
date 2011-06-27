package cc.gui.menu_old_system;

public class BooleanEntry extends MenuEntry
{
	private boolean value = false;

	public BooleanEntry( String text )
    {
	    super( text );
    }

	@Override
    public void draw()
    {
		super.draw();
		settings.font.drawString( value ? "on" : "off", settings.width, 0 );
    }

	@Override
    public void select()
    {
		value = !value;
		listener.informChange( value );
    }

	protected void setValue( boolean value ) {
    	this.value = value;
    }



}
