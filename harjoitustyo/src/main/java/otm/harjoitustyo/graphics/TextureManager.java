package otm.harjoitustyo.graphics;

import java.util.HashMap;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class TextureManager {

	private static TextureManager textureManager = new TextureManager();
	private HashMap<String, Texture> loadedTextures;
	private int currentTextureId;

	private TextureManager() {
		loadedTextures = new HashMap<>();
	}

	public static TextureManager getInstance() {
		return textureManager;
	}

	public void useTexture(Texture texture) {
		if(currentTextureId != texture.getTextureId()) {
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, texture.getTextureId());
			currentTextureId = texture.getTextureId();
		}
	}

	public Texture getTexture(String name) {
		Texture texture = loadedTextures.get(name);
		if(texture == null) {
			texture = new Texture(name);
			loadedTextures.put(name, texture);
		}
		return texture;
	}

	// Unloads the texture from memory
	public void deleteTexture(String name) {
		Texture texture = loadedTextures.remove(name);
		if(texture != null) {
			texture.deleteTexture();
		}
	}
}
