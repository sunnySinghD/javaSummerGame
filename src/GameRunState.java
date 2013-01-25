import java.io.File;
import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class GameRunState extends BasicGameState {
	private int stateID;
	int x,y,i;
	Manager m;
	
	public GameRunState(int id){
		stateID = id;
	}
	
	@Override
	public void keyPressed(int key, char c){
		if (key == Input.KEY_O) m.plant.speed+=0.1;
		else if (key == Input.KEY_P) m.plant.speed-=0.1;
		else if (key == Input.KEY_SPACE) m.managePause();
		
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		
		//m.plant.move(newx, newy);
	}
	
	@Override
	public void mousePressed(int button, int x, int y){
		//if (m.gameState == Manager.GameState.RUNNING){
		m.plant.isMoving=true;
		m.plant.onClick(x, y);
		//}
		//m.plant.lockDest = true;
//		m.plant.dragged(x, y);
	}
	
	@Override
	public void mouseReleased(int button, int x, int y){
		//if (m.gameState == Manager.GameState.RUNNING) 
			m.plant.onRelease();
		
		//m.plant.isMoving=true;
		//m.plant.isMoving=false;
	}
	
	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy){
//		m.plant.dragged(newx, newy);
//		if (!m.plant.lockDest) 
		//if (m.gameState == Manager.GameState.RUNNING) 
		m.plant.onClick(newx, newy);
		
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		m.scoreManager.saveToFile();
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		//m.gameState = Manager.GameState.RUNNING;
		
		/* upon entering the GameRunState, create a new manager, wiping out the old manager.
		 *  this only works in java since there is automatic garbage collection. 
		 *  any other language, must wipe out old manager. make one instance in init(...) function 
		 *  and then (re)initialize all the data here in enter(...)
		 */
		m.reset();
		//File f = new File("blah");
//		Path filePath = "";
		
		/*
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		m.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		m.update(delta);
		if (m.gameState == Manager.GameState.EXIT) game.enterState(0);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		// TODO Auto-generated method stub
		m = new Manager();
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return stateID;
	}
}
