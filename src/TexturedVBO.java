import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class TexturedVBO {
	private int vboTextureID, vboVertexID;
	private Texture tex;
	private float rotation;
	private boolean visible;
	private float pivotX, pivotY;
	
	public TexturedVBO( Texture tex, int vertID, int texID ){
		vboTextureID = texID;
		vboVertexID = vertID;
		this.tex = tex;
		rotation = 0;
		visible = true;
		pivotX = 0.5f;
		pivotY = 0.5f;
	}

	public void render(float x, float y, float width, float height){
		if ( visible ){
			glPushMatrix();
			{
				glTranslatef( x + (pivotX * width), y + (pivotY * height), 0);
				glRotatef( rotation, 0, 0, 1 );
				glTranslatef( -x - (pivotX * width), -y - (pivotY * height), 0);
				glTranslatef( x, y, 0 );
				glBindTexture(GL_TEXTURE_2D, tex.getID());
			
				glBindBuffer( GL_ARRAY_BUFFER, vboTextureID);
				glTexCoordPointer( 2, GL_FLOAT, 0, 0 );
			
				glBindBuffer( GL_ARRAY_BUFFER, vboVertexID );
				glVertexPointer( 2, GL_FLOAT, 0, 0);
			
				glEnableClientState( GL_VERTEX_ARRAY );
				glEnableClientState( GL_TEXTURE_COORD_ARRAY );

				glDrawArrays( GL_QUADS, 0, 4 );
			
				glEnableClientState( GL_VERTEX_ARRAY);
				glEnableClientState( GL_TEXTURE_COORD_ARRAY);
			}
			glPopMatrix();
		}
	}

	public void dispose(){
		glDeleteBuffers(vboTextureID);
		glDeleteBuffers(vboVertexID);
	}

	public Texture getTexture(){
		return tex;
	}

	public void visible( boolean visible ){
		this.visible = visible;
	}

	public boolean isVisible(){
		return visible;
	}

	public float rotation(){
		return rotation;
	}

	public void setRotation( float rotation ){
		this.rotation = rotation;
	}
}