package cc.comm;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import cc.event.Event;


public class TestClient {
	
	Connection server;
	
	private Event e1 = Event.make("thrust 0");
	private Event e2 = Event.make("rotation 0 false");
	private Event e3 = Event.make("rotation 0 false");
	
	public TestClient()
	{
		try {
			server = new ClientSocketConnection();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		poll();
	}
	
	
//	public static void main(String[] argv)
//	{
//		new TestClient();
//	}
	
	public void poll()
	{
		ArrayList<Event> adsf = new ArrayList<Event>();
		ArrayList<Event> rec = new ArrayList<Event>();
		adsf.add(e1);
		adsf.add(e2);
		adsf.add(e3);
		
		
		while(true)
		{
			System.out.print("Client sending events ...");
			server.send(adsf);
			System.out.println(" OK.");
			
			try {
				rec.addAll( server.receive() );
			} catch (IOException e4) {
				// TODO Auto-generated catch block
				e4.printStackTrace();
			}
			
			for(Event fitta:rec)
				System.out.println("Client: Event received: "+fitta.serialize());
			
			rec.clear();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
