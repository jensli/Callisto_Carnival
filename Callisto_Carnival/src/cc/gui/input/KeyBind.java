package cc.gui.input;


public abstract class KeyBind<T>
{
	public static final int
		NO_MOD = 0,
		CTRL = 1,
		SHIFT = 2,
		ALT = 4;

	private int key;
	private boolean pressed = false;
	private int modifyers = 0;


	public KeyBind( int key ) {
		this( key, 0 );
	}


	public KeyBind( int key, int modifyers ) {
		this.key = key;
		this.modifyers = modifyers;
	}


	public T doKeyPress() {
		return null;
	}
	public T doKeyRelease() {
		return null;
	}
	public T doKeyHoldDown() {
		return null;
	}

	public int getModifyers() {
		return modifyers;
	}
	public void setModifyers( int modifyers ) {
    	this.modifyers = modifyers;
    }
	public boolean isPressed() {
    	return pressed;
    }
	public void setPressed( boolean pressed ) {
    	this.pressed = pressed;
    }

	public int getKey() {
		return key;
	}
	public boolean validMods( int mods )
	{
//		return mods == getModifyers();
		return ( mods & getModifyers() ) == getModifyers();
	}
}

