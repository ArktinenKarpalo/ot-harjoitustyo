package otm.harjoitustyo.graphics;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

public class Renderer {

	private HashSet<Drawable> drawables;

	private static Renderer renderer = new Renderer();

	public static Renderer getInstance() {
		return renderer;
	}

	private Renderer() {
		drawables = new HashSet<>();
	}

	public void addDrawable(Drawable... drawable) {
		for(Drawable d : drawable) {
			drawables.add(d);
		}
	}

	// Only deletes drawable from the render list, drawable must be deleted separately to free GPU resources
	public void deleteDrawable(Drawable... drawable) {
		for(Drawable d : drawable) {
			drawables.remove(d);
		}
	}

	public void drawAll() {
		Drawable[] drawList = new Drawable[drawables.size()];
		Iterator<Drawable> it = drawables.iterator();
		for(int i = 0; i < drawables.size(); i++) {
			drawList[i] = it.next();
		}

		Arrays.sort(drawList, Comparator.comparingInt((drawable) -> drawable.getZ()));

		for(int i = 0; i < drawList.length; i++) {
			drawList[i].draw();
		}
	}
}
