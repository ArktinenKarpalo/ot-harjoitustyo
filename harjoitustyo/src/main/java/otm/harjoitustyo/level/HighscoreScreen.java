package otm.harjoitustyo.level;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.glfw.GLFW;
import otm.harjoitustyo.graphics.Drawable;
import otm.harjoitustyo.graphics.Renderer;
import otm.harjoitustyo.graphics.text.Text;

public class HighscoreScreen implements Scene {

	enum State {
		WAITING_NICK, DISPLAY_SCORES
	}

	private boolean hasEnded;
	private State state;
	private long score;
	private int scoreListOffset;
	private String levelName, nick = "";
	private Text instruction, nickText;
	private List<HighscoreDatabase.Highscore> scores;

	private List<Text> tableTexts;
	private List<Drawable> displayDrawables;

	public HighscoreScreen(String levelName, long score) {
		this.levelName = levelName;
		this.score = score;
		this.state = State.WAITING_NICK;
		waitingInit();
	}

	private void waitingInit() {
		instruction = new Text("Type a nickname, press Enter to continue.", "OpenSans-Regular.ttf", 24, 1, 1001);
		instruction.setPosition(10, 50);
		Renderer.getInstance().addDrawable(instruction);
	}

	private void waitingClose() {
		Renderer.getInstance().deleteDrawable(instruction);
		instruction.delete();
		if(nickText != null) {
			Renderer.getInstance().deleteDrawable(nickText);
			nickText.delete();
		} else {
			nick = "Anonymous";
		}
		HighscoreDatabase.getInstance().submitScore(nick, levelName, score);
	}

	private void displayInit() {
		displayDrawables = new ArrayList<>();
		Text titleText = new Text("High Scores", "OpenSans-Regular.ttf", 72, 1, 1001);
		titleText.setPosition(1280 / 2 - 200, 100);
		Text instructionText = new Text("Arrow keys to navigate, enter to continue", "OpenSans-Regular.ttf", 24, 1, 1001);
		instructionText.setPosition(1280 / 2 - 600, 200);
		Renderer.getInstance().addDrawable(titleText, instructionText);
		displayDrawables.add(titleText);
		displayDrawables.add(instructionText);
		scores = HighscoreDatabase.getInstance().getScores(levelName);
		for(int i = 0; i < scores.size(); i++) {
			if(scores.get(i).nickname.equals(this.nick) && scores.get(i).score == this.score) {
				this.scoreListOffset = i;
				break;
			}
		}
		displayScores();
	}

	private void displayClose() {
		for(Drawable d:displayDrawables) {
			Renderer.getInstance().deleteDrawable(d);
			d.delete();
		}
		for(Text t:tableTexts) {
			Renderer.getInstance().deleteDrawable(t);
			t.delete();
		}

	}

	private void displayScores() {
		if(tableTexts != null) {
			tableTexts.forEach((text) -> {
				Renderer.getInstance().deleteDrawable(text);
				text.delete();
			});
		}
		tableTexts = new ArrayList<>();
		scoreListOffset = Math.max(Math.min(scoreListOffset, scores.size() - 5), 0);
		boolean colored = false; // There may be multiple scores with same score and name, we highlight only the first occurence
		for(int i = scoreListOffset; i < Math.min(scores.size(), scoreListOffset + 6); i++) {
			Text placement = new Text((i + 1) + ".", "OpenSans-Regular.ttf", 48, 1, 1001);
			Text nick = new Text(scores.get(i).nickname, "OpenSans-Regular.ttf", 42, 1, 1001);
			Text score = new Text(String.valueOf(scores.get(i).score), "OpenSans-Regular.ttf", 72, 1, 1001);
			placement.setPosition(10, 300 + 100 * (i - scoreListOffset));
			nick.setPosition(150, 300 + 100 * (i - scoreListOffset));
			score.setPosition(800, 300 + 100 * (i - scoreListOffset));

			if(!colored && scores.get(i).nickname.equals(this.nick) && scores.get(i).score == this.score) {
				colored = true;
				placement.setColor(200, 200, 0, 255);
				nick.setColor(200, 200, 0, 255);
				score.setColor(200, 200, 0, 255);
			}

			Renderer.getInstance().addDrawable(placement, nick, score);
			tableTexts.add(placement);
			tableTexts.add(nick);
			tableTexts.add(score);
		}
	}

	public Scene nextScene() {
		if(hasEnded) {
			return null;
		} else {
			return this;
		}
	}

	private void setNick(String nick) {
		this.nick = nick;
		if(nickText != null) {
			Renderer.getInstance().deleteDrawable(nickText);
			nickText.delete();
		}
		nickText = new Text(nick, "OpenSans-Regular.ttf", 72, 1, 1001);
		nickText.setPosition(50, 720 / 2 - 72);
		Renderer.getInstance().addDrawable(nickText);
	}

	public void handleKeyInput(long window, int key, int scancode, int action, int mods) {
		if(state == State.WAITING_NICK) {
			if(key == GLFW.GLFW_KEY_BACKSPACE && (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT)) {
				if(nick.length() > 0) {
					setNick(nick.substring(0, nick.length() - 1));
				}
			} else if(key == GLFW.GLFW_KEY_ENTER && action == GLFW.GLFW_PRESS) {
				waitingClose();
				state = State.DISPLAY_SCORES;
				displayInit();
			}
		} else if(state == State.DISPLAY_SCORES) {
			if(key == GLFW.GLFW_KEY_UP && (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT)) {
				scoreListOffset--;
				displayScores();
			} else if(key == GLFW.GLFW_KEY_DOWN && (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT)) {
				scoreListOffset++;
				displayScores();
			} else if(key == GLFW.GLFW_KEY_ENTER && action == GLFW.GLFW_PRESS) {
				displayClose();
				hasEnded = true;
				state = null;
			}
		}
	}

	public void handleCharInput(long window, int codepoint) {
		if(state == State.WAITING_NICK) {
			if(nick.length() < 16) {
				setNick(nick + String.valueOf(Character.toChars(codepoint)));
			}
		}
	}

	public void loop() {
		if(state == State.WAITING_NICK) {

		} else if(state == State.DISPLAY_SCORES) {

		}
	}
}
