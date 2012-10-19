import java.util.Random;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;


public class Fly extends CollisionObject{
	float[] dest;
	float[] center;
	// sums deltas from each update() to compare with counterMax
	static Random rand;
	float angle = 0;
	boolean isDead = false;
	static final int flyRadius = 4;
	float speed = 0.5f;
	
	public Fly(){
		super();
		shape = new Circle(coords[0], coords[1], flyRadius);
		dest = new float[2];
		center = new float[2];
		center = coords;
		setDestCoords(100f,100f);
		isMoving = true;
		counterMax = 10;
	}
	
	public Fly(float x, float y){
		super();
		shape = new Circle(coords[0], coords[1], flyRadius);
		dest = new float[2];
		center = new float[2];
		center = coords;
		setDestCoords(x,y);
		isMoving = true;
		counterMax = 5;
	}
	
	public Fly(float x1, float y1, float x2, float y2){
		super();
		shape = new Circle(coords[0], coords[1], flyRadius);
		dest = new float[2];
		center = new float[2];
		setDestCoords(x1, y1);
		setCoords(x2, y2);
		center = coords;
		isMoving = true;
		counterMax = 5;		
	}
	public void setCoords(float x, float y){
		coords[0] = x;
		coords[1] = y;
	}
	public void setDestCoords(float x, float y){
		dest[0] = x;
		dest[1] = y;
	}
	/** creates circular motion for each fly
	 * @param radius
	 */
	public void orbit(int radius){
		if (angle >= 6.2) angle = 0;
		else angle += 0.1;
		coords[0] = center[0] + radius*(float)Math.cos(angle);
		coords[1] = center[1] + radius*(float)Math.sin(angle);
		shape.setLocation(coords[0], coords[1]);
	}
	
	/** move a fly in a straight line from src( coords[] ) to dest( dest[] )
	 * 
	 * @param delta
	 */
	public void move(){
		
		float dx = dest[0]-coords[0];
		float dy = dest[1]-coords[1];
		
		if ( Math.abs(dx) >= 0.1 || Math.abs(dy) >= 0.1 ) {
			float slope = dy/dx;
			int signX = (dx > 0) ? 1:-1;
			int signY = (dy > 0) ? 1:-1;
			
			if (slope > 2 || slope < -2){
				coords[1] += signY*speed;
			}
			
			else {
				coords[0] += signX*speed;
				coords[1] += slope * signX*speed;
			}
		}

		/*
		else {
			coords = dest;
			isMoving = false;
		}
		*/
		//shape.setLocation(coords[0],coords[1]);

	}

	/** move fly in orbit and towards dest[]
	 * 
	 * @param delta
	 */
	public void update(int delta){
		// increment counter
		moveCounter += delta;
		shape.setLocation(coords[0],coords[1]);
		// always orbit
		if (moveCounter % 4 == 0)
			orbit(1);
		
		if (isMoving){
			// if counter reaches limit, move a step towards dest[]
			if (moveCounter >= counterMax){
				moveCounter = 0;
				move();
			}
		}
		
		/*
		// if fly is not moving, die
		else {
			die();
		}
		*/
		
	}
	
	/** set flag so a fly will not be drawn/ will be removed
	 * 
	 */
	public void die(){
		coords[0]=500;
		coords[1]=500;
		shape.setLocation(coords[0], coords[1]);
		isMoving = false;
		isDead = true;
	}

	/*
	public boolean isCollidingWith(BaseLeaf b){
		
		return true;
		
	}
	*/
	
	public void speedUp(){
		speed+=0.05;
	}
	
	public void render(Graphics g){
		if (!isDead)
			super.render(g);
	}

}
