package edu.wpi.cs.peds.hmn.stats.costbenefit;

/**
 * @author Zhenhao Lei, zlei@wpi.edu
 */

public class Point{
	private int x;
	private float y;
	
	public Point(int x, float y)
	{
		this.x = x;
		this.y = y;
	}
	public int getX(){
		return x;
	}
	public float getY(){
		return y;
	}
}