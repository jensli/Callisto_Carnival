package cc.game;


public class ShieldedHealth implements Health
{
	private double
		healthLevel = 100,
		sheild = 0,
		normalHealth = 100,
		normalSheild = 0,
		sheildRechargeRate = 0.1;

	@Override
	public void energyDamage( double d )
	{
	}

	@Override
	public boolean isAlive()
	{
		return healthLevel > 0;
	}

	@Override
	public void kill()
	{
	}

	@Override
    public void update( double dT )
	{
		sheild += sheildRechargeRate * dT;
	}

	@Override
	public void normalDamage( double d )
	{
		sheild -= d;

		if ( sheild < 0 ) {
			healthLevel += sheild;
			sheild = 0;
			healthLevel = Math.max( 0, healthLevel );
		}
	}

	@Override
	public void radiationDamage( double d )
	{
		healthLevel -= d;
		healthLevel = Math.max( 0, healthLevel );
	}


	/*
	 * Setters and getters
	 */
	public double getHealthLevel() {
		return healthLevel;
	}
	public void setHealthLevel( double healthLevel ) {
		this.healthLevel = healthLevel;
	}
	public void incHealth( double d ) {
		this.healthLevel += d;
	}
	public double getSheild() {
		return sheild;
	}
	public void setSheild( double sheild ) {
		this.sheild = sheild;
	}
	public double getNormalHealth() {
		return normalHealth;
	}
	public void setNormalHealth( double normalHealth ) {
		this.normalHealth = normalHealth;
	}
	public double getNormalSheild() {
		return normalSheild;
	}
	public void setNormalSheild( double normalSheild ) {
		this.normalSheild = normalSheild;
	}
}
