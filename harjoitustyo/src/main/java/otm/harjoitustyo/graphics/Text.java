package otm.harjoitustyo.graphics;

import com.mlomb.freetypejni.Bitmap;
import com.mlomb.freetypejni.Face;
import com.mlomb.freetypejni.FreeTypeConstants;
import com.mlomb.freetypejni.GlyphSlot;
import org.lwjgl.opengl.GL12;

public class Text implements Drawable {

	private String text, fontName;
	private int z, scale, x, y, fontSize;
	public Sprite[] characters;

	public Text(String text, String fontName, int fontSize, int scale, int z) {
		this.x = 0;
		this.y = 0;
		this.z = z;
		this.text = text;
		this.fontSize = fontSize;
		this.scale = scale;
		this.fontName = fontName;
		init();
	}


	private void init() {
		characters = new Sprite[text.length()];
		Face face = FreeType.getInstance().getFont(fontName);
		face.setPixelSizes(fontSize, fontSize);
		int xOffset = 0;
		int yOffset = 0;
		for(int i=0; i<text.length(); i++) {
			face.loadChar(text.charAt(i), FreeTypeConstants.FT_LOAD_RENDER);
			GlyphSlot gs = face.getGlyphSlot();
			Bitmap bm = gs.getBitmap();

			Texture charTex = new Texture(bm.getBuffer(), bm.getRows(), bm.getWidth(), GL12.GL_RED);
			characters[i] = new Sprite(charTex, ShaderManager.textShader);
			characters[i].setScale(bm.getWidth()*scale, bm.getRows()*scale);
			characters[i].setPosition(xOffset+gs.getBitmapLeft(), yOffset-(gs.getBitmapTop()));

			xOffset += gs.getAdvance().getX()/fontSize;
			yOffset += gs.getAdvance().getY()/fontSize;
		}
	}

	public void setPosition(int x, int y) {
		for(int i=0; i<characters.length; i++)
			characters[i].move(x-this.x, y-this.y);
		this.x = x;
		this.y = y;
	}

	public void setColor(int r, int g, int b, int a) {
		for(int i=0; i<characters.length; i++) {
			characters[i].setColor(r, g, b, a);
		}
	}

	@Override
	public void delete() {
		for(int i=0; i<characters.length; i++) {
			characters[i].getTexture().deleteTexture();
			characters[i].delete();
		}
	}

	@Override
	public int getZ() {
		return z;
	}

	@Override
	public void draw() {
		for(int i=0; i<characters.length; i++)
			characters[i].draw();
	}
}
