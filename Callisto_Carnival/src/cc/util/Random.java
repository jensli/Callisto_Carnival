package cc.util;


public class Random
{

	private static java.util.Random
		// Seed with 0, will be the same on every machine
		instance = new java.util.Random( 0 ),
		// Randomized seed, will be different on every machine
		normalInstance = new java.util.Random();

	public static java.util.Random getNormalRandom()
	{
		return normalInstance;
	}


	public static double gameDouble( double low, double high )
	{
		return low + ( getGameRandom().nextDouble() * ( high - low ) );
	}

	public static java.util.Random getGameRandom()
	{
		return instance;
	}
}

