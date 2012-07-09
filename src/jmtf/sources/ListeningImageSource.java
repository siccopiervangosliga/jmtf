/**
 * Java Motion Tracking Framework
 */
package jmtf.sources;

import jmtf.AbstractImageSource;
import jmtf.ImageUpdateListener;
import jmtf.JMTFImage;

/**
 * @author Luca Rossetto
 * buffers last result from an ImageUpdater and provides it.
 *
 */
public class ListeningImageSource extends AbstractImageSource implements
		ImageUpdateListener {

	public ListeningImageSource(JMTFImage initialImage){
		this.img = initialImage;
	}
	
	/* (non-Javadoc)
	 * @see jmtf.ImageUpdateListener#update(jmtf.JMTFImage)
	 */
	@Override
	public void update(JMTFImage img) {
		this.img = img.copy();
	}

	/* (non-Javadoc)
	 * @see jmtf.AbstractImageSource#hasMoreImages()
	 */
	@Override
	public boolean hasMoreImages() {
		return this.img == null;
	}

	/* (non-Javadoc)
	 * @see jmtf.AbstractImageSource#getNextImage()
	 */
	@Override
	public JMTFImage getNextImage() {
		return this.img.copy();
	}

}
