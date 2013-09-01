/**
 * Java Motion Tracking Framework
 */
package jmtf;


/**
 * Defines properties of an ImageSource
 * @author Luca Rossetto
 *
 */
public interface ImageSource {

	/**
	 * Checks if the ImageSource can provide a new image when calling getNextImage()
	 * @return result of the check
	 */
	boolean hasMoreImages();
	
	/**
	 * Gets the next image from specified source. Returns null if there are no more images.
	 * @return next image if available, null otherwise
	 */
	JMTFImage getNextImage();
	
	/**
	 * Gets the current image from the source. Returns null if there is none.
	 * @return current image if available, null otherwise
	 */
	JMTFImage getCurrentImage();
	
}
