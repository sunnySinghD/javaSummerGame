import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;


public class CollisionObject extends ImageObject{
	Shape shape = null;
	int shapeWidth = 1, shapeHeight = 1;
	boolean isMoving = false;

	public CollisionObject(){
		super();
	}
	
	public CollisionObject(int aX, int aY, int aWidth, int aHeight) {
		super(aX, aY);
		shapeWidth = aWidth;
		shapeHeight = aHeight;
		shape = new Rectangle(aX,aY,aWidth,aHeight);
	}
	
	public void loadImage(String pathToImage) throws SlickException{
		super.loadImage(pathToImage);
		shapeWidth = imgWidth;
		shapeHeight = imgHeight;
		shape = new Rectangle(coords[0],coords[1],shapeWidth,shapeHeight);
	}
	
	public void render(Graphics g){
		super.render(g);
		if (shape != null){
			g.draw(shape);
		}
	}
	
	/** checks if this.shape intersects obj.shape
	 * 
	 * @param obj
	 * @return true if colliding
	 */
	public boolean isCollidingWith(CollisionObject obj){
		if ( shape.intersects(obj.shape) )
			return true;
		else return false;
	}
	
	public void move( int delta, float newX, float newY ){
		//counter += delta;
		//if (counter >= counterMax){
			//counter = 0;
			shape.setLocation(newX, newY);
		//}
	}
	
}
