import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
/*
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPort;
import com.illposed.osc.OSCPortOut;
*/
public class LifeMessages extends ThreadedGame {
	protected Grid theGame;
	protected int numX, numY;
	protected int numSamples;
	//protected OSCPortOut port;
	protected String[] samples;
	protected int speed;
	protected int count;
	protected boolean pause;
	public LifeMessages() {
	}

	public LifeMessages(int width, int height) {
		super(width, height);
	}

	public LifeMessages(int sWidth, int sHeight, int width, int height) {
		super(sWidth, sHeight, width, height);
	}

	protected void init(){
		super.init();
		numX = 80;
		numY = 60;
		int pitches = Math.max( numX, numY );
		numSamples = 4;
		pause = true;
		speed = 20;
		samples = new String[numSamples];
//		setAddress( "127.0.0.1", OSCPort.defaultSCLangOSCPort() );
		//port = OSCPortOut( new InetAddress( OSCPort.defaultSCLangOSCPort() );
		startGame( numX, numY, 24, pitches );		
	}
	
	protected void load(){
	}
	
	protected LifeForce addToTheLifeStream( String path, float width, float height, float startX, float startY){
		LifeForce temp = new LifeForce(path, width, height, startX, startY);
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
				if( Keyboard.getEventKey() == Keyboard.KEY_A ){
					placeGlider( getX(), getY() );
				}
				if( Keyboard.getEventKey() == Keyboard.KEY_D ){
					placeSpaceship2( getX(), getY() );
				}
				if( Keyboard.getEventKey() == Keyboard.KEY_P ){
					pause = !pause;
				}
				if( Keyboard.getEventKey() == Keyboard.KEY_S ){
					sendGrain(1,0.2, 0.3);
				}
				if( Keyboard.getEventKey() == Keyboard.KEY_N ){
					sendNotes( );
				}
				if( Keyboard.getEventKey() == Keyboard.KEY_M ){
					sendNotes( generateMelody( 12, 4 ) );
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
				if( Keyboard.getEventKey() == Keyboard.KEY_UP ){
					speed-=20;
					if( speed < 1 ){
						speed = 1;
					}
				}
				if( Keyboard.getEventKey() == Keyboard.KEY_DOWN ){
					speed+=20;
				}
			}
		}

		while( Mouse.next() ){
			if( Mouse.getEventButtonState() ){
				if( Mouse.getEventButton() >= 0 ){
					int x = getX();
					int y = getY();
					theGame.set(x, y, !theGame.alive(x, y) );
					System.out.println( theGame.value( y, x ) );
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
		if( !pause ){
			count++;
			if ( count > speed ){
				theGame.next();
				sendNotes();  
				count = 0;
			}
		}
		for( GameObject obj : objects ){
			obj.update();
		}
	}
	
	public void startGame(int i, int j, int base, int range) {
		theGame = new Grid( i, j );
//		String[] pics = {"C.png", "C#.png", "D.png", "D#.png", "E.png", "F.png", 
//				"F#.png", "G.png", "G#.png", "A.png", "A#.png", "B.png", };
//		String[] pics = { "Green Square.png", "Red Square.png" }; 
		String[] pics = {"C.png", "D.png", "E.png", "F.png", "G.png", "A.png", "B.png", };
		LifeForce tempForce;
		int[][] notes;
		if( range == 20 ){
			notes = PitchRowMatrix.free( new int[]{ 0, 1, 2, 5, 2, 2, 0, 5, 10, 11, 13, 11, 12, 16, 18, 15, 10, 5, 2, 0}, range );
		} else {
			notes = PitchRowMatrix.free( (int)Math.max( i, j ), range );
		}
		PitchRowMatrix.print( notes );
		int count = 0, temp = 0;
		for( Cell cell : theGame ){
			temp = base + notes[count % i][count/i];
//			tempForce = addToTheLifeStream(pics[temp% 12 % pics.length], screenWidth/i, screenHeight/j, 
//			(count%i)*(screenWidth/i),(j - (count/i))*(screenHeight/j) );
			tempForce = addToTheLifeStream(pics[count% 12 % pics.length], screenWidth/i, screenHeight/j, 
			(count%i)*(screenWidth/i),(j - (count/i))*(screenHeight/j) );
			tempForce.getALife( cell );
			tempForce.starve(2);
			tempForce.overcrowd(3);
			tempForce.setLowBirth(3);
			tempForce.setHighBirth(3);
			tempForce.setValue( temp );
			count++;
		}
	}

	public void sendNotes(){
		/**
		Object[] args = new Object[3];
		try {
			for( Integer note : getNotes() ){
				args[0] = note;
				args[1] = new float[]{(float)0.25};
				args[2] = 127;
				OSCMessage msg = new OSCMessage( "/add" );
				msg.addArgument(args);
				port.send( msg );
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		 */	
	}
	
	public void sendNotes( int[] notes ){
		/**
		Object[] args = new Object[3];
		try {
			for( Integer note : notes ){
				args[0] = note;
				args[1] = new float[]{(float)0.25};
				args[2] = 127;
				OSCMessage msg = new OSCMessage( "/add" );
				msg.addArgument(args);
				port.send( msg );
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/	
	}
	public Iterable<Integer> getNotes(){
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for( Cell cell : theGame ){
			if( cell.alive() ){
				temp.add( cell.value() );
			}
		}
		return temp;
	}
	
	public void printNotes(){
		int count = 0;
		for( Integer note : getNotes() ){
			System.out.print( note + "    " );
			count++;
			if( count % 20 == 0 ){
				System.out.println();
			}
		}
	}
	
	public int[] generateMelody( int length, int groupSize ){
		if( allDead() ){
			return null;
		}
		int[] tune = new int[length];
		GroupedMelody<Integer> melody = new GroupedMelody<Integer>( getNotes() );
		int count = 0;
		for( Integer note : melody.generate( length, groupSize ) ){
			tune[count++] = note;
			System.out.println( note );
		}
		return tune;
	}
	
	protected boolean allDead() {
		for( Cell cell : theGame ){
			if( cell.alive() ){
				return false;
			}
		}
		return true;
	}

	public void placeGlider(int i, int j){
		theGame.set( i, j, true );
		theGame.set( i+1, j, true );
		theGame.set( i+2, j, true );
		theGame.set( i+2, j+1, true );
		theGame.set( i+1, j+2, true );
	}
	
	public void placeSpaceship(int i, int j){
		theGame.set( i, j, true );
		theGame.set( i-2, j, true );
		theGame.set( i+2, j, true );
		theGame.set( i-2, j+1, true );
		theGame.set( i+2, j+1, true );
		theGame.set( i-2, j+2, true );
		theGame.set( i+2, j+2, true );
		theGame.set( i-2, j+3, true );
		theGame.set( i+2, j+3, true );
		theGame.set( i-1, j+3, true );
		theGame.set( i+1, j+3, true );
		theGame.set( i-1, j+4, true );
		theGame.set( i+1, j+4, true );
		theGame.set( i, j+4, true );
		theGame.set( i, j+5, true );
	}
	
	public void placeSpaceship2(int i, int j){
		theGame.set( i, j, true );
		theGame.set( i+2, j, true );
		theGame.set( i, j+3, true );
		theGame.set( i+1, j+4, true );
		theGame.set( i+2, j+4, true );
		theGame.set( i+3, j+4, true );
		theGame.set( i+3, j+3, true );
		theGame.set( i+3, j+2, true );
		theGame.set( i+3, j+1, true );
	}
	
	public void sendGrain(int numSample, double duration, double position){
		/**	
		Object[] args = new Object[3];
		args[0] = numSample;
		args[1] = duration;
		args[2] = position;
		OSCMessage msg = new OSCMessage( "/play_grain" );
		msg.addArgument(args);
		try {
			port.send( msg );
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/	
	}
	
	public void setAddress(String address, int host){
		/**	
		try {
			port = new OSCPortOut(InetAddress.getByName(address), host);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		*/	
	}
	
	public void load( boolean[][] states, int startX, int startY ){
		
	}
	
	public boolean[][] load( String path ){
		byte[] bits; int width;
		//int height = ( bits.length * 8 ) / width;
		return null;
	}
	
	public void save( int startX, int startY, int width, int height, String path ){
		
	}
	
	public static void main(String[] args) {
		LifeMessages g = new LifeMessages();
		g.start();
	}
}