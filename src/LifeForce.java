
public class LifeForce extends GameObject {
	protected Cell life;
	public LifeForce(TexturedVBO vbo) {
		super(vbo);
		createLife();
	}

	public LifeForce(TexturedVBO vbo, float x, float y) {
		super(vbo, x, y);
		createLife();
	}

	public LifeForce(String name, float width, float height, float x, float y) {
		super(name, width, height, x, y);
		createLife();
	}
	
	private void createLife() {
		life = new Cell();
	}

	public void getALife( Cell life ) {
		this.life = life;
	}
	
	
	public void setLowBirth( int birthLow ){
		if( life != null ){
			life.setLowBirth(birthLow);
		}
	}

	public void setHighBirth( int birthHigh ){
		if( life != null ){
			life.setHighBirth(birthHigh);
		}
	}
	
	public void overcrowd( int dieHigh ){
		if( life != null ){
			life.overcrowd(dieHigh);
		}
	}
	
	public void starve( int dieLow ){
		if( life != null ){
			life.starve(dieLow);
		}
	}
	
	public int value() {
		return life.value();
	}

	public void setValue(int value) {
		life.setValue( value );
	}

	public void update(){
		if( life != null ){
			setVisible( life.alive() );
		}
		super.update();
	}

	public void update( float xAccel, float yAccel ){
		if( life != null ){
			setVisible( life.alive() );
		}
		super.update( xAccel, yAccel );
	}
	public String toString(){
		return "[" + x + ", " + y + " : " + width + ", " + height + "] = " + (life.alive() ? "alive" : "dead");
	}
}