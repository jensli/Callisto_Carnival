package cc.gui.menu_old_system;

public class MenuTest
{
	public String str = "";
	public boolean b1 = false,
		b2 = true;

	public Menu menu;

	public Menu makeMenu()
	{
		Menu newMenu = new Menu( "The Menu:" );

		BooleanEntry e1 = new BooleanEntry( "Wooooooo:" );
		e1.setValue( true );
		e1.setListener( new MenuListener() {
			public void informChange( Object newValue ) {
				b1 = (Boolean) newValue;
			}
		});
		newMenu.addEntry( e1 );

		BooleanEntry e2 = new BooleanEntry( "Waaaaaaaa:" );
		e2.setValue( true );
		e2.setListener( new MenuListener() {
			public void informChange( Object newValue ) {
				b2 = (Boolean) newValue;
			}
		});
		newMenu.addEntry( e2 );

		return newMenu;
	}


	public void run()
	{
		menu = makeMenu();





	}


	public static void main( String[] args )
	{

	}
}
