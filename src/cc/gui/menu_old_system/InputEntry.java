package cc.gui.menu_old_system;

import java.util.regex.Pattern;

public class InputEntry extends MenuEntry
{
	private Pattern validPattern = null;
	private String value = "";
	private boolean isEditing = false;
	private MenuListener listener2;


	public InputEntry( String text )
    {
	    super( text );
	    this.setValidRegex( "*" );
    }


	@Override
    public void select()
    {
		if ( isValidInput() ) {
			if ( isEditing ) {
				listener2.informChange( value );
			}
			isEditing = !isEditing;
		}
    }

	@Override
    public void spacePress()
	{
		// TODO
    }

	private boolean isValidInput()
	{
		return true;
	}

	public void setValidRegex( String validRegex ) {
		validPattern = Pattern.compile( validRegex );
	}
	public void setValidPattern( Pattern validPattern ) {
		this.validPattern = validPattern;
	}
	public void setValue( String value ) {
		this.value = value;
	}


	protected Pattern getValidPattern() {
    	return validPattern;
    }



}
