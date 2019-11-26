package otm.harjoitustyo.audio;

import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.alDeleteSources;
import static org.lwjgl.openal.AL10.alGenSources;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourcei;
import static org.lwjgl.openal.ALC10.ALC_DEFAULT_DEVICE_SPECIFIER;
import static org.lwjgl.openal.ALC10.alcCloseDevice;
import static org.lwjgl.openal.ALC10.alcCreateContext;
import static org.lwjgl.openal.ALC10.alcDestroyContext;
import static org.lwjgl.openal.ALC10.alcGetString;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;
import static org.lwjgl.openal.ALC10.alcOpenDevice;


import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

public class AudioManager {

	private static AudioManager audioManager = new AudioManager();
	private HashMap<String, AudioBuffer> audioBuffers;
	private PriorityQueue<SourcePointerPair> sourcePointerRemoves;
	private long context, device;

	public static AudioManager getInstance() {
		return audioManager;
	}

	private AudioManager() {
		initAL();
		audioBuffers = new HashMap<>();
		sourcePointerRemoves = new PriorityQueue<SourcePointerPair>(Comparator.comparingLong((spp) -> spp.time));
	}

	private class SourcePointerPair {

		public int sourcePointer;
		public long time;

		SourcePointerPair(long time, int sourcePointer) {
			this.time = time;
			this.sourcePointer = sourcePointer;
		}

	}

	// Should be called periodically to delete old AL source pointers from memory
	public void deleteOldAudioSources() {
		long now = System.currentTimeMillis();
		while(sourcePointerRemoves.size() > 0 && sourcePointerRemoves.peek().time <= now) {
			alDeleteSources(sourcePointerRemoves.poll().sourcePointer);
		}
	}

	public void playAudio(String path) {
		AudioBuffer buf = loadFile(path);
		int sourcePointer = alGenSources();
		alSourcei(sourcePointer, AL_BUFFER, buf.getBufferPointer());
		sourcePointerRemoves.add(new SourcePointerPair(System.currentTimeMillis() + buf.getDuration() * 1000 + 1000, sourcePointer));
		alSourcePlay(sourcePointer);
	}

	// Load audiofile to OpenAL buffer, if it has not been loaded yet
	public AudioBuffer loadFile(String path) {
		AudioBuffer buf = audioBuffers.get(path);
		if(buf == null) {
			buf = new AudioBuffer(path);
			audioBuffers.put(path, buf);
		}
		return buf;
	}

	// Free the memory used by the given buffer
	public void removeBuffer(String path) {
		AudioBuffer buf = audioBuffers.remove(path);
		if(buf != null) {
			buf.delete();
		}
	}

	private void initAL() {
		String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
		device = alcOpenDevice(defaultDeviceName);

		int[] attr = {0};
		context = alcCreateContext(device, attr);

		alcMakeContextCurrent(context);

		ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
		ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
	}

	public void close() {
		alcDestroyContext(context);
		alcCloseDevice(device);
	}
}
