import org.newdawn.slick.Graphics;


public class BaseLeaf extends CollisionObject {
	int baseWidth = 1;
	int baseHeight = 1;
	int health = 5;
	
	int collCounter = 0;
	static final int collCounterMax = 1000;
	
	int healCounter = 0;
	static final int healCounterMax = 40000;
	public BaseLeaf(){
		super();
	}
	
	public BaseLeaf(int x, int y, int w, int h){
		super(x,y,w,h);
		baseWidth = w;
		baseHeight = h;
	}
	
	public void hurtLeaf(){
		health--;
	}
	
	public void render(Graphics g){
		if (health >= 0)
		super.render(g);
		g.drawImage(img,coords[0],coords[1]);
		g.drawString(Integer.toString(health),coords[0],coords[1]);
//		g.drawString(Integer.toString(health),shape.getX(), shape.getY());
	}
}
