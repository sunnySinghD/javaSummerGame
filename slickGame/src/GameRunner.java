import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public class GameRunner extends StateBasedGame {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public GameRunner() {
		super("Plantsssss");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		// TODO Auto-generated method stub
		this.addState(new MenuState(0));
		this.addState(new GameRunState(1));
		
	}
	
	public static void main(String[] args){
		try { 
			AppGameContainer app = new AppGameContainer(new GameRunner());
//			AppGameContainer app = new AppGameContainer(new AniTest());
			app.setDisplayMode(WIDTH, HEIGHT, false);
			app.start(); 
		} 
		catch (SlickException e) { 
			e.printStackTrace();
		}
	}
}
