package otm.harjoitustyo.graphics.text;

import otm.harjoitustyo.graphics.Drawable;

public class Text implements Drawable {

	private String text, fontName;
	private int z, scale, x, y, fontSize;
	public Character[] characters;

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
		characters = new Character[text.length()];
		Font font = new Font(fontName);
		int xOffset = 0;
		int yOffset = 0;

		for(int i = 0; i < text.length(); i++) {
			characters[i] = font.loadCharacter(text.substring(i, i + 1), fontSize, z);
			characters[i].setPosition(xOffset + characters[i].getXoff(), yOffset + characters[i].getYoff());
			xOffset += characters[i].getAdvanceWidth();
		}
		font.delete();
	}

	public void setPosition(int x, int y) {
		for(int i = 0; i < characters.length; i++) {
			characters[i].move(x - this.x, y - this.y);
		}
		this.x = x;
		this.y = y;
	}

	public void setColor(int r, int g, int b, int a) {
		for(int i = 0; i < characters.length; i++) {
			characters[i].setColor(r, g, b, a);
		}
	}

	@Override
	public void delete() {
		for(int i = 0; i < characters.length; i++) {
			characters[i].delete();
		}
	}

	@Override
	public int getZ() {
		return z;
	}

	@Override
	public void draw() {
		for(int i = 0; i < characters.length; i++) {
			characters[i].draw();
		}
	}
}
