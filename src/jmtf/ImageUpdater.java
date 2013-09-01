/**
 * Java Motion Tracking Framework
 */
package jmtf;

/**
 * @author Luca Rossetto
 *
 */
public interface ImageUpdater {

	/**
	 * Adds a ImageUpdateListener to the list of listeners to notify
	 * @param listener ImageUpdateListener to add to the list
	 */
	void addImageUpdateListener(ImageUpdateListener listener);
	
	/**
	 * Removes a ImageUpdateListener to the list of listeners to notify
	 * @param listener ImageUpdateListener to remove from the list
	 */
	void removeImageUpdateListener(ImageUpdateListener listener);
	
}
