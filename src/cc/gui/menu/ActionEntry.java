package cc.gui.menu;

public class ActionEntry extends MenuEntry
{

	public ActionEntry( String text )
    {
	    super( text );
    }

	@Override
    public void select() 
	{
		listener.informChange( null );
    }
	
}
