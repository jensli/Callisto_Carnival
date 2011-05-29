package cc.comm;

import java.io.IOException;


/**
 * Host, the player that hosts the game has an instance of this class running
 * 
 * 
 * @author Bj�rn
 *
 */

public class Host {
	
	private ConnectionServer server;
	
	public Host()
	{
		try {
			server = new ConnectionServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Connection connectLocalClient()
	{
		return server.getDirectConnection();
	}
	
	public ConnectionServer getServer()
	{
		return server;
	}
	
	public void stopListening()
	{
		server.stopListening();
	}
	

}
