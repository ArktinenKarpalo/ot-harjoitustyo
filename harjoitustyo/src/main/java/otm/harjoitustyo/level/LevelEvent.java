package otm.harjoitustyo.level;

public class LevelEvent implements Comparable {

	public int key; // 0, 1, 2, 3...
	public LevelEventType type;
	public long time; // Milliseconds since the start of the level, same key must not have more than 1 event at one time
	public boolean consumed = false; // Consumed by key press of the user

	public LevelEvent(LevelEventType type, int key, long time) {
		this.type = type;
		this.key = key;
		this.time = time;
	}

	@Override
	public int compareTo(Object o) {
		return Long.compare(time, ((LevelEvent) o).time);
	}
}
