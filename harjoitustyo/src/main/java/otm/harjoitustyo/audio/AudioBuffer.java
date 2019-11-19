package otm.harjoitustyo.audio;

import org.lwjgl.system.MemoryUtil;
import otm.harjoitustyo.Resources;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_memory;
import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memFree;

public class AudioBuffer {

	private int bufferPointer;
	private int duration; // In seconds

	// .ogg files
	public AudioBuffer(String path) {
		load(path);
	}

	public int getBufferPointer() {
		return bufferPointer;
	}

	// In seconds
	public int getDuration() {
		return duration;
	}

	private void load(String path) {
		ShortBuffer rawAudioBuffer;

		IntBuffer channelsBuf = memAllocInt(1);
		IntBuffer sampleBuf = memAllocInt(1);

		ByteBuffer fileBuf = Resources.loadResourceAsByteBuffer(path);
		rawAudioBuffer = stb_vorbis_decode_memory(fileBuf, channelsBuf, sampleBuf);
		MemoryUtil.memFree(fileBuf);

		int channels = channelsBuf.get(0);
		int sampleRate = sampleBuf.get(0);

		duration = (int)Math.ceil(rawAudioBuffer.capacity()/((channels*sampleRate)/1000.0));

		memFree(channelsBuf);
		memFree(sampleBuf);

		int format;
		if(channels == 1) {
			format = AL_FORMAT_MONO16;
		} else {
			format = AL_FORMAT_STEREO16;
		}

		bufferPointer = alGenBuffers();

		alBufferData(bufferPointer, format, rawAudioBuffer, sampleRate);

		memFree(rawAudioBuffer);
	}

	public void delete() {
		alDeleteBuffers(bufferPointer);
	}
}
