import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import otm.harjoitustyo.level.HighscoreDatabase;

public class DatabaseTest {

	HighscoreDatabase hdb;

	@Before
	public void init() {
		hdb = new HighscoreDatabase("jdbc:sqlite:memory");
	}

	@After
	public void clean() {
		hdb.resetDatabase();
	}

	@Test
	public void submitScoreTest() {
		hdb.submitScore("a", "b", 0L);
		hdb.submitScore("a", "b", 1L);
		Assert.assertEquals(2, hdb.getScores("b").size());
	}

	@Test
	public void getScoreTest() {
		hdb.submitScore("a", "b", 0L);
		hdb.submitScore("a", "b", 1L);
		hdb.submitScore("a", "c", 1L);
		List<HighscoreDatabase.Highscore> scores = hdb.getScores("b");
		Assert.assertEquals(2, scores.size());
	}
}