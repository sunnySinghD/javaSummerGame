import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Plant extends CollisionObject{

	ArrayList<BaseLeaf> leaves;
	float[] dest;

	// coords to draw the rectangle, calculated with respect to the
	// actual coordinates from CollisionObject coords[]
	float rectSrcCoordX;
	float rectSrcCoordY;

	static final int startX = GameRunner.WIDTH/2;
	static final int startY = GameRunner.HEIGHT - 100;

	int eatCounter = 0;
	static final int eatCounterMax = 400;

	private int moveCounter;

	int stretchCounter = 0;
	static final int stretchCounterMax = 2000;
	float stretchRate = 1.0f;

	int waitCounter = 0;
	static final int waitCounterMax = 500;

	float speed = 0.5f;
	float distance = 0;
	boolean destIsHome = true;
	float startRadius;
	float baseSpeed = 1.0f;
	float distanceAtB;
	Animation ani;
	Animation eat;
	Animation vine;
	boolean isEating;
	Image[] vineframes = new Image[3];
	Image vinep= null;

	public Plant(){
		super(startX,startY, 100,100);
		try {
			loadImage("plant.png");
			Image[] frames = new Image[4];
			frames[0]= new Image("plant1.png");
			frames[1]= new Image("plant2.png");
			frames[2]= new Image("plant1.png");
			frames[3]= new Image("plant3.png");
			Image[] frames2 = new Image[6];
			frames2[0] = frames[0];
			frames2[1] = new Image("plant4.png");
			frames2[2] = new Image("plant5.png");
			frames2[3]= new Image("plant6.png");
			frames2[4]=frames[2];
			frames2[5]=frames2[1];

			vineframes[0] = new Image("vine1.png");
			vineframes[1] = new Image("vine2.png");
			vineframes[2] = new Image("vine3.png");
			ani = new Animation(frames, 150);
			eat = new Animation(frames2, 50);
			vine = new Animation(vineframes, 200);

			vinep = new Image("vinepiece.png");

		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setRectSrcCoords();
		leaves = new ArrayList<BaseLeaf>();
		leaves.add(new BaseLeaf(GameRunner.WIDTH/2 - 100, GameRunner.HEIGHT - 100, 50, 50));
		leaves.add(new BaseLeaf(GameRunner.WIDTH/2 + 100, GameRunner.HEIGHT - 100, 50, 50));
		try {
			leaves.get(0).loadImage("leafleft.png");
			leaves.get(1).loadImage("leafright.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dest = new float[2];
		dest[0] = startX; dest[1] = startY;
		startRadius = Manager.sqdist(GameRunner.WIDTH/2, GameRunner.HEIGHT, startX, startY);
		distanceAtB = 2*startRadius;
	}

	public void setRectSrcCoords(){
		rectSrcCoordX = coords[0]-shapeWidth/2;
		rectSrcCoordY = coords[1]-shapeHeight/2;
		shape.setLocation(rectSrcCoordX,rectSrcCoordY);

	}

	public void renderVine(Graphics g){
		float dist = Manager.sqdist(GameRunner.WIDTH/2, GameRunner.HEIGHT, coords[0], coords[1]);
		int numberOfBlocks = (int)Math.sqrt(dist)/10;
		for(int i=0; i<numberOfBlocks; i++){
			g.drawImage(vinep, GameRunner.WIDTH/2, GameRunner.HEIGHT-i*10);
		}
	}
	public void render(Graphics g){
		//g.draw(shape);
		drawLeaves(g);
		renderVine(g);
		g.drawLine(GameRunner.WIDTH/2, GameRunner.HEIGHT, coords[0], coords[1]);
		//g.drawImage(img, rectSrcCoordX, rectSrcCoordY);
		g.drawImage(vineframes[0], GameRunner.WIDTH/2, GameRunner.HEIGHT, coords[0]-vineframes[0].getWidth()/2, coords[1], 0, 0, vineframes[0].getWidth(), vineframes[1].getHeight());
		g.drawAnimation(vine, coords[0]-vine.getWidth()/2, coords[1]);

		if (isEating) g.drawAnimation(eat, rectSrcCoordX, rectSrcCoordY);
		else{
			g.drawAnimation(ani, rectSrcCoordX, rectSrcCoordY);
		}
		g.drawString(Integer.toString(stretchCounter), 100, GameRunner.HEIGHT - 100);
		g.drawString(Float.toString(speed), 100, GameRunner.HEIGHT - 50);
	}

	public void drawLeaves(Graphics g){
		for (Iterator<BaseLeaf> iter = leaves.iterator(); iter.hasNext();){
			BaseLeaf b = iter.next();
			b.render(g);

		}
	}

	public void move(){

		float dx = dest[0]-coords[0];
		float dy = dest[1]-coords[1];
		if ( Math.abs(dx) >= 0.1 || Math.abs(dy) >= 0.1) {

			float slope = dy/dx;
			int signX = (dx > 0) ? 1:-1;
			int signY = (dy > 0) ? 1:-1;

			if (Math.abs(slope) >= 2){
				coords[1] += signY*speed;
			}

			else {
				coords[0] += signX*speed;
				coords[1] += slope * signX*speed;
			}

			setRectSrcCoords();
		}	



	}

	/** are the current coords close enough to the current dest
	 * 
	 * @return
	 */
	public boolean hasReachedDest(){
		float x = Math.abs(dest[0]-coords[0]);
		float y = Math.abs(dest[1]-coords[1]);
		//System.out.println(x);
		//System.out.println(y);
		if (x <= 1 && y <= 1) return true;
		else return false;
	}

	public void updateSpeed(){
		float squaredDistance = Manager.sqdist(startX, startY, coords[0], coords[1]);
		if (squaredDistance <= startRadius){
			speed = 1.0f;
			//			stretchRate = 1.0f;
		}
		else if (squaredDistance <= distanceAtB){
			speed = 1.0f;
			//			stretchRate = 0.8f;
		}
		else{
			//			stretchRate = 0.8f;
			if (squaredDistance <= distanceAtB*1.5)
				speed = baseSpeed * distanceAtB/squaredDistance;
			else if (squaredDistance <= distanceAtB*2)
				speed = baseSpeed * distanceAtB/squaredDistance;
		}
	}

	public void update(int delta){
		// increment counter
		moveCounter += delta;

		setRectSrcCoords();

		updateSpeed();

		if (stretchCounter >= stretchCounterMax){
			// cannot set the dest any more after stretch is maxed, goes home
			onRelease();
		}	

		// check to see if intended to reach starting position
		if (hasReachedDest() && destIsHome){
			// reset stretch counter and stop moving
			stretchCounter = 0;
			destIsHome = false;
			isMoving = false;
		}

		if (isMoving){

			// increment stretch counter if it moves
			stretchCounter += delta;

			// if counter reaches limit, move a step towards dest[]
			if (moveCounter >= counterMax){
				moveCounter = 0;
				move();
			}
		}
	}

	public void test(){

	}
	/** as long as plant is not max stretched, tell plant to move to (x,y)
	 * 
	 * @param x
	 * @param y
	 */
	public void onClick(float x, float y){

		if (stretchCounter < stretchCounterMax){
			dest[0]=x;
			dest[1]=y;
			// in case of multiple clicks and releases before the plant is stretched to max
			destIsHome = false;
		}
	}

	/** when mouse button is released or if plant is stretched to max, tell plant to go to start position
	 * 
	 */
	public void onRelease(){
		dest[0]=startX;
		dest[1]=startY;
		// tell plant its going home
		destIsHome = true;
	}

}
