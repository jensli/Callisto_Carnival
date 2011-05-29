package cc.app;

public enum ProgramStateCode
{
	RESTARTING,
	EXITING( true ),
	RUNNING;
	
	
	private ProgramStateCode( boolean isStopping ) {
	    this.isStopping = isStopping;
    }
	private ProgramStateCode() {}

	private boolean isStopping = false;

	public boolean isStopping() {
    	return isStopping;
    }
}
