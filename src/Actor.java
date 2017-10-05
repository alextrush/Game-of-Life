
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;

public class Actor extends GameObject {
	protected ArrayList<Float> targets;
	protected Float target;
	protected float step;
	public Actor(TexturedVBO vbo) {
		super(vbo);
		init();
	}

	public Actor(TexturedVBO vbo, float x, float y) {
		super(vbo, x, y);
		init();
	}

	public Actor(String name, float width, float height, float x, float y) {
		super(name, width, height, x, y);
		init();
	}
	
	protected void init() {
		target = null;
		step = 6;
	}

	public void addTarget( Float point ){
		targets.add( point );
	}
	
	public void clearTargets(){
		targets = new ArrayList<Float>();
		target = null;
	}

	public void set( float x, float y ){
		super.set(x, y);
		clearTargets();
	}
	
	public void addTargets(ArrayList<Float> targets) {
		for( Float point : targets ){
			this.targets.add(point);
			System.out.println(point);
		}
		target = targets.get(0);
	}
	
	public void update(){
		if( target != null ){
			if( x == target.x && y == target.y ){
				target = (targets.isEmpty()) ? null : targets.get(0);
				if(!targets.isEmpty()){
					targets.remove(0);
				}
			} else {
				//move towards the target
				double dx = target.x-x, dy = target.y-y;
				if( dx == 0 ){
					y +=  (dy>0) ? step : -step;
				} else if ( dy == 0 ){
					x +=  (dx>0) ? step : -step;
				} else {
					double slope = dy/dx, intercept = y-slope*x;
					if( Math.abs(dx) > Math.abs(dy) ){
						x += (dx>0) ? step : -step;
						y = (float)(slope*x + intercept);
					} else {
						y += (dy>0) ? step : -step;					
						x = (float)((y - intercept)/slope);
					}
				}
				if( Math.abs(target.x-x) < step ){
					x = target.x;
				}
				if( Math.abs(target.y-y) < step ){
					y = target.y;
				}
			}
		}
	}
}
