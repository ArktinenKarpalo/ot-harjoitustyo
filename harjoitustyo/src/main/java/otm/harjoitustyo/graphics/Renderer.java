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

	/**
	 * Add a drawable to the list of drawables that are drawn during each call to Renderer
	 *
	 * @param drawable Drawable to add
	 */
	public void addDrawable(Drawable... drawable) {
		for(Drawable d : drawable) {
			drawables.add(d);
		}
	}

	/**
	 * Only deletes the drawable from the render list, drawable must be deleted separately to free memory-resources.
	 *
	 * @param drawable Drawable to delete
	 */
	public void deleteDrawable(Drawable... drawable) {
		for(Drawable d : drawable) {
			drawables.remove(d);
		}
	}

	/**
	 * Draw all drawables in the drawable list in order of z-level
	 */
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
