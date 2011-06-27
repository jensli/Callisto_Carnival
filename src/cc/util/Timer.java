package cc.util;

import org.lwjgl.Sys;


/**
 * Used to measure time
 * Unit: microseconds
 *
 * @author Jens
 */

public class Timer
{
	private long
		lastTime,
		tickInterval,
		timeLeft;

	public static final int unitsPerSec = 1000000;

	private boolean isRunning = false;

	public Timer( long newTickInterval )
	{
		tickInterval = newTickInterval;
		timeLeft = tickInterval;
	}

	public Timer()
	{
		tickInterval = Integer.MAX_VALUE;
		timeLeft = Integer.MAX_VALUE;
	}

	/**
	 * Returns true if it was at least tickInterval time since it returned true
	 * last time. Returns true once each tickInterval.
	 */
	public boolean retrieveTick()
	{
		if ( !isRunning ) {
			return false;
		}

		final long currentTime = this.getTime();

		timeLeft -= currentTime - lastTime;
		lastTime = currentTime;

		if ( timeLeft <= 0 ) {
			timeLeft += tickInterval;
			return true;
		} else {
			return false;
		}
	}

	public long retrieveElapsedTime()
	{
		if ( !isRunning ) {
			throw new IllegalStateException( "Timer not running" );
		}

		final long currentTime = this.getTime(),
			returnTime = currentTime - lastTime;

		lastTime = currentTime;

		return returnTime;
	}

	public double retrieveElapsedMillis() {
		return retrieveElapsedTime() / 1000.0;
	}

	public long getTime()
	{
		return ( Sys.getTime() * unitsPerSec ) / Sys.getTimerResolution();
	}

//		return System.nanoTime();
//		return System.currentTimeMillis() * 1000000;

	public void start()
	{
		start( 0 );
	}

	public void start( long offset )
	{
		isRunning = true;
		lastTime = getTime() - offset;
	}

	public void stop()
	{
		isRunning = false;
	}
}
