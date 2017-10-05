public class GroundForce extends LifeForce {
	protected TexturedVBO[] states;
	protected TexturedVBO[] statesFlipped;
	protected boolean flipped = false;
	public GroundForce(TexturedVBO vbo) {
		super(vbo);
	}

	public GroundForce(TexturedVBO vbo, float x, float y) {
		super(vbo, x, y);
	}

	public GroundForce(String name, float width, float height, float x, float y) {
		super(name, width, height, x, y);
	}

	public GroundForce(String[] names, float width, float height, float x, float y) {
		super(names[0], width, height, x, y);
		states = new TexturedVBO[names.length];
		statesFlipped = new TexturedVBO[names.length];
		for( int i = 0 ; i < names.length ; i++ ){
			states[i] = TextureUtil.loadVBO(names[i], width, height);
			statesFlipped[i] = TextureUtil.loadVBO(names[i], height, width);
		}
		vbo = states[0];
	}

	public void update(){
		if( states != null && life != null && life instanceof SpaceAwareCell ){
//			if( ((SpaceAwareCell)life).rotation() == 0 || ((SpaceAwareCell)life).rotation() == 180 ){
				flipped = false;
				vbo = states[((SpaceAwareCell)life).frame() % states.length];
				vbo.setRotation( ((SpaceAwareCell)life).rotation() );
/**			} else {
				flipped = true;
				vbo = statesFlipped[((SpaceAwareCell)life).frame() % states.length];
				vbo.setRotation( ((SpaceAwareCell)life).rotation() );
			}
*/		}
		super.update();
	}

	public void update(float xAccel, float yAccel){
		update();
		super.update(xAccel, yAccel);
	}

	public void render(){
		render( x, y, width, height );
	}
	
	public void render( float x, float y ){
		render( x, y, width, height );
	}
	
	public void render( float x, float y, float width, float height ){
		synchronized( this.vbo ){
			if( flipped ){
				vbo.render(x, y, height, width);
			} else {
				vbo.render(x, y, width, height);
			}
		}
	}
}