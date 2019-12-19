package otm.harjoitustyo.graphics;

import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL45.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL45.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL45.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL45.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL45.glAttachShader;
import static org.lwjgl.opengl.GL45.glCompileShader;
import static org.lwjgl.opengl.GL45.glCreateProgram;
import static org.lwjgl.opengl.GL45.glCreateShader;
import static org.lwjgl.opengl.GL45.glDeleteShader;
import static org.lwjgl.opengl.GL45.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL45.glGetProgramiv;
import static org.lwjgl.opengl.GL45.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL45.glGetShaderiv;
import static org.lwjgl.opengl.GL45.glGetUniformLocation;
import static org.lwjgl.opengl.GL45.glLinkProgram;
import static org.lwjgl.opengl.GL45.glShaderSource;
import static org.lwjgl.opengl.GL45.glUniform4f;
import static org.lwjgl.opengl.GL45.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL45.glUseProgram;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memFree;


import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import otm.harjoitustyo.Resources;

public class ShaderProgram {

	// OpenGL Id
	private int shaderProgram;

	public ShaderProgram(String vertexShaderPath, String fragmentShaderPath) {
		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		compileShader(vertexShader, vertexShaderPath);

		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		compileShader(fragmentShader, fragmentShaderPath);

		shaderProgram = glCreateProgram();
		glAttachShader(shaderProgram, vertexShader);
		glAttachShader(shaderProgram, fragmentShader);
		glLinkProgram(shaderProgram);
		glUseProgram(shaderProgram);
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);

		IntBuffer success = BufferUtils.createIntBuffer(1);
		glGetProgramiv(shaderProgram, GL_LINK_STATUS, success);

		if(success.get(0) == 0) {
			System.out.println("Failed to link shader " + vertexShaderPath + ", " + fragmentShaderPath + ": " + glGetProgramInfoLog(shaderProgram));
		}
	}

	public void setUniformMatrix4f(String uniform, Matrix4f mat) {
		ShaderManager.getInstance().useShader(this);
		int uniformLoc = glGetUniformLocation(shaderProgram, uniform);
		FloatBuffer fb = memAllocFloat(16);
		mat.get(fb);
		glUniformMatrix4fv(uniformLoc, false, fb);
		memFree(fb);
	}

	public void setUniformVector4f(String uniform, Vector4f vec) {
		ShaderManager.getInstance().useShader(this);
		int uniformLoc = glGetUniformLocation(shaderProgram, uniform);
		glUniform4f(uniformLoc, vec.x, vec.y, vec.z, vec.w);
	}

	public void setUniform1i(String uniform, int i) {
		ShaderManager.getInstance().useShader(this);
		int uniformLoc = glGetUniformLocation(shaderProgram, uniform);
		glUniform1i(uniformLoc, i);
	}

	public int getShaderProgramId() {
		return shaderProgram;
	}

	private void compileShader(int shaderId, String sourcePath) {
		glShaderSource(shaderId, shaderSource(sourcePath));
		glCompileShader(shaderId);

		IntBuffer success = BufferUtils.createIntBuffer(1);

		glGetShaderiv(shaderId, GL_COMPILE_STATUS, success);

		if(success.get(0) == 0) {
			System.out.println("Failed to create shader " + sourcePath + ": " + glGetShaderInfoLog(shaderId));
		}
	}

	private String shaderSource(String path) {
		return Resources.loadResourceAsString(path);
	}
}
