package cc.util.math;


public class Quad
{
	public Vec p1, p2, p3, p4;

	public Quad()
	{
		;
	}

	public Quad( Vec p1, Vec p2, Vec p3, Vec p4 ) {
	    this.p1 = p1;
	    this.p2 = p2;
	    this.p3 = p3;
	    this.p4 = p4;
    }

	public Quad( Quad q )
	{
		this.p1 = new Vec( q.p1 );
		this.p2 = new Vec( q.p2 );
		this.p3 = new Vec( q.p3 );
		this.p4 = new Vec( q.p4 );
	}

	public void rotateAround( Vec p, Vec dir )
	{
		sub( p );
		rotate( dir );
		add( p );
	}

//	public void rotate( final Vec dir )
//	{
//		doToAll( Vec.rotateAction( dir ) );
//	}

	public void rotate( Vec dir )
	{
		p1.rotate( dir );
		p2.rotate( dir );
		p3.rotate( dir );
		p4.rotate( dir );
	}

//	public <T> void doToAll( Action1<Vec> a )
//	{
//		a.run( p1 );
//		a.run( p2 );
//		a.run( p3 );
//		a.run( p4 );
//	}

	public Quad rotated( Vec dir )
	{
		Quad q = new Quad( this );
		q.rotate( dir );
		return q;
	}

	public void add( Vec p )
	{
		p1.add( p );
		p2.add( p );
		p3.add( p );
		p4.add( p );
	}

	public void sub( Vec p )
	{
		p1.sub( p );
		p2.sub( p );
		p3.sub( p );
		p4.sub( p );
	}

	@Override
    public String toString()
	{
		return "[" + p1 + "\n" + p2 + "\n" + p3 + "\n" + p4 + "]";
	}
}
