
import java.util.ArrayList;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class ThreadedGame implements Runnable{
	
	protected float screenWidth, screenHeight;
	protected float width, height;
	protected float offsetX, offsetY;
	protected float foreX, foreY;
	protected ArrayList<GameObject> objects;
	protected Thread thread;
	protected TexturedVBO background;
	
	//creates a game window with default size 800x600
	public ThreadedGame(){
		screenWidth = 800;
		screenHeight = 600;
		width = 800;
		height = 600;
	}	
	
	//creates a game window with a specific dimension
	public ThreadedGame(int width, int height){
		this.screenWidth = width;
		this.screenHeight = height;
		this.width = 800; 
		this.height = 600;
	}
	
	//creates a game window with a specific dimension
	public ThreadedGame(int sWidth, int sHeight, int width, int height){
		this.screenWidth = sWidth;
		this.screenHeight = sHeight;
		this.width = width; 
		this.height = height;
	}

	public void start(){
		//starts the thread which will run the game loop
		if ( thread == null ){
			thread = new Thread( this );
			thread.start();
		}
	}
	
	protected void init(){
		// Initialize OpenGL
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glOrtho(0, screenWidth, screenHeight, 0, 1, -1);
		//GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glViewport(0, 0, (int)screenWidth, (int)screenHeight);
		
		//allow textures
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		//refresh at 60 fps
		Display.sync(60);
		objects = new ArrayList<GameObject>();
		offsetX = 0; offsetX = 0; foreX = 0; foreY = 0;
	}
	
	public void run(){
		try{
			//creates a display
			Display.setDisplayMode( new DisplayMode((int)screenWidth, (int)screenHeight) );
			Display.create();
			Display.setResizable(true);
			//sets up game
			init();
			load();
		} catch ( LWJGLException e ){
			//occurs when a display is already being used by the computer
			e.printStackTrace();
			System.exit(-1);
		}
		while(!Display.isCloseRequested()){
			//receive imput from any devices used to control the game
			readEvents();
			//update the elements on the screen that need to be
			update();
			//draw everything to the screen
			render();
			Display.update();
			//respond to any window resizing
			if( Display.wasResized() ){
				resize();
			}
			//refresh the screen at 60 fps
			Display.sync(60);
		}
		//clean up any resources upon finishing
		end();
	}
	
	protected void load(){
		background = TextureUtil.loadTiledVBO( "Red Square.png", 400, 200, 1, 2 );
	}
	
	protected void loadFile(){
	}
	
	protected GameObject add( String path, int width, int height, float startX, float startY ){
		GameObject temp = new GameObject(path, width, height, startX, startY);
		objects.add( temp );
		return temp;
	}
	
	protected void add( GameObject obj ){
		objects.add( obj ); 
	}
	
	public void loadObjects( ArrayList<GameObject> objects ){
		this.objects = objects;
	}
	
	protected void readEvents(){}
	
	protected void update(){}
	
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
	
	protected void resize(){
		screenWidth = Display.getWidth();
		screenHeight = Display.getHeight();
		GL11.glViewport( 0, 0, (int)screenWidth, (int)screenHeight );
	}
	
	protected void dispose(){
		for( GameObject obj : objects ){
			obj.dispose();
		}
	}
	
	protected void end(){
		//stop the thread
		thread = null;
		//remove any resources
		dispose();
		if( background != null ){
			background.dispose();
		}
		//remove the display
		Display.destroy();
		//exit the program
		System.exit(0);
	}

	public static void main( String[] args ){
		ThreadedGame t = new ThreadedGame();
		t.start();
	}
}