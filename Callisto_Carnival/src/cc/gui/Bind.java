package cc.gui;

import j.util.eventhandler.Posting;
import cc.gui.input.KeyBind;

class Bind extends KeyBind<Posting>
{

	private Posting onKeyPress,
		onKeyRelease,
		onKeyHold;

	public Bind( int key, Posting onKeyPress, Posting onKeyRelease,
			Posting onKeyHold ) {
		super( key );
		this.onKeyPress = onKeyPress;
		this.onKeyRelease = onKeyRelease;
		this.onKeyHold = onKeyHold;
	}

	public Posting doKeyPress() {
		return onKeyPress;
	}

	public Posting doKeyRelease() {
		return onKeyRelease;
	}

	public Posting doKeyHoldDown() {
		return onKeyHold;
	}

}
