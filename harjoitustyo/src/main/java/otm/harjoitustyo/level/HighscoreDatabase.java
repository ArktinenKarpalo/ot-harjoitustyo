package otm.harjoitustyo.level;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HighscoreDatabase {

	private static HighscoreDatabase highscoreDatabase = new HighscoreDatabase();

	public static HighscoreDatabase getInstance() {
		return highscoreDatabase;
	}

	private Connection conn;

	private HighscoreDatabase() {
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:/" + Paths.get("").toAbsolutePath() + "/score.sqlite3");
			initTables();
		} catch(SQLException err) {
			throw new Error(err);
		}
	}

	public class Highscore {
		String nickname, levelName;
		int score, date;

		public Highscore(String nickname, String levelName, int score, int date) {
			this.nickname = nickname;
			this.levelName = levelName;
			this.score = score;
			this.date = date;
		}

		public String toString() {
			return nickname + " - " + levelName + " - " + score + " - " + date;
		}
	}

	/**
	 * Used to get list of scores of a certain level from the database
	 * @param levelName Name of the level to get scores of
	 * @return List of all saved scores for the level
	 */
	public ArrayList<Highscore> getScores(String levelName) {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Scores WHERE levelname = ? ORDER BY score DESC");
			ps.setString(1, levelName);
			ResultSet rs = ps.executeQuery();
			ArrayList<Highscore> list = new ArrayList<>();
			while(rs.next()) {
				list.add(new Highscore(rs.getString("nickname"),
					rs.getString("levelname"),
					rs.getInt("score"),
					rs.getInt("date")));
			}
			return list;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Saves a row with given parameters and current date as timestamp to the databse
	 * @param nickname Nickname of the player
	 * @param levelName Name of the level
	 * @param score Score of the player's current attempt
	 */
	public void submitScore(String nickname, String levelName, int score) {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO Scores(nickname, levelname, score, date) VALUES(?,?,?,?)");
			ps.setString(1, nickname);
			ps.setString(2, levelName);
			ps.setInt(3, score);
			ps.setInt(4, (int) (System.currentTimeMillis() / 1000L));
			ps.execute();

		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	private void initTables() {
		String sql = "CREATE TABLE IF NOT EXISTS Scores (" +
			"id INTEGER PRIMARY KEY AUTOINCREMENT," +
			"nickname TEXT," +
			"levelname TEXT," +
			"score INTEGER," +
			"date INTEGER)"; // unix-time
		try {
			conn.prepareStatement(sql).execute();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
