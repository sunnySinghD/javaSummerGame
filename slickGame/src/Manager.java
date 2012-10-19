import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Shape;


public class Manager {
	ArrayList<Fly> allFlies;
	static Random rand;
	Plant plant;
	Image background;
	int score;
	Scores scoreManager;
	
	// number of flies that will be generated
	int maxNumberOfFlies = 1;
	int makeFlyCounter;
	static int makeFlyCounterMax = 100;
	
	public enum State { LEVEL1, LEVEL2, LEVEL3, LEVEL4, LEVEL5, LEVEL6};
	State state;
	
	public enum GameState { RUNNING, STORE, PAUSED, SHOW_SCORES, EXIT};
	GameState gameState;

	private boolean isPaused = false;

	public Manager(){
		allFlies = new ArrayList<Fly>();
		plant = new Plant();
		rand = new Random();
		score = 0;
		state = State.LEVEL1;
		scoreManager = new Scores();
		gameState = GameState.RUNNING;
		try {
			background = new Image("background.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void reset(){
		allFlies = new ArrayList<Fly>();
		plant = new Plant();
		rand = new Random();
		score = 0;
		state = State.LEVEL1;
		maxNumberOfFlies = 1;
		makeFlyCounterMax = 100;
		gameState = GameState.RUNNING;
		isPaused = false;
		readFromFile();
	}
	
	public void managePause(){
		if (gameState == GameState.PAUSED) gameState = GameState.RUNNING;
		else if (gameState == GameState.RUNNING) gameState = GameState.PAUSED;
		else if (gameState == GameState.SHOW_SCORES) gameState = GameState.EXIT;
	}
	public void pause(){
		gameState = GameState.PAUSED;
		isPaused = true;
	}

	public void unpause(){
		gameState = GameState.RUNNING;
		isPaused = false;
	}

	/** make a fly at a random location near top of screen that is aimed at one of the leaves
	 * 
	 */
	public void makeFly(){

		// get a random leaf
		int index = rand.nextInt(plant.leaves.size());
		BaseLeaf b = plant.leaves.get(index);

		float x = rand.nextInt(GameRunner.WIDTH+200)-100;
		float y = rand.nextInt(GameRunner.HEIGHT/5)-GameRunner.HEIGHT/5;

		Fly f = new Fly(b.coords[0], b.coords[1], x, y);

		for(int i = 0; i<maxNumberOfFlies; i++) {
			f.speedUp();
		}
		allFlies.add(f);
	}

	public boolean myContains(Shape big, Shape small){
		if (big.getMaxX() >= small.getMaxX() &&
				big.getMinX() <= small.getMinX() &&
				big.getMaxY() >= small.getMaxY() &&
				big.getMinY() <= small.getMinY() )
			return true;
		else return false;
	}

	/** if the current dest of the fly is gone, assign it a new leaf/destination
	 * 
	 * @param f 
	 */
	public void findNextLeaf(Fly f){
		float lowestDist;
		float temp;
		int size = plant.leaves.size();
		if (size > 0){
			int index = rand.nextInt(size);
			BaseLeaf b = plant.leaves.get(index);
			f.setDestCoords(b.coords[0], b.coords[1]);
			/*
			for(BaseLeaf b: plant.leaves){
				//temp = distance(b.coords, )
			}
			 */
		}
		else f.die();
	}

	/** if a leaf is dead, change dests of all flies that are moving to dead leaf to another leaf
	 * 
	 * @param b: the dead leaf
	 */
	public void findNextLeafForAllFlies(BaseLeaf b){
		Fly aFly;
		for (Iterator<Fly> iter = allFlies.iterator(); iter.hasNext();) {
			aFly = iter.next();
			if (Arrays.equals(aFly.dest,b.coords)) findNextLeaf(aFly);
			/*
			else if (Math.abs(aFly.dest[0]-b.coords[0]) <= 0.1 && Math.abs(aFly.dest[1]-b.coords[1]) <= 0.1){
				System.out.println("fuck");
			}
			 */
		}
	}
	public float distance(float[] set1, float[] set2){
		float dist;
		return dist = (float)Math.sqrt(Math.pow(set1[0]-set1[0],2) + Math.pow(set2[1]-set2[1], 2));
	}

	/** squared distance from start---(x1,y1) to dest---(x2,y2)
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static float sqdist(float x1, float y1, float x2, float y2){
		return (float) (Math.pow(x2-x1,2) + Math.pow(y2-y1, 2));
	}


	/** increments a collision counter for a given fly and damage the health of a leaf after enough time
	 * 
	 * @param afly
	 * @param aleaf
	 * @param delta
	 */
	public void leafAndFlyCollision(Fly afly, BaseLeaf aleaf, int delta){
		if(afly.isCollidingWith(aleaf)){
			//afly.counter
			aleaf.healCounter = 0;
			aleaf.collCounter+=delta;
			if (aleaf.collCounter > BaseLeaf.collCounterMax){
				aleaf.hurtLeaf();
				aleaf.collCounter=0;
			}
		}
		//else
		//aleaf.collCounter=0;
	}

	/** manages the collisions between plant and a fly, killing a fly if its been eaten (after enough time)
	 * 
	 * @param afly
	 * @param delta
	 */
	public void plantAndFlyCollision(Fly afly, int delta) {
		if (myContains(plant.shape,afly.shape)) {
			plant.isEating=true;
			plant.eatCounter+=delta;
			if (plant.eatCounter >= Plant.eatCounterMax){
				afly.die();
				score+=100;
				plant.stretchCounter-=500;
				plant.eatCounter = 0;
				plant.isEating=false;
			}
		}
		//else plant.eatCounter = 0;
	}

	/** generates flies based on a timer/counter, limited by maxNumberOfFlies
	 * 
	 * @param delta
	 */
	public void timedFlyMaker(int delta){
		makeFlyCounter += delta;
		if (makeFlyCounter >= makeFlyCounterMax){
			//int numberOfFliesToMake = maxNumberOfFlies - allFlies.size();
			//for(int i = 0; i < numberOfFliesToMake; i++){
			if (allFlies.size() < maxNumberOfFlies){
				if (plant.leaves.size() > 0){
					makeFly();
					makeFlyCounter = 0;
				}
				// if there are no more leaves left, we exit to menu
				else {
//					gameState=GameState.EXIT;
					scoreManager.addScore(score);
//					for (int i=0; i<10; i++) {
//						scoreManager.addScore(i*1000);
					//highScores.print();
					scoreManager.print();
					gameState=GameState.SHOW_SCORES;
//					}
				}
			}
		}

	}

	/** changes the level of the game if depending on score
	 *  
	 */
	public void stateChange(){
		State newState;
		if (score < 1000) newState = State.LEVEL1;
		else if (score < 2000) newState = State.LEVEL2;
		else if (score < 3000) newState = State.LEVEL3;
		else if (score < 4000) newState = State.LEVEL4;
		else if (score < 5000) newState = State.LEVEL5;
		else newState = State.LEVEL6;

		boolean hasChanged = false;
		if (state != newState){
			hasChanged = true;
			state = newState;
		}

		if (hasChanged){
			switch( state ){
			case LEVEL1 : 
				maxNumberOfFlies = 1;
				makeFlyCounterMax = 900;
				break;
			case LEVEL2 : 
				maxNumberOfFlies = 2;
				break;
			case LEVEL3 :
				makeFlyCounter = 800;
				break;
			case LEVEL4 :
				maxNumberOfFlies = 3;
				break;
			case LEVEL5 :
				makeFlyCounterMax = 700;
				break;
			case LEVEL6 :
				maxNumberOfFlies = 4;
				break;

			}
		}
	}

	public void healLeaves(int delta){
		BaseLeaf aLeaf;
		for(Iterator<BaseLeaf> bIter = plant.leaves.iterator(); bIter.hasNext();){
			aLeaf = bIter.next();
			aLeaf.healCounter += delta;
			if (aLeaf.healCounter >= BaseLeaf.healCounterMax){
				aLeaf.health++;
				aLeaf.healCounter = 0;
			}
		}
	}
	
	public void readFromFile(){
		Path file = Paths.get("blah.tst");
		ObjectInputStream in = null;

		try {
			in = new ObjectInputStream(new BufferedInputStream(Files.newInputStream(file))); 
			scoreManager = (Scores) in.readObject();
			in.close();
		}
		catch (IOException | ClassNotFoundException x){
			System.err.println(x);
		}
	}

	/** called on every tick to update all flies and check for dead ones. checks for collisions
	 * 
	 * @param delta
	 */
	public void update(int delta){

		switch (gameState){
		case RUNNING:
			// usual code
			plant.update(delta);
			stateChange();
			healLeaves(delta);
			/*
			// add a new fly if there are none
			if (allFlies.size() < maxNumberOfFlies){
				makeFly();
			}
			 */

			// make flies
			timedFlyMaker(delta);

			Fly afly;
			plant.isEating=false;
			// iterate over all flies
			for (Iterator<Fly> iter = allFlies.iterator(); iter.hasNext();) {

				// update a fly
				afly = iter.next();
				afly.update(delta);

				// remove dead fly
				if (afly.isDead){
					iter.remove();
				}

				plantAndFlyCollision(afly, delta);

				// check for collisions with any leaf
				for (Iterator<BaseLeaf> bIter = plant.leaves.iterator(); bIter.hasNext();){
					BaseLeaf aleaf = bIter.next();

					// manage collisions between a leaf and a fly
					leafAndFlyCollision(afly, aleaf, delta);
					/*
					if (afly.isCollidingWith(aLeaf)){
						//afly.die();
						findNextLeaf(afly);
						aLeaf.hurtLeaf();
					}
					 */
					//remove a leaf if it has no health
					if (aleaf.health < 0){
						bIter.remove();
						findNextLeafForAllFlies(aleaf);
						score-=200;
					}
				}


				// check for collisions with the plant itself
				/*
				if (afly.isCollidingWith(plant)){	
					afly.die();
					score += 10;
				}
				 */

				if (plant.shape.contains(afly.shape)){
					afly.die();
					score += 10;
				}
			}
			break;
		case SHOW_SCORES:
			break;
		case PAUSED:
			// nothing 
			break;
		case EXIT:
			/*
			highScores.addScore(score);
			highScores.print();
			System.out.print(highScores);
			*/
			break;
		default:
			break;
		}

	}

	public void renderHighScores(Graphics g){
		g.drawString("HIGH SCORES", GameRunner.WIDTH/2 - 100, 10);
		for (int i=0; i < scoreManager.maxSize; i++)
		g.drawString(String.format("%d. %d%n", i+1, scoreManager.get(i)), 100, i*GameRunner.HEIGHT/scoreManager.maxSize);
	}
	
	public void render(Graphics g){
		g.setColor(Color.black);
		g.drawImage(background, 0, 0);
		switch (gameState){
		case PAUSED:
			// draw pause string;
			g.drawString("PAUSED", GameRunner.WIDTH/2 - 100, 10);
		case RUNNING:
			// normal drawing
			plant.render(g);
			for (Iterator<Fly> iter = allFlies.iterator(); iter.hasNext();) {
				iter.next().render(g);
			}
			g.drawString(Integer.toString(score), GameRunner.WIDTH - 100, 10);
			break;
		case SHOW_SCORES:
			renderHighScores(g);
			break;
		default:
			break;		
		}

	}
}
