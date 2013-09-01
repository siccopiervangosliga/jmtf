/**
 * Java Motion Tracking Framework
 */
package jmtf;


import java.util.ArrayList;
import java.util.Iterator;

/**
 * Abstract Class for ImageSources, when implementing a new ImageSource, inherit from this class.
 * @author Luca Rossetto
 *
 */
public abstract class AbstractImageSource implements ImageSource, ImageUpdater {

	/**
	 * Buffer for current image
	 */
	protected JMTFImage img = null;
	
	private ArrayList<ImageUpdateListener> listeners = new ArrayList<ImageUpdateListener>();
	
	/* (non-Javadoc)
	 * @see jmtf.ImageSource#hasMoreImages()
	 */
	@Override
	public abstract boolean hasMoreImages();

	/* (non-Javadoc)
	 * @see jmtf.ImageSource#getNextImage()
	 */
	@Override
	public abstract JMTFImage getNextImage();

	/* (non-Javadoc)
	 * @see jmtf.ImageSource#getCurrentImage()
	 */
	@Override
	public JMTFImage getCurrentImage() {
		return this.img;
	}

	@Override
	public void addImageUpdateListener(ImageUpdateListener listener) {
		this.listeners.add(listener);		
	}
	
	@Override
	public void removeImageUpdateListener(ImageUpdateListener listener) {
		this.listeners.remove(listener);
		
	}

	/**
	 * updates all registered ImageUpdateListener
	 */
	protected void notifyListeners(){
		Iterator<ImageUpdateListener> iter = this.listeners.iterator();
		while(iter.hasNext()){
			iter.next().update(this.img);
		}
	}

}
