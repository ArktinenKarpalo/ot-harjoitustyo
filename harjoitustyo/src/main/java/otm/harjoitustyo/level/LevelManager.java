package otm.harjoitustyo.level;

import otm.harjoitustyo.audio.AudioManager;
import otm.harjoitustyo.graphics.*;

import static org.lwjgl.glfw.GLFW.*;

public class LevelManager {

	private Level level;
	private long startTime, duration;
	boolean running;

	private VideoDecoder videoDecoder;

	Sprite background;
	Sprite[] levelEventSprites;

	int levelEventPointer = 0; // points to the index of the next keypress event in the level

	Sprite[] keyPressIndicators;

	// Locations of keypress indicators
	int keyX[] = {530, 530+55, 530+55*2, 530+55*3};
	int keyY = 40;

	public LevelManager(Level level) {
		this.level = level;
	}

	public boolean isRunning() {
		return running;
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
		background = new Sprite(new Texture("background_loading.png"));
		background.setZ(-1000);
		background.setSize(1280);
		Renderer.getInstance().addDrawable(background);

		keyPressIndicators = new Sprite[4];
		for(int i=0; i<4; i++) {
			keyPressIndicators[i] = new Sprite(TextureManager.getInstance().getTexture("bb.png"));
			keyPressIndicators[i].setSize(50);
			keyPressIndicators[i].setPosition(keyX[i], keyY);
			keyPressIndicators[i].setColor(200, 200, 50, 120);
			keyPressIndicators[i].setZ(10);
			Renderer.getInstance().addDrawable(keyPressIndicators[i]);
		}


		levelEventSprites = new Sprite[level.levelEvents.length];
		for(int i=0; i<level.levelEvents.length; i++) {
			if(level.levelEvents[i].type == LevelEventType.KEY_DOWN) {
				levelEventSprites[i] = new Sprite(TextureManager.getInstance().getTexture("bb.png"));
				levelEventSprites[i].setColor(0, 50, 150, 120);
				// Slow, but usually fast enough TODO
				for(int j=i; j<level.levelEvents.length; j++) {
					if(level.levelEvents[j].type == LevelEventType.KEY_UP && level.levelEvents[j].key == level.levelEvents[i].key) {
						levelEventSprites[i].setScale(20, (int)((level.levelEvents[j].time-level.levelEvents[i].time)*level.scrollingSpeed));
						break;
					}
				}
				Renderer.getInstance().addDrawable(levelEventSprites[i]);
			} else if(level.levelEvents[i].type == LevelEventType.KEY_UP) {
				// For key up, no indicator
			} else if(level.levelEvents[i].type == LevelEventType.KEY_PRESS) {
				levelEventSprites[i] = new Sprite(TextureManager.getInstance().getTexture("bb.png"));
				levelEventSprites[i].setColor(0, 50, 150, 120);
				levelEventSprites[i].setScale(20, 15);
				Renderer.getInstance().addDrawable(levelEventSprites[i]);
			}
		}

		this.duration = AudioManager.getInstance().loadFile(level.musicPath).getDuration();
		AudioManager.getInstance().playAudio(level.musicPath);
		startTime = System.currentTimeMillis();

		running = true;
	}

	public void loopLevel() {
		long now  = System.currentTimeMillis();
		if(now-startTime > this.duration) {
			finishLevel();
			return;
		}

		// levelEventPointer points to the index of the next keypress event in the level, may be length+1th element
		while(levelEventPointer < level.levelEvents.length && level.levelEvents[levelEventPointer].time < now-startTime) {
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
				background.getTexture().deleteTexture();
				background.setTexture(new Texture(videoDecoder.texBuf, videoDecoder.height, videoDecoder.width, "frame"));
				int expectedFrame = (int)Math.floor(((now-startTime)/1000.0)* videoDecoder.frameRate);
				if(expectedFrame > videoDecoder.currentFrame) {
					if(expectedFrame - videoDecoder.currentFrame > 2) {
						videoDecoder.skipNFrames += expectedFrame- videoDecoder.currentFrame;
					}
					videoDecoder.notifyAll();
				}
			}
		}

		for(int i=0; i<levelEventSprites.length; i++) {
			if(levelEventSprites[i] != null) {
				levelEventSprites[i].setPosition(keyX[level.levelEvents[i].key], -(now-startTime-level.levelEvents[i].time)*level.scrollingSpeed+90);
				if(now-startTime-level.levelEvents[i].time > 0) {
					levelEventSprites[i].setColor(200, 200, 200, 120);
				}
			}
		}
	}

	private void pressKey(int key) {
		long now = System.currentTimeMillis();

		while(levelEventPointer < level.levelEvents.length && level.levelEvents[levelEventPointer].time < now-startTime)
			levelEventPointer++;

		keyPressIndicators[key].setColor(250, 250, 100, 120);

		int future = levelEventPointer, past = levelEventPointer-1;
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
				if(closestEvent == null || (now-startTime)-level.levelEvents[past].time <  closestEvent.time-(now-startTime))
					closestEvent = level.levelEvents[past];
				break;
			}
			past--;
		}
		if(closestEvent != null && Math.abs((now-startTime)-closestEvent.time) < 1500) {
			System.out.println(Math.abs(closestEvent.time - (now-startTime)) + " and " + closestEvent.time + " "  + closestEvent.type + closestEvent.consumed);
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

		for(int i=0; i<keyPressIndicators.length; i++)
			if(keyPressIndicators[i] != null)
				Renderer.getInstance().deleteDrawable(keyPressIndicators[i]);
		Renderer.getInstance().deleteDrawable(background);
	}
}
