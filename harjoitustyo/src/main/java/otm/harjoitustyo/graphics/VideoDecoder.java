package otm.harjoitustyo.graphics;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import otm.harjoitustyo.Resources;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
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
 * 4. Notify all objects waiting for VideoDecoder object
 * 5. Wait for VideoDecoder object to get notified
 * 6. Immediately delete the texture buffer containing
 * 	the frame, ready = false, go to 1
 *
 * 	Because of 6. the buffer containing the frame should be
 * 	sent to GPU or copied before notifying the thread to process the next frame.
 */

public class VideoDecoder extends MediaListenerAdapter implements Runnable {

	public ByteBuffer texBuf;
	public int height, width;
	public boolean ready = false;
	public boolean stop = false;
	private String videoPath;
	public double frameRate = -1;

	public int skipNFrames = 0;
	private int skipFramesRemaining = 0;
	public int currentFrame;

	public VideoDecoder(String videoPath) {
		this.videoPath = videoPath;
	}

	@Override
	public void run() {
		while(true) {
			synchronized(this) {
				if(stop)
					break;
			}
			currentFrame = 1;
			IMediaReader reader = ToolFactory.makeReader(Resources.getResourceAsTemporaryFile(videoPath).getPath());
			reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
			reader.addListener(this);
			while(reader.readPacket() == null) {
				synchronized(this) {
					if(stop)
						break;
				}
				if(frameRate == -1)
					frameRate = reader.getContainer().getStream(0).getFrameRate().getDouble();
			}
		}
	}

	public void onVideoPicture(IVideoPictureEvent event) {
		currentFrame++;
		if(skipFramesRemaining > 0) {
			skipFramesRemaining--;
			return;
		}
		BufferedImage bi = event.getImage();
		height = bi.getHeight();
		width = bi.getWidth();

		texBuf = memAlloc(width * height * 3);
		texBuf.put(((DataBufferByte)bi.getRaster().getDataBuffer()).getData());
		texBuf.flip();

		synchronized(this) {
			if(stop) {
				memFree(texBuf);
				this.notifyAll();
				return;
			}
			ready = true;
			this.notifyAll();
			try {
				this.wait();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			ready = false;
			skipFramesRemaining += skipNFrames;
			skipNFrames = 0;
		}
		memFree(texBuf);
	}
}
