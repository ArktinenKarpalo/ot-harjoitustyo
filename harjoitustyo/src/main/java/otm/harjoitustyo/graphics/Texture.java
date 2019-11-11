package otm.harjoitustyo.graphics;

import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_BGR;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memFree;

public class Texture {

	// OpenGL texture id
	private int textureId, width, height;
	private String name;

	public Texture(String path) {
		name = path;
		loadTexture(path);
	}

	// texbuf color format BGR
	public Texture(ByteBuffer texBuf, int height, int width, String name) {
		this.name = name;
		this.width = width;
		this.height = height;
		loadTexture(texBuf);
	}

	// Unloads the texture from GPU memory
	public void deleteTexture() {
		glDeleteTextures(textureId);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getTextureId() {
		return textureId;
	}

	public String getName() {
		return name;
	}

	// Loads the texture from the file to OpenGL
	private void loadTexture(String path) {
		IntBuffer w = memAllocInt(1);
		IntBuffer h = memAllocInt(1);
		IntBuffer comp = memAllocInt(1);

		STBImage.stbi_set_flip_vertically_on_load(false);
		ByteBuffer texBuf = STBImage.stbi_load(this.getClass().getResource("/"+path).getPath(), w, h, comp, STBImage.STBI_rgb_alpha);
		width = w.get();
		height = h.get();

		int id = glGenTextures();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, id);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, texBuf);
		glGenerateMipmap(GL_TEXTURE_2D);

		STBImage.stbi_image_free(texBuf);

		memFree(w);
		memFree(h);
		memFree(comp);

		textureId = id;
	}

	// texbuf color format BGR
	private void loadTexture(ByteBuffer texBuf) {
		int id = glGenTextures();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, id);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_BGR, GL_UNSIGNED_BYTE, texBuf);
		glGenerateMipmap(GL_TEXTURE_2D);

		textureId = id;
	}
}
