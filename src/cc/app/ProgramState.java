/**
 *
 */
package cc.app;

public abstract class ProgramState
{
	public abstract void update( double localDT );
	public void tickState( double dT ) {}
	public void enter() {}
	public void exit() {}
	public void finish() {}
}