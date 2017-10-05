

import org.lwjgl.opengl.GL11;

public class Line {
	protected float x1,y1,x2,y2;
	protected int count;
	public Line( float x1, float y1, float x2, float y2 ){
		this.x1 = x1;
		this.y2 = y2;
		this.x1 = x1;
		this.y2 = y2;
		count = 0;
	}
	
	public float x1(){
		return x1;
	}
	public float x2(){
		return x2;
	}
	public float y1(){
		return y1;
	}
	public float y2(){
		return y2;
	}
	
	public void render(){
		GL11.glBegin( GL11.GL_LINES ); {
			GL11.glPointSize(5);
			GL11.glVertex3f(x1, y1,count);
			GL11.glVertex3f(x2, y2,count);
		}GL11.glEnd();
		count=(count+1)%20;
	}
}
