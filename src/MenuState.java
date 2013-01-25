import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class MenuState extends BasicGameState{
	private int stateID;
	Button start;
	Button exit;
	boolean startGame = false;
	boolean exitGame = false;
	
	public MenuState(int id){
		stateID = id;
	}
	
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		startGame = false;
		exitGame = false;
	}
	
	@Override
	public void mousePressed(int button, int x, int y){
		if (start.containsPoint(x, y)){
			startGame = true;
		}
		if (exit.containsPoint(x, y)){
			exitGame = true;
		}
	}
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		start = new Button("Start", 50, 200);
		exit = new Button("Exit", 50, 400);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.setColor(Color.white);
		start.render(g);
		exit.render(g);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// TODO Auto-generated method stub
		if (startGame) game.enterState(1);
		if (exitGame) System.exit(0);
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return stateID;
	}
}
