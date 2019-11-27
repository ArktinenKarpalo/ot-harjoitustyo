package otm.harjoitustyo.graphics;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.util.HashMap;

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

	// Even after set texture, getTexture should be used to keep texture references correct
	public void setTexture(String name, Texture texture) {
		Texture previous = loadedTextures.put(name, texture);
		if(previous != null)
			previous.deleteTexture();
	}

	// If texture is not found, returns null
	public Texture getTexture(String name) {
		Texture texture = loadedTextures.get(name);
		if(texture != null)
			texture.references++;
		return texture;
	}

	// If texture is not found, attemps to load it from the disk
	public Texture getFileTexture(String name) {
		Texture texture = loadedTextures.get(name);
		if(texture == null) {
			texture = new Texture(name);
			loadedTextures.put(name, texture);
		}
		texture.references++;
		return texture;
	}

	public void deleteUnusedTextures() {
		loadedTextures.entrySet().removeIf((entry) -> {
			if(entry.getValue().references == 0) {
				entry.getValue().deleteTexture();
				return true;
			} else {
				return false;
			}
		});
	}
}
