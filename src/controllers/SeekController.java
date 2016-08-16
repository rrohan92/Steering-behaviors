package controllers;

import engine.Car;
import engine.Game;
import engine.GameObject;


public class SeekController extends Controller
{
	GameObject target;
     
    public SeekController(GameObject target)
    {
    	this.target = target;
    }
    
   
    public Vector2D seek(Car character)
    {
        Vector2D position = new Vector2D();
        Vector2D E = new Vector2D();
        
        position.x = character.getX();
        position.y = character.getY();
        
        E.x = target.getX();
        E.y = target.getY();
        
        Vector2D D = E.subtract(position);  
        
        Vector2D ND = D.normalize();
        		    
        return ND;
    }
	@Override
	public void update(Car subject, Game game, double delta_t,
			double[] controlVariables)
	{         		 
		 Vector2D A = seek(subject);
		          		 
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
	       if (isZero(linear) && isZero(speed) && !isZero(angular)) controlVariables[VARIABLE_THROTTLE] = 0.5;
	       
	       }
		
		
		boolean isZero(double d) 
		{
			return Math.abs(d)<=0.000001;
		}


	}
