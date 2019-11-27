package otm.harjoitustyo.level;

import java.util.Arrays;

public class Level {

	public String name, backgroundPath, musicPath, backgroundType;
	public float scrollingSpeed; // How quickly level event indicators should move

	public LevelEvent[] levelEvents;

	public Level() {
		loadTestLevel();
	}

	// Temporary
	public void loadTestLevel() {
		name = "test level";
		backgroundType = "video";
		backgroundPath = "level1.mp4";
		musicPath = "level1.ogg";
		scrollingSpeed = 0.16f;
		levelEvents = new LevelEvent[43];
		int i = 0;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 1825);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 5350);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 8915);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 9390);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 9780);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 10120);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 10490);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 10680);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 11140);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 11570);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 11920);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 12250);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 12510);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 12910);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 13320);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 13670);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 14000);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 14240);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 14450);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 14700);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 15070);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 15530);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 16000);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 16450);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 16890);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 17260);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 17530);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 17750);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 18190);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 18650);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 19040);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 19310);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 19580);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 19960);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 20360);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 20770);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 21020);
		i++;
		levelEvents[i] = new LevelEvent(LevelEventType.KEY_PRESS, 0, 21730);
		i++;

		levelEvents[i] = new LevelEvent(LevelEventType.KEY_HOLD, 0, 23030, 23900-23030);
		i++;

		levelEvents[i] = new LevelEvent(LevelEventType.KEY_HOLD, 1, 23900, 24890-23900);
		i++;

		levelEvents[i] = new LevelEvent(LevelEventType.KEY_HOLD, 2, 24890, 25740-24890);
		i++;

		levelEvents[i] = new LevelEvent(LevelEventType.KEY_HOLD, 3, 25740, 26610-25740);
		i++;

		levelEvents[i] = new LevelEvent(LevelEventType.KEY_HOLD, 2, 26610, 27490-26610);
		i++;
	}

	public void init() {
		Arrays.sort(levelEvents);
	}
}
