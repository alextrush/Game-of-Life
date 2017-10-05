import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class AwarenessGame extends ThreadedGame {
	protected LevelRandomizer theGame;
	protected int numX, numY;
	public AwarenessGame() {
	}

	public AwarenessGame(int width, int height) {
		super(width, height);
	}

	public AwarenessGame(int sWidth, int sHeight, int width, int height) {
		super(sWidth, sHeight, width, height);
	}

	protected void init(){
		super.init();
		numX = 20;
		numY = 15 ;
		startGame( numX, numY );		
	}
	
	protected void load(){
	}
	
	protected GroundForce addToTheLifeStream( String[] path, float width, float height, float startX, float startY){
		GroundForce temp = new GroundForce(path, width, height, startX, startY);
		objects.add( temp );
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
				if( Keyboard.getEventKey() == Keyboard.KEY_3 ){
					theGame.randomize(3);
				}
				if( Keyboard.getEventKey() == Keyboard.KEY_4 ){
					theGame.randomize(4);
				}
				if( Keyboard.getEventKey() == Keyboard.KEY_5 ){
					theGame.randomize(5);
				}
				if( Keyboard.getEventKey() == Keyboard.KEY_6 ){
					theGame.randomize(6);
				}
				if( Keyboard.getEventKey() == Keyboard.KEY_7 ){
					theGame.randomize(7);
				}
				if( Keyboard.getEventKey() == Keyboard.KEY_8 ){
					theGame.randomize(8);
				}
				if( Keyboard.getEventKey() == Keyboard.KEY_9 ){
					theGame.randomize(20);
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
		return (int)(Mouse.getX() / screenWidth * numX );
	}
	
	public int getY(){
		return (int)( numY - (Mouse.getY() * numY / screenHeight ) );
	}
	
	public void update(){
		for( GameObject obj : objects ){
			obj.update();
		}
	}
	
	protected void render(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		if( background != null ){
			background.render( -offsetX, offsetY, screenWidth, screenHeight );
		}
		for( GameObject obj : objects ){
			if( obj.scroll() ){
				obj.render( obj.x() * screenWidth / width - foreX, screenHeight - ( obj.y() * screenHeight / height ) - foreY );
			} else {
				obj.render( obj.x() * screenWidth / width - offsetX, screenHeight - ( obj.y() * screenHeight / height ) - offsetY );
			}
		}
	}
	
	public void startGame(int i, int j) {
		theGame = new LevelRandomizer( i, j );
		GroundForce tempForce;
		int count = 0;
		for( Cell cell : theGame ){
			tempForce = addToTheLifeStream( new String[]
					{"Ground.png", "Ground1.png", "Ground2b.png", "Ground2.png", "Ground3.png", 
					"Ground4.png", "Ground5.png", "Ground6.png", "Ground7.png", "Ground8.png" }, 
					screenWidth/i, screenHeight/j, (count%i)*(screenWidth/i),(j - (count/i))*(screenHeight/j) );
			tempForce.getALife( cell );
			count++;
		}
	}
	
	public static void main(String[] args) {
		AwarenessGame g = new AwarenessGame();
		g.start();
	}
}