package otm.harjoitustyo.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.lwjgl.opengl.GL45.*;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memFree;

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
			System.out.println("Failed to link shader "+ vertexShaderPath + ", " + fragmentShaderPath + ": " + glGetProgramInfoLog(shaderProgram));
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

	public void setUniformVector3f(String uniform, Vector3f vec) {
		ShaderManager.getInstance().useShader(this);
		int uniformLoc = glGetUniformLocation(shaderProgram, uniform);
		glUniform3f(uniformLoc, vec.x, vec.y, vec.z);
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
			System.out.println("Failed to create shader "+ sourcePath + ": "+ glGetShaderInfoLog(shaderId));
		}
	}

	private String shaderSource(String path) {
		String source = "";
		try {
			List<String> contents = Files.readAllLines(Paths.get(ClassLoader.getSystemClassLoader().getResource(path).getPath()));
			source = String.join("\n", contents);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return source;
	}
}
