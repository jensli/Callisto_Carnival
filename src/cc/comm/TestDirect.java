package cc.comm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.event.Event;


public class TestDirect {
	
	private Host host;
	private ConnectionServer server;
	private Connection toServer;
	
	private Event e1 = Event.make("thrust 0");
	private Event e2 = Event.make("rotation 0 false");
	private Event e3 = Event.make("rotation 0 false");

	List<Event> random = new ArrayList<Event>();
	List<Event> random1 = new ArrayList<Event>();
	List<Event> random2 = new ArrayList<Event>();
	
	
	public TestDirect()
	{
		host = new Host();
		toServer = host.connectLocalClient();
		server = host.getServer();
	}
	
//	public static void main(String[] argv)
//	{
//		TestDirect t;
//		t = new TestDirect();
//		try {
//			t.test();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
	
	public void test() throws InterruptedException
	{

		
		
		while(true)
		{
			random.add(e2);
			random.add(e3);
			
			toServer.send(e1);
			toServer.send(random);
			System.out.println("Client: Sent 3 events ... ");
			
			Thread.sleep(1000);
			
			random1 = server.receive();
			
			for (Event e:random1)
				System.out.println("Server: Recieved Event!! ... "+ e.serialize());
			
			Thread.sleep(1000);
			
			server.send(random);
			System.out.println("Server: Sending 2 events ... ");
			
			Thread.sleep(1000);
			
			random.add(e1);
			random.add(e2);
			
			try {
				random2 = toServer.receive();
			} catch (IOException e4) {
				e4.printStackTrace();
			}
			
			for(Event r:random2)
				System.out.println("Client: Recieved Event!! ... "+ r.serialize());
			
			System.out.println("*** CYCLE COMPLETED.");
			Thread.sleep(2000);
		}
	}
}
