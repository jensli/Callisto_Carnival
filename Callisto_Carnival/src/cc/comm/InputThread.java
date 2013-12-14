package cc.comm;

import j.util.lists.Queue;
import j.util.util.Util;

import java.io.BufferedReader;
import java.io.IOException;



/**
 * Reads text using a BufferedReader, using a separate thread to make IO method calls asynchronous.
 *
 * The thread polls the [link]BufferedReader for data at preset intervals, buffering the data internally within the class
 * for instantaneous retrieval when needed. The IO mehtod calls are non-blocking (asynchronous), meaning that they will
 * return their buffered data instantly.
 *
 *
 * @author Bj�rn
 * @see BufferedReader
 */

//TODO Make sure thread is properly killed on garbagecollect

public class InputThread
{
	private BufferedReader in;
	private Queue<String> buffer;
	private boolean isClosing = false;
	private Thread thread;

	private Runnable loopRunner = new Runnable() {
		@Override public void run() {
			doInputLoop();
		}
	};

	/**
	 * Create a new instance of InputThread.
	 * @param in	The inputstream to be used
	 */
	public InputThread( BufferedReader in )
	{
		this.in = in;

		buffer = new Queue<String>();
//		initThread();
//		thread.start();

		thread = new Thread( loopRunner, "Network input" );
		thread.start();
	}


	/**
	 * Initiates the thread.
	 *
	 * Polls the stream for data every now and then,
	 * buffering it locally for your convenience.
	 */
//	private void initThread()
//	{
//		thread = new Thread("InputThread") {
//			public void run()
//			{
//				doInputLoop();
//			}
//		}; // End of inner class
//	}

	/**
	 * Reads a line of text, excluding the new-line marks
	 *
	 */
	public String readLine()
	{
		String line = null;

		synchronized(buffer) {

			if ( !buffer.isEmpty() ) {
				line = buffer.pop();
			}
		}

		return line;
	}

	/**
	 * Close the input stream, ending the thread.
	 */
	public void close()
	{
		isClosing = true;
		thread.interrupt();
	}

	/**
	 * Run on a separate thread, reads the in stream and writes
	 * the result to buffer to be checked later by the consumer
	 */
	private void doInputLoop()
	{
	    String line = null;

	    try {

		    while(!Thread.interrupted() && !isClosing) {

		    	line = in.readLine();

				if ( line != null ) {
					synchronized ( buffer ) {
						buffer.push( line );
					}
				}
 				else {
 					Util.sleep( 1 );
//		    		Logger.get().info( LogPlace.NET, "null string in " + getClass().getSimpleName() );
		    	}

		    }

		}catch (IOException e) {
    		e.printStackTrace();
    	}
	    finally {
	    	Util.close( in );
	    }

    }

}
