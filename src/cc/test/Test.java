package cc.test;

import j.util.util.Util;

import java.io.IOException;

import cc.game.GameObject;
import cc.gui.models.GraphicalModel;
import cc.gui.models.GraphicalModelGroup;
import cc.util.math.Vec;

public class Test
{
	public static void fn1() throws IOException
	{
		throw new IOException( "Wo?" );
	}
	public static void fn2() throws IOException
	{
		try {
	        fn1();
        } catch ( IOException e ) {
        	throw new IOException( "Waaa!" );
        } finally {
        	System.out.println( "FIN!" );
        }
	}
	
	
	public static class Asdf
	{
		Object[] arr = new Object[] 
		   { new Double( 1 ), new Vec( 1, 3 ), new Asdf2() };
	}
	
	
	public static class Asdf2
	{
		Object[] arr2 = new Object[]
		   { new Vec(), "asdf" };
	}
	
	public static void main( String[] args ) throws IOException
	{
//		Object d = GameFactory.get().createPlayerShip( "Tjo", Player.Color.BLUE );
		GraphicalModelGroup d = new GraphicalModelGroup( new GameObject( "tjo" ) );
		
		d.add( new GraphicalModel() {
			@Override
            public void draw( Vec pos, Vec forward ) {
            }
		}, 
		new GraphicalModel() {
			@Override
            public void draw( Vec pos, Vec forward ) {
            }
		}
		);
		
		Object o = new Asdf();
		
		System.out.println( "v = " + Util.deepToString( o ) );
	}
}
