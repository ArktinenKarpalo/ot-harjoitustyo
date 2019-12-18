package otm.harjoitustyo;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCharCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.system.MemoryUtil.NULL;


import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import otm.harjoitustyo.audio.AudioManager;
import otm.harjoitustyo.graphics.Renderer;
import otm.harjoitustyo.graphics.ShaderManager;
import otm.harjoitustyo.graphics.TextureManager;
import otm.harjoitustyo.graphics.text.Text;
import otm.harjoitustyo.level.Level;
import otm.harjoitustyo.level.LevelLoader;
import otm.harjoitustyo.level.LevelManager;
import otm.harjoitustyo.level.Scene;

public class Game {

	private long window;
	private Scene currentScene;

	public Game() {
	}

	public void run() {
		init();
		loop();

		// Game gets closed, close windows
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		glfwTerminate();
		glfwSetErrorCallback(null).free();

		AudioManager.getInstance().close();

		System.exit(0);
	}

	private void init() {
		GLFWErrorCallback.createPrint(System.err).set();

		if(!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

		window = glfwCreateWindow(1280, 720, "OTM-Harjoitustyö", NULL, NULL);
		if(window == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}

		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
				glfwSetWindowShouldClose(window, true);
			}
			if(currentScene != null) {
				currentScene.handleKeyInput(window, key, scancode, action, mods);
			}
		});

		glfwSetCharCallback(window, (window, codepoint) -> {
			if(currentScene != null) {
				currentScene.handleCharInput(window, codepoint);
			}
		});

		// Center the window
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.width() - 1280) / 2, (vidmode.height() - 720) / 2);

		glfwMakeContextCurrent(window);

		// Vsync
		glfwSwapInterval(0);

		glfwShowWindow(window);

		GL.createCapabilities();

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

		Renderer.getInstance();
		ShaderManager.getInstance().initShaders();
	}

	private void loop() {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		double prevTime = glfwGetTime();
		int frames = 0;

		Level testLevel = LevelLoader.loadLevel("level1.zip");
		testLevel.init();
		LevelManager lm = new LevelManager(testLevel);
		currentScene = lm;
		lm.loadLevel();

		while(!glfwWindowShouldClose(window)) {
			if(frames == 1) {
				AudioManager.getInstance().deleteOldAudioSources();
				TextureManager.getInstance().deleteUnusedTextures();
			}

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			if(currentScene == null) {
				break;
			} else {
				currentScene.loop();
				currentScene = currentScene.nextScene();
			}

			Renderer.getInstance().drawAll();

			glfwSwapBuffers(window);

			glfwPollEvents(); // Process input

			double now = glfwGetTime();
			if(now - prevTime >= 1.0) {
				System.out.println("FPS: " + frames / (now - prevTime));
				prevTime = glfwGetTime();
				frames = 0;
			}
			frames++;
		}
	}
}
