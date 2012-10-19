import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;


public class Menu extends BasicGameState {
	private int stateID;
	int x,y,i;
	Manager m;
	
	Color[] textColor = new Color[3];

	public Menu(int id){
		stateID = id;
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
	}
	
	@Override
	public void mousePressed(int button, int x, int y) {
		m.plant.onClick(x, y);
	}
	
	@Override
	public int getID() {
		return stateID;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		// TODO Auto-generated method stub
		textColor[0]= Color.white;
		textColor[1]= Color.white;
		textColor[2]= Color.white;
		m = new Manager();
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		m.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {

		Input input = container.getInput();
		int mx = input.getMouseX();
		int my = input.getMouseY();

		if (mx >= 30 && mx <= 80 && my >= 50 && my <= 70)
			textColor[0]=Color.blue;
		else textColor[0]=Color.white;
		
		m.update(delta);
		//m.plant.move();
	}
}