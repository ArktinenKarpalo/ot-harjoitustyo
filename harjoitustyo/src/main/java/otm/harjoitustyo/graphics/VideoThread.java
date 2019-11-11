package otm.harjoitustyo.graphics;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;

import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memFree;

/**
 * Video thread is used to extract frames from
 * a video file and prepare them to format that can
 * be given directly to the OpenGL in the
 * thread that rendering is done in.
 *
 * Video thread will continuously loop the frames of the video
 * 1. Extract the next frame from the video
 * 2. Possibly skip the frame and go to 1
 * 3. ready = true
 * 4. Notify all objects waiting for VideoThread object
 * 5. Wait for VideoThread object to get notified
 * 6. Immediately delete the texture buffer containing
 * 	the frame, ready = false, go to 1
 *
 * 	Because of 6. the buffer containing the frame should be
 * 	sent to GPU or copied before notifying the thread to process the next frame.
 */

public class VideoThread extends MediaListenerAdapter implements Runnable {

	private BufferedImage bi = null;
	public ByteBuffer texBuf;
	public int height, width;
	public boolean ready = false;
	private boolean stop = false;
	private String videoPath;

	public int skipNFrames = 0;
	private int skipFramesRemaining = 0;

	public VideoThread(String videoPath) {
		this.videoPath = videoPath;
	}

	@Override
	public void run() {
		while(true && !stop) {
			IMediaReader reader = ToolFactory.makeReader(this.getClass().getResource("/"+videoPath).getPath());
			reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
			reader.addListener(this);
			while(reader.readPacket() == null && !stop);
		}
	}

	public void onVideoPicture(IVideoPictureEvent event) {
		if(skipFramesRemaining > 0) {
			skipFramesRemaining--;
			return;
		}
		bi = event.getImage();
		height = bi.getHeight();
		width = bi.getWidth();
		texBuf = memAlloc(width * height * 3);
		texBuf.put(((DataBufferByte)bi.getData().getDataBuffer()).getData());
		texBuf.flip();
		synchronized(this) {
			ready = true;
			this.notifyAll();
			try {
				this.wait();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			ready = false;
			skipFramesRemaining = skipNFrames;
			skipNFrames = 0;
		}
		memFree(texBuf);
	}
}
