import java.util.Comparator;
import java.util.LinkedList;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.*;

public class Scores extends LinkedList<Integer>{ 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final int maxSize = 10;

	/*
	public Scores(){
		super();
	}
	 */
	public Scores(){
		super();
		for(int i=0; i<maxSize; i++){
			add(0);
		}
	}


	public void addScore(int score){
		for(int i=0; i<maxSize; i++){
			// if the score is higher than the score stored at index i, insert it at i
			if (score > get(i)){
				add(i,score);
				break;
			}
		}
		if (size() > maxSize) remove(maxSize);
	}

	public void print(){
		int i = 0;
		for(int x:this){
			System.out.format("Score #%d is: %d%n", ++i, x);
		}
	}

	public void saveToFile(){
		Path file = Paths.get("blah.tst");
		ObjectOutputStream out = null;

		try {
			out = new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(file, CREATE, TRUNCATE_EXISTING))); 
			out.writeObject(this);
			out.close();
		}
		catch (IOException x){
			System.err.println(x);
		}
	}
}

class scoreComparator implements Comparator<Integer> {

	@Override
	public int compare(Integer o1, Integer o2) {
		if (o1 > o2) return -1;
		else if (o1 == o2) return 0;
		else return 1;
	}


}