package otm.harjoitustyo;

import org.lwjgl.system.MemoryUtil;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.stream.Collectors;

public class Resources {
	public static String loadResourceAsString(String path) {
		BufferedReader br = new BufferedReader(new InputStreamReader(Resources.class.getResourceAsStream("/" + path)));
		return br.lines().collect(Collectors.joining("\n"));
	}

	/**
	 * Used to load resources from the jar
	 * @param path
	 * @return ByteBuffer allocated by LWJGL MemoryUtil, must be freed with memFree after usage
	 */
	public static ByteBuffer loadResourceAsByteBuffer(String path) {
		InputStream is = Resources.class.getResourceAsStream("/" + path);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int len = 0;
		byte[] buf = new byte[1<<12];
		while(len != -1) {
			if(len > 0)
			baos.write(buf, 0, len);
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

	public static InputStream getResourceAsInputStream(String path) {
		return Resources.class.getResourceAsStream("/" + path);
	}
}