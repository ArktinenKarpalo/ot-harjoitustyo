package otm.harjoitustyo.level;

public interface Scene {

	/**
	 * Called once in each game loop when the scene is active
	 */
	void loop();

	/**
	 * Called once in each game loop to process keyInputs with parameters from GLFW keyCallback
	 */
	void handleKeyInput(long window, int key, int scancode, int action, int mods);

	/**
	 * Called once in each game loop to process charInputs with parameters from GLFW charCallback
	 */
	void handleCharInput(long window, int codepoint);

	/**
	 * If the scene is active, game switches the scene to scene returned by this function in the end of the gameloop. Ie. should return the scene itself, if the scene doesn't change.
	 * @return Scene to switch to
	 */
	Scene nextScene();
}
