package otm.harjoitustyo.level;

public interface Scene {

	void loop();

	void handleKeyInput(long window, int key, int scancode, int action, int mods);

	void handleCharInput(long window, int codepoint);

	Scene nextScene();
}
