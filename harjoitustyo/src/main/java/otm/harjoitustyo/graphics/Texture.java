package otm.harjoitustyo.graphics;

import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memFree;


import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;
import otm.harjoitustyo.Resources;

public class Texture {

	// OpenGL texture id
	public int references = 0; // How many objects are using this texture, when references reaches 0, the texture may be deleted
	private int textureId, width, height;
	private String name;

	public Texture(String path) {
		name = path;
		loadTexture(path);
	}

	// pixelFormat = OpenGL color pixelformat ie. BGR
	public Texture(ByteBuffer texBuf, int height, int width, int pixelFormat) {
		this.width = width;
		this.height = height;
		loadTexture(texBuf, pixelFormat);
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
		ByteBuffer imgBuf = Resources.loadResourceAsByteBuffer(path);
		ByteBuffer texBuf = STBImage.stbi_load_from_memory(imgBuf, w, h, comp, STBImage.STBI_rgb_alpha);
		MemoryUtil.memFree(imgBuf);

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
	private void loadTexture(ByteBuffer texBuf, int pixelFormat) {
		int id = glGenTextures();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, id);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, pixelFormat, GL_UNSIGNED_BYTE, texBuf);
		glGenerateMipmap(GL_TEXTURE_2D);

		textureId = id;
	}
}
