package cc.event;



public abstract class StandardEvent extends Event
{
	private Cathegory type;

	public StandardEvent( Cathegory type )
    {
	    super();
	    this.type = type;
    }
	public StandardEvent()
	{
	}

	@Override
    public cc.event.Event.Cathegory getType() {
	    return type;
    }

	void setType( Cathegory type ) {
    	this.type = type;
    }

	@Override
    public StandardEvent clone() {
		return (StandardEvent) super.clone();
	}
	public StandardEvent make() {
		return clone();
	}

}

