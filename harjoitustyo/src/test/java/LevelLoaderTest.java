import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import otm.harjoitustyo.level.Level;
import otm.harjoitustyo.level.LevelEvent;
import otm.harjoitustyo.level.LevelEventType;
import otm.harjoitustyo.level.LevelLoader;

public class LevelLoaderTest {

	private static Level testLevel, testLevelLoaded;

	@BeforeClass
	public static void init() {
		LevelEvent[] events = new LevelEvent[4];
		events[0] = new LevelEvent(LevelEventType.KEY_PRESS, 3, 0);
		events[1] = new LevelEvent(LevelEventType.KEY_HOLD, 2, 3500, 5000);
		events[2] = new LevelEvent(LevelEventType.KEY_HOLD, 0, 9000, 2222);
		events[3] = new LevelEvent(LevelEventType.KEY_PRESS, 1, 9000);
		testLevel = new Level("Test level", "video", "", "", 3.14f, events);
		testLevel.init();

		testLevelLoaded = LevelLoader.loadLevel("test_level.zip");
		testLevelLoaded.init();
	}

	@Test
	public void levelName() {
		Assert.assertEquals(testLevel.name, testLevelLoaded.name);
	}

	@Test
	public void scrollingSpeed() {
		Assert.assertEquals(testLevel.scrollingSpeed, testLevelLoaded.scrollingSpeed, 0.0);
	}

	@Test
	public void backgroundType() {
		Assert.assertEquals(testLevel.backgroundType, testLevelLoaded.backgroundType);
	}

	@Test
	public void levelEvents() {
		Assert.assertArrayEquals(testLevel.levelEvents, testLevelLoaded.levelEvents);
	}

	@Test
	public void backgroundPathNotNull() {
		Assert.assertNotNull(testLevelLoaded.backgroundPath);
	}

	@Test
	public void musicPathNotNull() {
		Assert.assertNotNull(testLevelLoaded.musicPath);
	}
}
