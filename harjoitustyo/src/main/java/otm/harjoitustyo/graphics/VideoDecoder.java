package otm.harjoitustyo.graphics;

import static org.lwjgl.system.MemoryUtil.memFree;


import java.nio.ByteBuffer;
import org.bytedeco.ffmpeg.avcodec.AVCodec;
import org.bytedeco.ffmpeg.avcodec.AVCodecContext;
import org.bytedeco.ffmpeg.avcodec.AVPacket;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.avutil.AVFrame;
import org.bytedeco.ffmpeg.avutil.AVRational;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avformat;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacpp.PointerPointer;
import org.lwjgl.system.MemoryUtil;

/**
 * Video thread is used to extract frames from
 * a video file and prepare them to format that can
 * be given directly to the OpenGL in the
 * thread that rendering is done in.
 * <p>
 * Video thread will continuously loop the frames of the video
 * 1. Extract the next frame from the video
 * 2. Possibly skip the frame and go to 1
 * 3. ready = true
 * 4. Notify all objects waiting for VideoDecoder object
 * 5. Wait for VideoDecoder object to get notified
 * 6. Immediately delete the texture buffer containing
 * the frame, ready = false, go to 1
 * <p>
 * Because of 6. the buffer containing the frame should be
 * sent to GPU or copied before notifying the thread to process the next frame.
 */

public class VideoDecoder implements Runnable {

	public ByteBuffer y, u, v;
	byte[] bytesFull, bytesQuarter;
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
		AVFormatContext format_ctx = new AVFormatContext(null);
		AVPacket pkt = new AVPacket();

		avformat.avformat_open_input(format_ctx, videoPath, null, null);

		avformat.avformat_find_stream_info(format_ctx, (PointerPointer) null);

		// Print info about format
		avformat.av_dump_format(format_ctx, 0, videoPath, 0);

		int video_stream_index = -1;
		for(int i = 0; i < format_ctx.nb_streams(); i++) {
			if(format_ctx.streams(i).codecpar().codec_type() == avutil.AVMEDIA_TYPE_VIDEO) {
				video_stream_index = i;
				break;
			}
		}

		height = format_ctx.streams(video_stream_index).codecpar().height();
		width = format_ctx.streams(video_stream_index).codecpar().width();

		AVCodecContext codec_ctx = avcodec.avcodec_alloc_context3(null);
		avcodec.avcodec_parameters_to_context(codec_ctx, format_ctx.streams(video_stream_index).codecpar());

		AVCodec codec = avcodec.avcodec_find_decoder(codec_ctx.codec_id());
		avcodec.avcodec_open2(codec_ctx, codec, (PointerPointer) null);

		AVFrame frm = avutil.av_frame_alloc();
		AVRational avratFramerate = avformat.av_guess_frame_rate(format_ctx, format_ctx.streams(video_stream_index), null);
		frameRate = ((double) avratFramerate.num()) / avratFramerate.den();
		currentFrame = 1;

		y = MemoryUtil.memAlloc(width * height);
		u = MemoryUtil.memAlloc(width * height / 4);
		v = MemoryUtil.memAlloc(width * height / 4);

		bytesFull = new byte[width * height];
		bytesQuarter = new byte[width * height / 4];

		while(true) {
			synchronized(this) {
				if(stop) {
					break;
				}
			}
			currentFrame = 1;
			avformat.av_seek_frame(format_ctx, video_stream_index, 0, avformat.AVSEEK_FLAG_ANY);
			while(avformat.av_read_frame(format_ctx, pkt) >= 0) {
				synchronized(this) {
					if(stop) {
						break;
					}
				}
				if(pkt.stream_index() == video_stream_index) {
					currentFrame++;
					avcodec.avcodec_send_packet(codec_ctx, pkt);
					int ret = avcodec.avcodec_receive_frame(codec_ctx, frm);
					if(skipFramesRemaining > 0) {
						skipFramesRemaining--;
					} else {
						if(ret >= 0) {
							frm.data(0).position(0).get(bytesFull);
							y.position(0);
							y.put(bytesFull);
							y.position(0);

							bytesQuarter = new byte[width * height / 4];
							frm.data(1).position(0).get(bytesQuarter);
							u.position(0);
							u.put(bytesQuarter);
							u.position(0);

							frm.data(2).position(0).get(bytesQuarter);
							v.position(0);
							v.put(bytesQuarter);
							v.position(0);

							synchronized(this) {
								if(stop) {
									this.notifyAll();
									break;
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

						}
					}
				}
				avcodec.av_packet_unref(pkt);
			}
		}
		memFree(y);
		memFree(u);
		memFree(v);
		avutil.av_frame_free(frm);
		avcodec.avcodec_close(codec_ctx);
		avformat.avformat_close_input(format_ctx);
	}
}
