package cc.app;

// Not used, classes internal in client app

public class ProgramStates
{
	public static interface ProgramState {
		public void update();
	}
	
	public static class MenuState implements ProgramState 
	{
		@Override
        public void update() 
		{
	        // TODO Auto-generated method stub
	        
        }
	}
	
	public static class ClientGameState implements ProgramState 
	{
		@Override
        public void update() 
		{
	        // TODO Auto-generated method stub
	        
        }
	}
	
	public static class ServerGameState extends ClientGameState 
	{
		@Override
        public void update() 
		{
	        // TODO Auto-generated method stub
	        
        }
	}
	                                     
}
