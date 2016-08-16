package controllers;

public class OutputFiltering 
{
    
    public double linear_acceleration(double angle, Vector2D A)
    {
    	Vector2D direction = new Vector2D(Math.cos(angle), Math.sin(angle));
    	
    	return direction.dotProduct(A);
    	    	
    }
  
    
    public double angular_acceleration(double angle, Vector2D A)
    {
    	Vector2D right = new Vector2D(Math.cos(angle+Math.PI/2), Math.sin(angle+Math.PI/2));
    	
    	return A.dotProduct(right);
    	
    }
}
