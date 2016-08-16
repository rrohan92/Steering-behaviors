package controllers;

public class Vector2D
{
	double x,y;

	Vector2D()
	{
		x = 0.0;
		y = 0.0;
	}

	Vector2D(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public Vector2D add(Vector2D a)
	{
		Vector2D sum =  new Vector2D(this.x + a.x, this.y + a.y);

		return sum;
	}

	public Vector2D subtract(Vector2D a)
	{
		Vector2D difference = new Vector2D(this.x - a.x, this.y - a.y);

		return difference;
	}

	public double length()
	{	  
		return Math.sqrt(this.x*this.x + this.y*this.y);  
	}

	public Vector2D normalize()
	{
		Vector2D result = new Vector2D();

		double length = Math.sqrt(this.x * this.x + this.y * this.y);

		if (length != 0)
		{
			result.x = this.x/length;
			result.y = this.y/length;
		}

		return result;
	}   

	public double dotProduct (Vector2D a) 
	{
		return this.x * a.x + this.y * a.y;
	}

	public Vector2D multiplication(double a)
	{
		this.x = this.x * a;
		this.y = this.y * a;

		return this;
	}
}

