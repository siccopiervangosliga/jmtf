/**
 * Java Motion Tracking Framework
 */
package jmtf.sources;

import jmtf.AbstractImageSource;
import jmtf.JMTFImage;

/**
 * 
 * @author Luca Rossetto
 * Returns an image filled with a color specified
 */
public class ColorImageSource extends AbstractImageSource {
	
	public ColorImageSource(int width, int height, int color){
		this.img = new JMTFImage(width, height);
		for(int i = 0; i < this.img.getPixels().length; ++i){
			this.img.getPixels()[i] = color;
		}
	}

	/* (non-Javadoc)
	 * @see jmtf.AbstractImageSource#hasMoreImages()
	 */
	@Override
	public boolean hasMoreImages() {
		return true;
	}

	/* (non-Javadoc)
	 * @see jmtf.AbstractImageSource#getNextImage()
	 */
	@Override
	public JMTFImage getNextImage() {
		return this.img.copy();
	}

}
