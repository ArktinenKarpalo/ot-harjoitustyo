import org.junit.Assert;
import org.junit.Test;
import otm.harjoitustyo.Resources;

public class ResourcesTest {

	@Test
	public void readResourceAsString() {
		String result = Resources.loadResourceAsString("test_resource.txt");
		Assert.assertEquals("Test1\n" +
				"Test2", result);
	}

}
