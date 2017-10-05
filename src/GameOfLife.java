import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.File;

public class GameOfLife extends ThreadedGame {
	protected Grid theGame;
	protected int numX, numY;
	public GameOfLife() {
	}

	public GameOfLife(int width, int height) {
		super(width, height);
	}

	public GameOfLife(int sWidth, int sHeight, int width, int height) {
		super(sWidth, sHeight, width, height);
	}

	protected void init(){
		super.init();
		numX = 30;
		numY = 20;
		startGame( numX, numY );		
	}
	
	protected void load(){
	}
	
	protected LifeForce addToTheLifeStream( String path, float width, float height, float startX, float startY){
		LifeForce temp = new LifeForce(path, width, height, startX, startY);
		objects.add( temp );
		System.out.println( temp );
		return temp;
	}
	
	protected void readEvents(){
		while( Keyboard.next() ){
			if( Keyboard.getEventKeyState() ){
				if( Keyboard.getEventKey() == Keyboard.KEY_SPACE ){
					theGame.next();
				}
				if( Keyboard.getEventKey() == Keyboard.KEY_C ){
					theGame.clear();
				}
				if( Keyboard.getEventKey() == Keyboard.KEY_F ){
					theGame.fill();
				}
				if( Keyboard.getEventKey() == Keyboard.KEY_R ){
					theGame.randomize();
				}				
			}
		}
		while( Mouse.next() ){
			if( Mouse.getEventButtonState() ){
				if( Mouse.getEventButton() >= 0 ){
					int x = getX();
					int y = getY();
					theGame.set(x, y, !theGame.alive(x, y) );
				}
			}
		}
	}
	
	public int getX(){
		return (int)(Mouse.getX() * numX / screenWidth );
	}
	
	public int getY(){
		return (int)( numY - (Mouse.getY() * numY / screenHeight ) );
	}
	
	public void update(){
		int count = 0;
		for( GameObject obj : objects ){
			obj.update();
		}
	}
	
	public void startGame(int i, int j) {
		theGame = new Grid( i, j );
		LifeForce tempForce;
		int count = 0;
		for( Cell cell : theGame ){
			tempForce = addToTheLifeStream("Green Square.png", screenWidth/i, screenHeight/j,
											(count%i)*(screenWidth/i),(j - (count/i))*(screenHeight/j) );
			tempForce.getALife( cell );
			count++;
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Hi?");
		GameOfLife g = new GameOfLife();
		g.start();
	}
}