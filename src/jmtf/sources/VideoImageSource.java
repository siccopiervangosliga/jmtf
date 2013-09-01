/**
 * Java Motion Tracking Framework
 */
package jmtf.sources;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

import jmtf.AbstractImageSource;
import jmtf.JMTFImage;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;

/**
 * @author Luca Rossetto
 *
 */
public class VideoImageSource extends AbstractImageSource {
	
	private LinkedList<JMTFImage> queue = new LinkedList<JMTFImage>();
	private IMediaReader reader;
	private int index = 0;
	
	/**
	 * Reads frames from 
	 * @param filename
	 */
	public VideoImageSource(String filename){
		this(filename, 0);
	}

	/**
	 * Reads frames from 
	 * @param filename
	 * @param removing the first Startframe - 1 frames
	 */
	public VideoImageSource(String filename, int startFrame) {
		this.reader = ToolFactory.makeReader(filename);
		reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR); //the wrong format for JMTF, but others are not supported
		reader.addListener(new FrameExtractor());
		fillQueue();
		while(this.index < startFrame && hasMoreImages()){
			getNextImage();
		}
	}

	/* (non-Javadoc)
	 * @see jmtf.AbstractImageSource#hasMoreImages()
	 */
	@Override
	public boolean hasMoreImages() {
		fillQueue();
		return !this.queue.isEmpty();
	}

	/* (non-Javadoc)
	 * @see jmtf.AbstractImageSource#getNextImage()
	 */
	@Override
	public JMTFImage getNextImage() {
		fillQueue();
		this.img = this.queue.poll();
		this.img.setFrameNumber(this.index++);
		notifyListeners();
		return this.img;
	}
	
	/*
	 * Queue is used in case multiple images are contained within one packet of video
	 */
	private void fillQueue(){
			while(queue.isEmpty() && this.reader.readPacket() == null){
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {}
			}
	}
	
	class FrameExtractor extends MediaToolAdapter {
		
		@Override
		public void onVideoPicture(IVideoPictureEvent event) {
			BufferedImage bimg = event.getImage();
			JMTFImage img = new JMTFImage(bimg);
			queue.offer(img);
		}
		
	}

}
