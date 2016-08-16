package controllers;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

import engine.Car;
import engine.Game;
import engine.GameObject;
import engine.Obstacle;
import engine.RotatedRectangle;

public class WallAvoidanceController extends Controller 
{
	GameObject target;
	double weight1 = 1.0;
	double weight2 = 11.0;
	double factor = 200.0;


	public WallAvoidanceController(GameObject target) 
	{
		this.target = target;
	}

	public Vector2D seek(Car character, Vector2D E)
	{
		Vector2D position = new Vector2D();

		position.x = character.getX();
		position.y = character.getY();

		Vector2D D = E.subtract(position);  

		Vector2D ND = D.normalize();

		return ND;
	}



	public boolean RayCast(Car character, Game game, double angle) 
	{
		RotatedRectangle ray = new RotatedRectangle(character.getX(), 
				character.getY(), 1, 1, character.getAngle() + angle);

		for(int i=10; i<=50; i+=10)
		{
			ray.C.x  += Math.cos(ray.ang) *i;
			ray.C.y  += Math.sin(ray.ang) *i;

			if(game.collision(ray) instanceof Obstacle)
				return true;
		}

		return false;
	}

	public Vector2D compute(Car subject, Vector2D A, double angle)
	{
		Vector2D new_target = new Vector2D(Math.cos(subject.getAngle()) + angle, 
				Math.sin(subject.getAngle() + angle));

		new_target = new_target.multiplication(factor);

		new_target = new_target.add(new Vector2D(subject.getX(), subject.getY()));

		Vector2D A1 = seek(subject, new_target);

		A.x = (weight1 * A.x + weight2 * A1.x)/(weight1 + weight2);
		A.y= (weight1 * A.y +  weight2 * A1.y)/(weight1 + weight2);

		return A;
	}
	
	public Vector2D new_acceleration(Car subject, Game game, Vector2D A)
	{
		boolean c = RayCast(subject, game, 0);              //center 
		boolean l = RayCast(subject, game, -Math.PI/4);     //left
		boolean r = RayCast(subject, game, Math.PI/4);      //right
		

		if(!c && l && r)	
			return compute(subject, A, 0.0);		

		if(c && l && !r)
			return compute(subject, A, Math.PI/4);

		else if(c && !l && r)
			return compute(subject, A, -Math.PI/4);

		else if(c && l && r)
			return compute(subject, A, Math.PI);

		else if(!c && l && !r)
			return compute(subject, A, Math.PI/4);

		else if(!c && !l && r)
			return compute(subject, A, -Math.PI/4);

		else if(c && !l && !r)
			return compute(subject, A, Math.PI);

		
		return A;
	}
	@Override
	public void update(Car subject, Game game, double delta_t,
			double[] controlVariables)
	{
		Vector2D E = new Vector2D(target.getX(), target.getY());

		Vector2D A = seek(subject, E);

		A = new_acceleration(subject, game, A);

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
		if (isZero(linear) && isZero(speed) && !isZero(angular)) controlVariables[VARIABLE_THROTTLE] = 0.2;

	}


	boolean isZero(double d) 
	{
		return Math.abs(d)<=0.000001;
	}
}
