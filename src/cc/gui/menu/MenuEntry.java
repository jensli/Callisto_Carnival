package cc.gui.menu;


public abstract class MenuEntry
{
	private String titel;
	protected MenuListener listener;
	protected MenuSettings settings;
	
	MenuEntry( String text ) {
		this.titel = text;
	}
	
	public void draw() 
	{
		settings.font.drawString( titel, 0, 0 );
	}
	
	public void update( double dT ) {}
	public void charInput( char ch ) {}
	public void backspacePress() {}
	public void select() {}

	public void setTitel( String text ) {
    	this.titel = text;
    }
	public void setListener( MenuListener listener ) {
		this.listener = listener;
	}
	protected void setSettings( MenuSettings settings ) {
		this.settings = settings;
	}
	public void enterPress() {
		this.select();
	}
	public void spacePress() {
		this.select();
	}
	
}
