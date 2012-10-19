import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;


public class Button{
	Shape shape;
	String label;
	
	public Button(String text, int x, int y){
		label = text;
		shape = new Rectangle(x,y, 200, 200);
	}
	
	public boolean containsPoint( float x, float y){
		return shape.contains(x, y);
	}
	
	public void render(Graphics g){
		g.draw(shape);
		g.drawString(label, shape.getCenterX(), shape.getCenterY());
	}
}
