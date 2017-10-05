
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;


public class TextureUtil {
	
	private static ByteBuffer buildBuffer( BufferedImage img ){
		int[] pixels = new int[img.getWidth() * img.getHeight()];
		img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth() );
		ByteBuffer buff = BufferUtils.createByteBuffer(img.getWidth() * img.getHeight() * 4 ); 
		for(int y = 0; y < img.getHeight(); y++){
			for(int x = 0; x < img.getWidth(); x++){
				int pixel = pixels[y*img.getWidth()+x];
				//red component
				buff.put((byte) ((pixel >> 16) & 0xFF));
				//green component
				buff.put((byte) ((pixel >> 8) & 0xFF));
				//blue component
				buff.put((byte) (pixel & 0xFF));
				//alpha component
				buff.put((byte) ((pixel >> 24) & 0xFF));
			}
		}
		buff.flip();
		return buff;		
	}
	
	//creates a texture for use with lwjgl
	public static Texture load(String name, boolean tiled){
		BufferedImage img = null;
		// name = ".." + File.separator + "img" + File.separator + name;
		try{
			img = ImageIO.read(Texture.class.getClassLoader().getResourceAsStream(name));
		} catch ( IOException e){
			e.printStackTrace();
			System.err.println( "Unable to load texture: " + name );
		} catch ( IllegalArgumentException e ){
			System.err.println( "What did you just send me? " + name );
			e.printStackTrace();
		}
		ByteBuffer buff = buildBuffer(img);
		
		int textureID = GL11.glGenTextures();
		
		GL11.glBindTexture( GL11.GL_TEXTURE_2D, textureID );
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		if ( tiled ){
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		}

		GL11.glTexImage2D( GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, img.getWidth(), img.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buff );
	
		return new Texture( textureID, img.getWidth(), img.getHeight() );
	}

	//creates a texture for use with lwjgl
	public static Texture load(String name, float width, float height, boolean tiled){
		BufferedImage img = null;
		try{
			img = ImageIO.read(Texture.class.getClassLoader().getResourceAsStream(name));
		} catch ( IOException e){
			e.printStackTrace();
			System.err.println( "Unable to load texture: " + name );
		} catch ( IllegalArgumentException e ){
			System.err.println( "What did you just send me?" );
			e.printStackTrace();
		}

		ByteBuffer buff = buildBuffer(img);
		
		int textureID = GL11.glGenTextures();
		
		GL11.glBindTexture( GL11.GL_TEXTURE_2D, textureID );
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		if( tiled ){
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		}
		
		GL11.glTexImage2D( GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, img.getWidth(), img.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buff );
	
		return (tiled) ? new Texture(textureID, img.getWidth(), img.getHeight()) : new Texture( textureID, width, height );
	}

	private static FloatBuffer buildSquare( float width, float height ){
		FloatBuffer vertices = BufferUtils.createFloatBuffer(8);
		vertices.put(new float[]{
			0, 0,
			0, height,
			width, height,
			width, 0
		});
		return vertices;
	}
	
	private static TexturedVBO bind( Texture texture, FloatBuffer vertices, FloatBuffer texCoords ){
		int vertID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		int textID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, texCoords, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		//return a textured vbo using the buffers created
		return new TexturedVBO( texture, vertID, textID);
	}

	public static TexturedVBO loadVBO( Texture texture ){
		if ( texture == null ){
			return null;
		}
		//create float buffer to set the dimensions of the texture
		FloatBuffer vertices = buildSquare( texture.getWidth(), texture.getHeight() );
		//create float buffer to store the texture mapping
		FloatBuffer texCoords = buildSquare( 1, 1 );
		//reset the buffer to the start point
		vertices.rewind();
		texCoords.rewind();
		//create the vertex buffer objects and bind them to an id
		return bind( texture, vertices, texCoords );
	}

	public static TexturedVBO loadVBO( String name ){
		Texture texture = load( name, false );
		if ( texture == null ){
			return null;
		}
		//create float buffer to set the dimensions of the texture
		FloatBuffer vertices = buildSquare( texture.getWidth(), texture.getHeight() );
		//create float buffer to store the texture mapping
		FloatBuffer texCoords = buildSquare( 1, 1 );
		//reset the buffer to the start point
		vertices.rewind();
		texCoords.rewind();
		//create the vertex buffer objects and bind them to an id
		return bind( texture, vertices, texCoords );
	}
	
	public static TexturedVBO loadVBO( Texture texture, float width, float height ){
		if ( texture == null ){
			return null;
		}
		//create float buffer to set the dimensions of the texture
		FloatBuffer vertices = buildSquare( width, height );
		//create float buffer to store the texture mapping
		FloatBuffer texCoords = buildSquare( 1, 1 );
		//reset the buffer to the start point
		vertices.rewind();
		texCoords.rewind();
		//create the vertex buffer objects and bind them to an id
		return bind( texture, vertices, texCoords );
	}

	public static TexturedVBO loadVBO( String name, float width, float height ){
		Texture texture = load( name, false );
		if ( texture == null ){
			return null;
		}
		//create float buffer to set the dimensions of the texture
		FloatBuffer vertices = buildSquare( width, height );
		//create float buffer to store the texture mapping
		FloatBuffer texCoords = buildSquare( 1, 1 );
		//reset the buffer to the start point
		vertices.rewind();
		texCoords.rewind();
		//create the vertex buffer objects and bind them to an id
		return bind( texture, vertices, texCoords );
	}

	public static TexturedVBO loadTiledVBO( Texture texture, float width, float height ){
		//create float buffer to set the dimensions of the texture
		FloatBuffer vertices = buildSquare( width, height );
		//create float buffer to store the texture mapping
		FloatBuffer texCoords = buildSquare( width / texture.getWidth(), height / texture.getHeight() );
		//reset the buffer to the start point
		vertices.rewind();
		texCoords.rewind();
		//create the vertex buffer objects and bind them to an id
		return bind( texture, vertices, texCoords );
	}
	
	public static TexturedVBO loadTiledVBO( String name, float width, float height ){
		Texture texture = load( name, width, height, true );
		
		//create float buffer to set the dimensions of the texture
		FloatBuffer vertices = buildSquare( width, height );
		//create float buffer to store the texture mapping
		FloatBuffer texCoords = buildSquare( width / texture.getWidth(), height / texture.getHeight() );
		//reset the buffer to the start point
		vertices.rewind();
		texCoords.rewind();
		//create the vertex buffer objects and bind them to an id
		return bind( texture, vertices, texCoords );
	}

	public static TexturedVBO loadTiledVBO( Texture texture, float width, float height, float tileX, float tileY ){
		//create float buffer to set the dimensions of the texture
		FloatBuffer vertices = buildSquare( width * tileX, height * tileX );
		//create float buffer to store the texture mapping
		FloatBuffer texCoords = buildSquare( tileX, tileY );
		//reset the buffer to the start point
		vertices.rewind();
		texCoords.rewind();
		//create the vertex buffer objects and bind them to an id
		return bind( texture, vertices, texCoords );
	}
	
	public static TexturedVBO loadTiledVBO( String name, float width, float height, float tileX, float tileY ){
		Texture texture = load( name, width, height, true );
		
		//create float buffer to set the dimensions of the texture
		FloatBuffer vertices = buildSquare( width * tileX, height * tileY );
		//create float buffer to store the texture mapping
		FloatBuffer texCoords = buildSquare( tileX, tileY );
		//reset the buffer to the start point
		vertices.rewind();
		texCoords.rewind();
		//create the vertex buffer objects and bind them to an id
		return bind( texture, vertices, texCoords );
	}
}