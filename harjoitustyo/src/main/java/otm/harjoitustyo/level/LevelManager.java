package otm.harjoitustyo.level;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_J;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_K;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;


import org.lwjgl.opengl.GL12;
import otm.harjoitustyo.audio.AudioManager;
import otm.harjoitustyo.graphics.Renderer;
import otm.harjoitustyo.graphics.Sprite;
import otm.harjoitustyo.graphics.Text;
import otm.harjoitustyo.graphics.Texture;
import otm.harjoitustyo.graphics.TextureManager;
import otm.harjoitustyo.graphics.VideoDecoder;

public class LevelManager {

	private Level level;
	private long startTime, duration;
	boolean running;
	long score = 0;

	private VideoDecoder videoDecoder;

	private Text scoreText;

	Sprite background;
	Sprite[] levelEventSprites;

	int levelEventPointer = 0; // points to the index of the next keypress event in the level

	Sprite[] keyPressIndicators;

	// Locations of keypress indicators
	int[] keyX = {530, 530 + 55, 530 + 55 * 2, 530 + 55 * 3};
	int keyY = 40;

	public LevelManager(Level level) {
		this.level = level;
	}

	public boolean isRunning() {
		return running;
	}

	// accuracy = missed by x milliseconds
	public int eventScore(int accuracy) {
		int score = 1000 - Math.abs(accuracy);
		return Math.max(score, 0);
	}

	public void loadLevel() {
		level.init();

		if(level.backgroundType.equals("video")) {
			videoDecoder = new VideoDecoder(level.backgroundPath);
			Thread videoThread = new Thread(videoDecoder);
			videoThread.start();
		} else {
			throw new Error("Background not implemented.");
		}
		TextureManager.getInstance().setTexture("frame", new Texture("background_loading.png"));
		background = new Sprite(TextureManager.getInstance().getTexture("frame"));
		background.setZ(-1000);
		background.setSize(1280);
		Renderer.getInstance().addDrawable(background);

		keyPressIndicators = new Sprite[4];
		for(int i = 0; i < 4; i++) {
			keyPressIndicators[i] = new Sprite(TextureManager.getInstance().getFileTexture("bb.png"));
			keyPressIndicators[i].setSize(50);
			keyPressIndicators[i].setPosition(keyX[i], keyY);
			keyPressIndicators[i].setColor(200, 200, 50, 120);
			keyPressIndicators[i].setZ(10);
			Renderer.getInstance().addDrawable(keyPressIndicators[i]);
		}

		levelEventSprites = new Sprite[level.levelEvents.length];
		for(int i = 0; i < level.levelEvents.length; i++) {
			if(level.levelEvents[i].type == LevelEventType.KEY_DOWN) {
				levelEventSprites[i] = new Sprite(TextureManager.getInstance().getFileTexture("bb.png"));
				levelEventSprites[i].setColor(0, 50, 150, 120);
				// Slow, but usually fast enough TODO
				for(int j = i; j < level.levelEvents.length; j++) {
					if(level.levelEvents[j].type == LevelEventType.KEY_UP && level.levelEvents[j].key == level.levelEvents[i].key) {
						levelEventSprites[i].setScale(20, (int) ((level.levelEvents[j].time - level.levelEvents[i].time) * level.scrollingSpeed));
						break;
					}
				}
				Renderer.getInstance().addDrawable(levelEventSprites[i]);
			} else if(level.levelEvents[i].type == LevelEventType.KEY_PRESS) {
				levelEventSprites[i] = new Sprite(TextureManager.getInstance().getFileTexture("bb.png"));
				levelEventSprites[i].setColor(0, 50, 150, 120);
				levelEventSprites[i].setScale(20, 15);
				Renderer.getInstance().addDrawable(levelEventSprites[i]);
			}
		}

		scoreText = new Text("0", "OpenSans-Regular.ttf", 72, 1, 1001);
		scoreText.setColor(0, 0, 0, 255);
		scoreText.setPosition(50, 80);
		Renderer.getInstance().addDrawable(scoreText);

		this.duration = AudioManager.getInstance().loadFile(level.musicPath).getDuration();
		AudioManager.getInstance().playAudio(level.musicPath);
		startTime = System.currentTimeMillis();

		running = true;
	}

	public void loopLevel() {
		long now = System.currentTimeMillis();
		if(now - startTime > this.duration) {
			finishLevel();
			return;
		}

		// levelEventPointer points to the index of the next keypress event in the level, may be length+1th element
		while(levelEventPointer < level.levelEvents.length && level.levelEvents[levelEventPointer].time < now - startTime) {
			levelEventPointer++;
		}

		if(level.backgroundType == "video") {
			synchronized(videoDecoder) {
				if(!videoDecoder.ready) {
					try {
						videoDecoder.wait();
					} catch(InterruptedException e) {
						e.printStackTrace();
					}
				}
				TextureManager.getInstance().setTexture("frame", new Texture(videoDecoder.texBuf, videoDecoder.height, videoDecoder.width, GL12.GL_BGR));
				background.setTexture(TextureManager.getInstance().getTexture("frame"));
				int expectedFrame = (int) Math.floor(((now - startTime) / 1000.0) * videoDecoder.frameRate);
				if(expectedFrame > videoDecoder.currentFrame) {
					if(expectedFrame - videoDecoder.currentFrame > 2) {
						videoDecoder.skipNFrames += expectedFrame - videoDecoder.currentFrame;
					}
					videoDecoder.notifyAll();
				}
			}
		}

		for(int i = 0; i < levelEventSprites.length; i++) {
			if(levelEventSprites[i] != null) {
				levelEventSprites[i].setPosition(keyX[level.levelEvents[i].key], -(now - startTime - level.levelEvents[i].time) * level.scrollingSpeed + 90);
				if(now - startTime - level.levelEvents[i].time > 0) {
					levelEventSprites[i].setColor(200, 200, 200, 120);
				}
			}
		}
	}

	private void pressKey(int key) {
		long now = System.currentTimeMillis();

		while(levelEventPointer < level.levelEvents.length && level.levelEvents[levelEventPointer].time < now - startTime) {
			levelEventPointer++;
		}

		keyPressIndicators[key].setColor(250, 250, 100, 120);

		int future = levelEventPointer, past = levelEventPointer - 1;
		LevelEvent closestEvent = null;
		while(future < level.levelEvents.length) {
			if((level.levelEvents[future].type == LevelEventType.KEY_DOWN || level.levelEvents[future].type == LevelEventType.KEY_PRESS) && level.levelEvents[future].key == key && !level.levelEvents[future].consumed) {
				closestEvent = level.levelEvents[future];
				break;
			}
			future++;
		}
		while(past >= 0) {
			if((level.levelEvents[past].type == LevelEventType.KEY_DOWN || level.levelEvents[past].type == LevelEventType.KEY_PRESS) && level.levelEvents[past].key == key && !level.levelEvents[past].consumed) {
				if(closestEvent == null || (now - startTime) - level.levelEvents[past].time < closestEvent.time - (now - startTime)) {
					closestEvent = level.levelEvents[past];
				}
				break;
			}
			past--;
		}
		if(closestEvent != null && Math.abs((now - startTime) - closestEvent.time) < 1500) {
			score += eventScore(Math.round(closestEvent.time - (now - startTime)));
			Renderer.getInstance().deleteDrawable(scoreText);
			scoreText.delete();
			scoreText = new Text(Long.toString(score), "OpenSans-Regular.ttf", 72, 1, 1001);
			scoreText.setPosition(50, 80);
			scoreText.setColor(0, 0, 0, 255);
			Renderer.getInstance().addDrawable(scoreText);

			System.out.println(Math.abs(closestEvent.time - (now - startTime)) + " and " + closestEvent.time + " " + closestEvent.type + closestEvent.consumed);
			closestEvent.consumed = true;
		}
	}

	private void releaseKey(int key) {
		keyPressIndicators[key].setColor(200, 200, 50, 120);
	}

	public void handleInput(long window, int key, int scancode, int action, int mods) {
		if(running) {
			if(key == GLFW_KEY_D) {
				if(action == GLFW_PRESS) {
					pressKey(0);
				} else if(action == GLFW_RELEASE) {
					releaseKey(0);
				}
			} else if(key == GLFW_KEY_F) {
				if(action == GLFW_PRESS) {
					pressKey(1);
				} else if(action == GLFW_RELEASE) {
					releaseKey(1);
				}
			} else if(key == GLFW_KEY_J) {
				if(action == GLFW_PRESS) {
					pressKey(2);
				} else if(action == GLFW_RELEASE) {
					releaseKey(2);
				}
			} else if(key == GLFW_KEY_K) {
				if(action == GLFW_PRESS) {
					pressKey(3);
				} else if(action == GLFW_RELEASE) {
					releaseKey(3);
				}
			}
		}
	}

	public void finishLevel() {
		running = false;
		synchronized(videoDecoder) {
			videoDecoder.stop = true;
			videoDecoder.notifyAll();
		}
		background.getTexture().deleteTexture(); // Must be deleted separately, because we don't use TextureManager for video frames

		for(int i = 0; i < keyPressIndicators.length; i++) {
			if(keyPressIndicators[i] != null) {
				Renderer.getInstance().deleteDrawable(keyPressIndicators[i]);
			}
		}
		Renderer.getInstance().deleteDrawable(background);

		Renderer.getInstance().deleteDrawable(scoreText);
	}
}
