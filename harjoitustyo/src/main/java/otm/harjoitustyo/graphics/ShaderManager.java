package otm.harjoitustyo.graphics;

import static org.lwjgl.opengl.GL45.glUseProgram;


import org.joml.Matrix4f;

public class ShaderManager {

	public static ShaderProgram spriteShader, textShader, frameShader;

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
		frameShader = new ShaderProgram("sprite.vert", "frame.frag");

		Matrix4f proj = new Matrix4f();
		proj.ortho2D(0, 1280, 720, 0);
		spriteShader.setUniformMatrix4f("projection", proj);
		textShader.setUniformMatrix4f("projection", proj);
		frameShader.setUniformMatrix4f("projection", proj);

		frameShader.setUniform1i("texture1", 0);
		frameShader.setUniform1i("texture2", 1);
		frameShader.setUniform1i("texture3", 2);
	}

	public void useShader(ShaderProgram shader) {
		if(currentShaderProgram != shader.getShaderProgramId()) {
			glUseProgram(shader.getShaderProgramId());
			currentShaderProgram = shader.getShaderProgramId();
		}
	}
}
