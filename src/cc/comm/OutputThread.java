package cc.comm;

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * OutputThread, a PrintWriter decorator class
 *
 * Calls to write-methods are asynchronous (The program will not halt waiting for the
 * stream to flush the outgoing data)
 *
 * @author Bj�rn
 *
 */

// TODO Make sure thread is properly killed on garbagecollect

public class OutputThread
{
	private PrintWriter stream;
//	LinkedList<String> buffer = new LinkedList<String>();;
	private BlockingQueue<String> buffer = new LinkedBlockingQueue<String>();
	private boolean isClosing;
	Thread thread;

	Runnable loopRunner = new Runnable() {
		@Override public void run() {
			try {
                while ( !Thread.interrupted() && !isClosing ) {
                	String out = buffer.take();
                	do {
	                	stream.println( out );
	                	out = buffer.poll();
                	} while ( out != null );
            		stream.flush();
                }
            } catch ( InterruptedException e ) {
                ;//e.printStackTrace();
            } finally {
            	// Close stream on exit
            	stream.close();
            }
		}
	};

//	Runnable loopRunner = new Runnable() {
//		@Override public void run() {
//			try {
//                while ( !Thread.interrupted() && !isClosing ) {
//
//                	String out = buffer.take();
//
//                	stream.println( out );
//            		stream.flush();
//                }
//            } catch ( InterruptedException e ) {
//                ;//e.printStackTrace();
//            } finally {
//            	// Close stream on exit
//            	stream.close();
//            }
//		}
//	};

	public OutputThread( PrintWriter s )
	{
		stream = s;
		isClosing = false;

		thread = new Thread( loopRunner, "Network output" );
		thread.start();
	}

	public void println( String s )
	{
		buffer.add( s );
	}

	/**
	 * Close the stream, kill the thread.
	 */
	public void close()
	{
		isClosing = true;
		thread.interrupt();
	}


}
