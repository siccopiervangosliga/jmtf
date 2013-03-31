/**
 * Java Motion Tracking Framework
 */
package jmtf.multiplexer;

import java.util.LinkedList;

import jmtf.AbstractImageSource;
import jmtf.JMTFImage;

/**
 * Multiplexes one ImageSource to multiple sources
 * @author Luca Rossetto
 *
 */
public class BufferedImageMultiplexer {
	
	private AbstractImageSource source;
	private MultiplexedImageSource[] multiplexedSources;
	
	/**
	 * 
	 * @param source AbstractImageSource from which to read
	 * @param numerOfSources number of sources to generate from that source
	 */
	public BufferedImageMultiplexer(AbstractImageSource source, int numerOfSources){
		this.source = source;
		this.multiplexedSources = new MultiplexedImageSource[numerOfSources];
		for(int i = 0; i < numerOfSources; ++i){
			this.multiplexedSources[i] = new MultiplexedImageSource();
		}
	}
	
	/**
	 * Returns one of the sources of the multiplexer
	 * @param sourceId the id of the source
	 * @return the source
	 */
	public AbstractImageSource getSource(int sourceId){
		if(sourceId >= this.multiplexedSources.length || sourceId < 0){
			throw new IllegalArgumentException("expected value between 0 and " + (this.multiplexedSources.length - 1) + ", got " + sourceId);
		}
		return this.multiplexedSources[sourceId];
	}
	
	public int getNumberOfSources(){
		return this.multiplexedSources.length;
	}
	
	private void addImageToMultiplexer(){
		if(this.source.hasMoreImages()){
			JMTFImage img = this.source.getNextImage();
			for(MultiplexedImageSource m : this.multiplexedSources){
				m.addImage(img);
			}
		}
	}
	
	private class MultiplexedImageSource extends AbstractImageSource {

		private LinkedList<JMTFImage> buffer = new LinkedList<JMTFImage>();
		
		private void addImage(JMTFImage img){
			this.buffer.offer(img);
		}
		
		/* (non-Javadoc)
		 * @see jmtf.AbstractImageSource#hasMoreImages()
		 */
		@Override
		public boolean hasMoreImages() {
			return (!this.buffer.isEmpty() || source.hasMoreImages());
		}

		/* (non-Javadoc)
		 * @see jmtf.AbstractImageSource#getNextImage()
		 */
		@Override
		public JMTFImage getNextImage() {
			if(this.buffer.isEmpty()){
				addImageToMultiplexer();
			}
			this.img = this.buffer.poll().copy();
			notifyListeners();
			System.gc();
			return this.img;
		}
	}
}