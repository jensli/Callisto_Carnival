package cc.game.objects;

import cc.game.GameObject;

public abstract class ObjectBehavior
{
	public void create() {}
	public void die() {}
	public void makeObject() {}

	public void update( GameObject controlled, double dT ) {}


}
