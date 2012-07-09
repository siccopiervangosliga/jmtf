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
	public void addImageUpdateListener(ImageUpdateListener listener);
	
	/**
	 * Removes a ImageUpdateListener to the list of listeners to notify
	 * @param listener ImageUpdateListener to remove from the list
	 */
	public void removeImageUpdateListener(ImageUpdateListener listener);
	
}
