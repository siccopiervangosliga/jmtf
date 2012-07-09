/**
 * Java Motion Tracking Framework
 */
package jmtf.sources;


import jmtf.AbstractImageSource;
import jmtf.JMTFImage;

/**
 * @author Luca Rossetto
 *
 */
public class SingleImageSource extends AbstractImageSource {

	public SingleImageSource(JMTFImage input){
		this.img = input;
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
		notifyListeners();
		return this.img.copy();
	}

}
