package otm.harjoitustyo.level;

public enum LevelEventType {
	KEY_DOWN, // Begin hold down, KEY_UP must follow
	KEY_UP, // End hold down, KEY_DOWN must precede
	KEY_PRESS // Single press, without KEY_UP
}
