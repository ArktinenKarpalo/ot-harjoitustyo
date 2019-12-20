package otm.harjoitustyo.graphics;

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
			texture.useTexture();
			currentTextureId = texture.getTextureId();
		}
	}

	/**
	 * Even after set texture, getTexture should be used to keep texture references correct
	 * @param name
	 * @param texture
	 */
	public void setTexture(String name, Texture texture) {
		Texture previous = loadedTextures.put(name, texture);
		if(previous != null) {
			previous.deleteTexture();
		}
	}

	/**
	 * @param name Name of the texture
	 * @return Null if not found
	 */
	public Texture getTexture(String name) {
		Texture texture = loadedTextures.get(name);
		if(texture != null) {
			texture.references++;
		}
		return texture;
	}

	/**
	 * Attempts to load the texture from disk if it is not found.
	 * @param name
	 * @return
	 */
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
