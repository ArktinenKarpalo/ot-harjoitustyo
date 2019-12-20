package otm.harjoitustyo.level;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.glfw.GLFW;
import otm.harjoitustyo.Resources;
import otm.harjoitustyo.graphics.Drawable;
import otm.harjoitustyo.graphics.Renderer;
import otm.harjoitustyo.graphics.text.Text;

public class LevelSelectScreen implements Scene {

	private boolean hasEnded;
	private int levelListOffset;

	private List<Text> tableTexts;
	private List<Drawable> displayDrawables;
	private LevelItem[] levels;
	private Scene nextScene;

	private class LevelItem {
		public String name, filename;
		public LevelItem(String name, String filename) {
			this.filename = filename;
			this.name = name;
		}
	}

	public LevelSelectScreen() {
		displayDrawables = new ArrayList<>();
		init();
	}

	private void init() {
		String[] levelList = Resources.loadResourceAsString("levels/levels").split("\n");
		levels = new LevelItem[levelList.length];
		for(int i=0; i<levelList.length; i++) {
			String[] split = levelList[i].split(";");
			levels[i] = new LevelItem(split[0], split[1]);
		}
		Text titleText = new Text("Select level", "OpenSans-Regular.ttf", 72, 1, 1001);
		titleText.setPosition(1280 / 2 - 200, 100);
		Text instructionText = new Text("Arrow keys to navigate, enter to select", "OpenSans-Regular.ttf", 24, 1, 1001);
		instructionText.setPosition(1280 / 2 - 600, 200);
		Renderer.getInstance().addDrawable(titleText, instructionText);
		displayDrawables.add(titleText);
		displayDrawables.add(instructionText);
		displayLevels();
	}

	private void displayClose() {
		for(Drawable d:displayDrawables) {
			Renderer.getInstance().deleteDrawable(d);
			d.delete();
		}
		for(Text t:tableTexts) {
			Renderer.getInstance().deleteDrawable(t);
			t.delete();
		}
	}

	private void displayLevels() {
		if(tableTexts != null) {
			tableTexts.forEach((text) -> {
				Renderer.getInstance().deleteDrawable(text);
				text.delete();
			});
		}
		tableTexts = new ArrayList<>();
		int selectedIndex = levelListOffset;
		levelListOffset = Math.max(Math.min(levelListOffset, levels.length - 5), 0);
		for(int i = levelListOffset; i < Math.min(levels.length, levelListOffset + 6); i++) {
			Text levelName = new Text(levels[i].name, "OpenSans-Regular.ttf", 48, 1, 1001);
			levelName.setPosition(150, 300 + 100 * (i - levelListOffset));

			if(i == selectedIndex)
				levelName.setColor(200, 200, 0, 255);

			Renderer.getInstance().addDrawable(levelName);
			tableTexts.add(levelName);
		}
		levelListOffset = selectedIndex;
	}

	public Scene nextScene() {
		if(hasEnded) {
			return nextScene;
		} else {
			return this;
		}
	}

	public void handleKeyInput(long window, int key, int scancode, int action, int mods) {
		if(key == GLFW.GLFW_KEY_UP && (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT)) {
			levelListOffset--;
			if(levelListOffset < 0)
				levelListOffset = levels.length-1;
			displayLevels();
		} else if(key == GLFW.GLFW_KEY_DOWN && (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT)) {
			levelListOffset++;
			if(levelListOffset >= levels.length)
				levelListOffset = 0;
			displayLevels();
		} else if(key == GLFW.GLFW_KEY_ENTER && action == GLFW.GLFW_PRESS) {
			displayClose();
			hasEnded = true;
			LevelManager lm = new LevelManager(LevelLoader.loadLevel("levels/" + levels[levelListOffset].filename));
			lm.loadLevel();
			nextScene = lm;
		}
	}

	public void handleCharInput(long window, int codepoint) {
	}

	public void loop() {
	}
}