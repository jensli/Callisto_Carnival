/**
 *
 */
package cc.util;

public class Pair<TYPE1, TYPE2>
{
	public TYPE1 first;
	public TYPE2 second;

	public Pair( TYPE1 first, TYPE2 second )
    {
        this.first = first;
        this.second = second;
    }

	public static <T1, T2> Pair<T1, T2> make( T1 o1, T2 o2 )
	{
		return new Pair<T1, T2>( o1, o2 );
	}


}