package otm.harjoitustyo.graphics.text;

import otm.harjoitustyo.graphics.Drawable;
import otm.harjoitustyo.graphics.Sprite;

public class Character implements Drawable {

	private Sprite sprite;
	private int advanceWidth, size, z, width, height, xoff, yoff;

	public Character(Sprite sprite, int size, int width, int height, int xoff, int yoff, int advanceWidth, int z) {
		this.width = width;
		this.height = height;
		this.size = size;
		this.xoff = xoff;
		this.yoff = yoff;
		this.advanceWidth = advanceWidth;
		this.z = z;
		this.sprite = sprite;
	}

	public int getAdvanceWidth() {
		return advanceWidth;
	}

	public int getXoff() {
		return xoff;
	}

	public int getYoff() {
		return yoff;
	}

	public void setPosition(int x, int y) {
		if(sprite != null) {
			sprite.setPosition(x, y);
		}
	}

	public void move(int x, int y) {
		if(sprite != null) {
			sprite.move(x, y);
		}
	}

	public void setColor(int r, int g, int b, int a) {
		if(sprite != null) {
			sprite.setColor(r, g, b, a);
		}
	}

	@Override
	public void delete() {
		if(sprite != null) {
			sprite.delete();
		}
	}

	@Override
	public int getZ() {
		return z;
	}

	@Override
	public void draw() {
		if(sprite != null) {
			sprite.draw();
		}
	}
}
