

import java.awt.geom.Point2D.Float;

public class GameObject {
	protected TexturedVBO vbo;
	protected float x, y;
	protected float dx, dy;
	protected float width, height;
	protected float rotation;
	protected float mass;
	protected boolean hitFlag, scroll;
	protected GameObject target;

	public GameObject( TexturedVBO vbo ){
		this.vbo = vbo;
		create( 0, 0, 10, 10 );
	}
	
	public GameObject( TexturedVBO vbo, float x, float y ){
		this.vbo = vbo;
		create( x, y, 10, 10 );
	}
	
	public GameObject( String name, float width, float height, float x, float y ){
		vbo = TextureUtil.loadVBO(name, width, height);
		create( x, y, width, height );
	}
	
	private void create( float x, float y, float width, float height ){
		this.x = x;
		this.y = y;
		dx = 0;    dy = 0;
		this.width = width;
		this.height = height;
		scroll = true; hitFlag = false;
	}
	
	public void render(){
		synchronized( this.vbo ){
			vbo.render(x, y, width, height);
		}
	}
	
	public void render( float x, float y ){
		synchronized( this.vbo ){
			vbo.render(x, y, width, height);
		}
	}
	
	public void render( float x, float y, float width, float height ){
		synchronized( this.vbo ){
			vbo.render(x, y, width, height);
		}
	}
	
	public void update(){
		synchronized( this ){
			x = x + dx;
			y = y + dy;		
		}
	}
	
	public void update( float xAccel, float yAccel ){
		synchronized( this ){
			float u = dx; float v = dy;
			dx = dx + xAccel;
			dy = dy + yAccel;
			x = x + (u+dx)/2;
			y = y + (v+dy)/2;		
		}
	}
	
	public void dispose(){
		vbo.dispose();
	}
	
	public float x(){
		return x;
	}
	
	public float y(){
		return y;
	}
	
	public Float point(){
		return new Float( x, y );
	}
	
	public float width(){
		return width;
	}
	
	public float height(){
		return height;
	}
	
	public void set( float x, float y ){
		this.x = x;
		this.y = y;
	}

	public float dx(){
		return dx;
	}
	
	public float dy(){
		return dy;
	}

	public void bounceX( float k ){
		dx *= -k;
		dy *= k;
	}
	
	public void bounceY( float k ){
		dx *= k;
		dy *= -k;
	}
	
	public void setDx( float dx ){
		this.dx = dx;
	}
	
	public void setDy( float dy ){
		this.dy = dy;
	}
	
	public void setHit( boolean hit ){
		hitFlag = hit;
	}
	
	public boolean hit(){
		return hitFlag;
	}
	
	public float mass(){
		return mass;
	}

	public boolean scroll(){
		return scroll;
	}
	
	public void setScroll( boolean scroll ){
		this.scroll = scroll;
	}

	public boolean collides(GameObject c) {
		if( hitFlag ){
			return false;
		}
		return false;
	}

	public void collide(GameObject c) {

	}

	public boolean isVisible(){
		return vbo.isVisible();
	}
	
	public void setVisible( boolean visible ){
		vbo.visible( visible );
	}
	
	public void check() {
		if( !collides( target ) ){
			hitFlag = false;
		}
	}
}