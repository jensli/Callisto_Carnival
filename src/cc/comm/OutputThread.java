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
			runOutputLoop();
		}
	};
	
	public OutputThread( PrintWriter s ) 
	{
		stream = s;
		isClosing = false;

		//		initThread();
//		thread.start();
		
		thread = new Thread( loopRunner, "Network output" );
		thread.start();
	}
	
	/**
	 * Initiates the thread:
	 * 
	 * Writes data to output stream every 10ms,
	 * critical for class operation.
	 */
//	private void initThread()
//	{
//		thread = new Thread( "OutputThread" ) {
//			
//			public void run() {
//				doOutputLoop();
//			}
//			
//		}; // End of inner class
//	}
	
	
	/**
	 * Write a String to the stream + newline
	 * @param s
	 */
//	public void println( String s )
//	{
//		synchronized ( buffer ) {
//			buffer.addFirst( s );
//		}
//	}
	
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
	

	private void runOutputLoop()
	{
	    try {

		    while ( !Thread.interrupted() && !isClosing ) {
	        	
		    	String out = buffer.take();

		    	stream.println( out );
        		stream.flush();
	        }
	        
	    } catch ( InterruptedException e ) {
	        ;//e.printStackTrace();
	    } finally {
	    	// Close stream on exit
	    	stream.close();
	    }
    }
	
//	private void runOutputLoop()
//	{
//	    
//	    try {
//		    
//
//		    while ( !Thread.interrupted() && !isClosing ) {
//	        	
//		    	String out = buffer.take();
////	                	synchronized ( buffer ) {
////	                		
////	                		if ( !buffer.isEmpty() ) {
////	                			out = buffer.take();
////	                		} else {
////	                			out = null;
////	                		}
////	                	}
//	        	
//	        	if ( out != null ) {
//	        		// Write data to stream (or buffer for writing)
//	        		stream.println( out );
//	        		stream.flush();
//	        	} else {
//	        		// Force writing of data to stream
//	        		stream.flush();
//	        		
////						try {
////							Thread.sleep( 10 );
////						} catch ( InterruptedException e ) {
////							;
////						}
//	        	}
//	        }
//	        
//	    } catch ( InterruptedException e ) {
//	        ;//e.printStackTrace();
//	    } finally {
//	    	// Close stream on exit
//	    	stream.close();
//	    }
//    }

}
