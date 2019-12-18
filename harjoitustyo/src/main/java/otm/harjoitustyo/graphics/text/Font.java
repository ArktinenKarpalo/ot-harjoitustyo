package otm.harjoitustyo.graphics.text;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL12;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import otm.harjoitustyo.Resources;
import otm.harjoitustyo.graphics.ShaderManager;
import otm.harjoitustyo.graphics.Sprite;
import otm.harjoitustyo.graphics.Texture;
import otm.harjoitustyo.graphics.TextureManager;

public class Font {

	private String path;
	private STBTTFontinfo fontInfo;
	private ByteBuffer fontBuffer;

	public Font(String path) {
		this.path = path;
		loadFont();
	}

	public Character loadCharacter(String character, int size, int z) {
		int codepoint = character.codePointAt(0);
		float scale = STBTruetype.stbtt_ScaleForPixelHeight(fontInfo, size);
		try(MemoryStack stack = MemoryStack.stackPush()) {

			IntBuffer advanceWidth = stack.mallocInt(1);
			IntBuffer leftSideBearing = stack.mallocInt(1);
			STBTruetype.stbtt_GetCodepointHMetrics(fontInfo, codepoint, advanceWidth, leftSideBearing);

			IntBuffer width = stack.mallocInt(1);
			IntBuffer height = stack.mallocInt(1);
			IntBuffer xoff = stack.mallocInt(1);
			IntBuffer yoff = stack.mallocInt(1);

			ByteBuffer texBuf = STBTruetype.stbtt_GetCodepointBitmap(fontInfo, scale, scale, codepoint, width, height, xoff, yoff);

			if(texBuf != null) {
				if(TextureManager.getInstance().getTexture(path + character + size) == null) {
					TextureManager.getInstance().setTexture(path + character + size, new Texture(texBuf, height.get(0), width.get(0), GL12.GL_RED));
				}
				STBTruetype.stbtt_FreeBitmap(texBuf);

				Sprite sprite = new Sprite(TextureManager.getInstance().getTexture(path + character + size), ShaderManager.textShader);
				sprite.setSize(Math.max(height.get(0), width.get(0)));
				return new Character(sprite, size, width.get(0), height.get(0), xoff.get(0), yoff.get(0), Math.round(advanceWidth.get(0) * scale), z);
			} else {
				return new Character(null, size, width.get(0), height.get(0), xoff.get(0), yoff.get(0), Math.round(advanceWidth.get(0) * scale), z);
			}
		}
	}

	public void delete() {
		MemoryUtil.memFree(fontBuffer);
	}

	private void loadFont() {
		fontInfo = STBTTFontinfo.create();
		fontBuffer = Resources.loadResourceAsByteBuffer(path); // Must be freed somewhere
		STBTruetype.stbtt_InitFont(fontInfo, fontBuffer);
	}
}
