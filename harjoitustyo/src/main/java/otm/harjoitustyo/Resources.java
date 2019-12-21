package otm.harjoitustyo;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;
import org.lwjgl.system.MemoryUtil;

public class Resources {

	/**
	 * Used to load resources from the jar as string
	 *
	 * @param path path of the file in the jar
	 * @return Returns the file as string, using \n as linebreak
	 */
	public static String loadResourceAsString(String path) {
		BufferedReader br = new BufferedReader(new InputStreamReader(Resources.class.getResourceAsStream("/" + path)));
		return br.lines().collect(Collectors.joining("\n"));
	}

	/**
	 * Used to load resources from the jar
	 *
	 * @param path path of the file in the jar
	 * @return ByteBuffer allocated by LWJGL MemoryUtil, must be freed with memFree after usage
	 */
	public static ByteBuffer loadResourceAsByteBuffer(String path) {
		InputStream is = Resources.class.getResourceAsStream("/" + path);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int len = 0;
		byte[] buf = new byte[1 << 12];
		while(len != -1) {
			if(len > 0) {
				baos.write(buf, 0, len);
			}
			try {
				len = is.read(buf);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		ByteBuffer bb = MemoryUtil.memAlloc(baos.size());
		bb.put(baos.toByteArray());
		bb.flip();
		return bb;
	}

	/**
	 * @param path path of the file in the jar
	 * @return InputSteam to file of the path
	 */
	public static InputStream getResourceAsInputStream(String path) {
		return Resources.class.getResourceAsStream("/" + path);
	}

	/**
	 * The file gets deleted when the application is closed.
	 *
	 * @param path path to the file in the jar
	 * @return Temporary copy of the file
	 */
	public static File getResourceAsTemporaryFile(String path) {
		InputStream is = Resources.class.getResourceAsStream("/" + path);
		File tempFile = null;
		try {
			tempFile = File.createTempFile("otm_temp_" + path, null);
			Files.copy(is, Paths.get(tempFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
			tempFile.deleteOnExit();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return tempFile;
	}
}
