
public class Texture{
	protected int id;
	protected float width;
	protected float height;
	
	public Texture(int id, float width, float height){
		this.id = id;
		this.width = width;
		this.height = height;
	}
	
	public int getID(){
		return id;
	}
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
}