/**
 * Java Motion Tracking Framework
 */
package jmtf;


/**
 * @author Luca Rossetto
 *
 */
public interface ImageUpdateListener {

	/**
	 * Updates the Image when a new one is available
	 * @param img the new image
	 */
	public void update(JMTFImage img);
	
}
