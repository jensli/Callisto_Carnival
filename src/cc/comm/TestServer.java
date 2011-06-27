package cc.comm;

import java.io.IOException;
import java.util.ArrayList;

import cc.event.Event;


public class TestServer {

	private ConnectionServer server;
	// private TestClient app;

	private Event e1 = Event.make("thrust 0");

//	public static void main(String[] argc)
//	{
//		System.out.println("*** TEST SERVER");
//		(new TestServer()).test();
//		System.out.println("*** MAIN THREAD ENDING");
//	}

	public void test()
	{
		try {
			server = new ConnectionServer();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ArrayList<Event> adsf = new ArrayList<Event>();
		ArrayList<Event> rec = new ArrayList<Event>();
		adsf.add(e1);

		while(true)
		{


//			System.out.println("Client sending events ...");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			rec.addAll( server.receive() );
			for(Event fitta:rec)
				System.out.println("Servah: Event received: "+fitta.serialize());

//			System.out.println("Server: Sending an event");
			server.send(rec);
			rec.clear();
		}
	}

}
