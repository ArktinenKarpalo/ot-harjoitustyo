package otm.harjoitustyo.graphics;

import org.joml.Matrix4f;
import static org.lwjgl.opengl.GL45.*;

public class ShaderManager {

	public static ShaderProgram spriteShader, textShader;

	private static ShaderManager shaderManager = new ShaderManager();

	private static int currentShaderProgram;

	public static ShaderManager getInstance() {
		return shaderManager;
	}

	private ShaderManager() {
	}

	public void initShaders() {
		spriteShader = new ShaderProgram("sprite.vert", "sprite.frag");
		textShader = new ShaderProgram("sprite.vert", "text.frag");

		Matrix4f proj = new Matrix4f();
		proj.ortho2D(0, 1280, 720, 0);
		spriteShader.setUniformMatrix4f("projection", proj);
		textShader.setUniformMatrix4f("projection", proj);
	}

	public void useShader(ShaderProgram shader) {
		if(currentShaderProgram != shader.getShaderProgramId()) {
			glUseProgram(shader.getShaderProgramId());
			currentShaderProgram = shader.getShaderProgramId();
		}
	}
}
