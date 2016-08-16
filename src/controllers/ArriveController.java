package controllers;

import engine.Car;
import engine.Game;
import engine.GameObject;

public class ArriveController extends Controller
{	
	GameObject target;
	double target_speed;

	public ArriveController(GameObject target)
	{
		this.target = target;
	}

	public Vector2D Arrive(Car character, double target_radius, double slow_radius, double time)
	{
		
		Vector2D position = new Vector2D(character.getX(), character.getY());
		
		Vector2D E = new Vector2D(target.getX(), target.getY());
		
		Vector2D D = E.subtract(position);
		
		Double length = D.length();
		
		if(length <= target_radius)
			return new Vector2D(0,0);
		
		if(length > slow_radius)
			target_speed = 250;
		
		else
			target_speed = 250 * (length/slow_radius);
		

		Vector2D normalized_D = D.normalize();
		Vector2D target_velocity = new Vector2D(normalized_D.x * target_speed, normalized_D.y *target_speed);
		Vector2D character_velocity = new Vector2D();
		character_velocity.x = character.getSpeed() * Math.cos(character.getAngle());
		character_velocity.y = character.getSpeed() * Math.sin(character.getAngle());

		Vector2D A = target_velocity.subtract(character_velocity);
		A.x = A.x/time;
		A.y = A.y/time;
		
		double length_A = A.length();
		
		if(length_A > 1.0)
		{
			A = A.normalize();
		}
		
		return A;
		
		
	}
	@Override
	public void update(Car subject, Game game, double delta_t,
			double[] controlVariables)
	
	{
	   Vector2D A = Arrive(subject, 1, 100, delta_t);
				 
	   double speed = subject.getSpeed();

	   double angle = subject.getAngle();
		 	 
	   OutputFiltering out = new OutputFiltering();
		 
	   double linear = out.linear_acceleration(angle, A);
		 			   
	   double angular = out.angular_acceleration(angle, A);
	   
	   
	   controlVariables[VARIABLE_STEERING] = angular;
       controlVariables[VARIABLE_THROTTLE] = 0;
       controlVariables[VARIABLE_BRAKE] = 0;
           

       if (linear>0) controlVariables[VARIABLE_THROTTLE] = linear;
       if (linear<0) controlVariables[VARIABLE_BRAKE] = -linear;
       if (isZero(linear) && isZero(speed) && !isZero(angular)) controlVariables[VARIABLE_THROTTLE] = 1;
       
       }
	
	
	boolean isZero(double d) 
	{
		return Math.abs(d)<=0.000001;
	}
}


