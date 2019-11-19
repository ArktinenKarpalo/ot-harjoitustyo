package otm.harjoitustyo;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import otm.harjoitustyo.audio.AudioManager;
import otm.harjoitustyo.graphics.Renderer;
import otm.harjoitustyo.level.Level;
import otm.harjoitustyo.level.LevelManager;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Game {

	private long window;
	LevelManager lm;

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
	}

	private void init() {
		GLFWErrorCallback.createPrint(System.err).set();

		if(!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

		window = glfwCreateWindow(1280, 720, "OTM-Harjoitustyö", NULL, NULL);
		if(window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
				glfwSetWindowShouldClose(window, true);
			if(lm != null && lm.isRunning())
				lm.handleInput(window, key, scancode, action, mods);
		});

		// Center the window
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.width() - 1280)/2, (vidmode.height() - 720)/2);

		glfwMakeContextCurrent(window);

		// Vsync
		glfwSwapInterval(0);

		glfwShowWindow(window);

		GL.createCapabilities();

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	private void loop() {
		glClearColor(0.0f, 0.0f, 0.1f, 0.0f);

		double prevTime = glfwGetTime();
		int frames = 0;

		Level testLevel = new Level();
		lm = new LevelManager(testLevel);
		lm.loadLevel();
		while(!glfwWindowShouldClose(window)) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			if(lm.isRunning())
				lm.loopLevel();
			else
				break;
			Renderer.getInstance().drawAll();

			glfwSwapBuffers(window);

			glfwPollEvents(); // Process input

			double now = glfwGetTime();
			if(now-prevTime >= 1.0) {
				System.out.println("FPS: " + frames/(now-prevTime));
				prevTime = glfwGetTime();
				frames = 0;
			}

			frames++;
		}
	}
}
