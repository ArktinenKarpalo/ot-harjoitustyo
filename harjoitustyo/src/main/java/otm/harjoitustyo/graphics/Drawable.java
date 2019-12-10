package otm.harjoitustyo.graphics;

public interface Drawable {

	/**
	 * Used to delete the drawable and all textures etc. related to it, doesn't delete the drawable from the Render-list
	 */
	void delete();

	/**
	 * Drawables are drawn so that smallest Z-level gets drawn in the back, largest in the front.
	 * @return Z-level of the drawable
	 */
	int getZ();

	/**
	 * Called each frame by Renderer to render the drawable
	 */
	void draw();

}
