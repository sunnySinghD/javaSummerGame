import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class ImageObject{
	float[] coords;
	Image img;
	//	int[] imgCoords;
	int imgWidth, imgHeight;
	// determines after how many counters to update position to draw correctly (how many seconds)
	int counterMax = 1;
	int moveCounter = 0;

	
	
	public ImageObject(){
		coords = new float[2];
		coords[0]=0;
		coords[1]=0;
		img = null;
		//		imgCoords = new int[2];
		//		imgCoords[0]=coords[0];
		//		imgCoords[1]=coords[1];
	}

	public ImageObject( float inX, float inY ){
		coords = new float[2];
		coords[0] = inX;
		coords[1] = inY;
		img = null;
		//		imgCoords = new int[2];
		//		imgCoords[0]=coords[0];
		//		imgCoords[1]=coords[1];
	}

	public void loadImage(String pathToImage) throws SlickException{
		img = new Image(pathToImage);
		imgWidth = img.getWidth();
		imgHeight = img.getHeight();
	}
	public void render(Graphics g){
		if (img != null){
//			g.drawImage(img, imgCoords[0], imgCoords[1]);
			g.drawImage(img, coords[0], coords[1]);
		}	
	}

}
