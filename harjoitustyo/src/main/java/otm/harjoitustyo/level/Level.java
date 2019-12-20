package otm.harjoitustyo.level;

import java.util.Arrays;

public class Level {

	public String name, backgroundPath, musicPath, backgroundType;
	public float scrollingSpeed; // How quickly level event indicators should move

	public LevelEvent[] levelEvents;

	public Level(String name, String backgroundType, String backgroundPath, String musicPath, float scrollingSpeed, LevelEvent[] levelEvents) {
		this.name = name;
		this.backgroundType = backgroundType;
		this.backgroundPath = backgroundPath;
		this.musicPath = musicPath;
		this.scrollingSpeed = scrollingSpeed;
		this.levelEvents = levelEvents;
		init();
	}

	public void init() {
		Arrays.sort(levelEvents);
	}
}
