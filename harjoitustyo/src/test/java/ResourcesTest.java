import org.junit.Assert;
import org.junit.Test;
import otm.harjoitustyo.Resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class ResourcesTest {

	private String testResourceContent = "Test1\n" +
			"Test2";

	@Test
	public void readResourceAsString() {
		String result = Resources.loadResourceAsString("test_resource.txt");
		Assert.assertEquals(testResourceContent, result);
	}

	@Test
	public void loadResourceAsByteBuffer() {
		ByteBuffer bb = Resources.loadResourceAsByteBuffer("test_resource.txt");
		Assert.assertEquals(testResourceContent.getBytes().length, bb.capacity());
		byte[] buf = new byte[bb.capacity()];
		bb.get(buf);
		Assert.assertArrayEquals(testResourceContent.getBytes(), buf);
	}

	@Test
	public void getResourceAsInputStream() {
		InputStream is = Resources.getResourceAsInputStream("test_resource.txt");
		byte[] buf = new byte[testResourceContent.length()];
		try {
			is.read(buf);
			Assert.assertEquals(0, is.available());
		} catch(IOException err) {
			throw new Error(err);
		}
		Assert.assertArrayEquals(testResourceContent.getBytes(), buf);
	}

	@Test
	public void getResourceAsTempFile() {
		File file = Resources.getResourceAsTemporaryFile("test_resource.txt");
		try {
			Assert.assertEquals(testResourceContent, Files.readAllLines(Paths.get(file.getPath())).stream().collect(Collectors.joining("\n")));
		} catch(IOException err) {
			throw new Error(err);
		}
	}

}
