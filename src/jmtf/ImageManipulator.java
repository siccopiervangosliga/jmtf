/**
 * Java Motion Tracking Framework
 */
package jmtf;

/**
 * Defines properties of every ImageManipulator
 * @author Luca Rossetto
 *
 */
public interface ImageManipulator extends ImageSource {

	/**
	 * Performs the actual image manipulation
	 * @param input the input image to manipulate
	 * @return the manipulated image
	 */
	public void manipulate(JMTFImage input);
	
	/**
	 * returns the current ImageSource of the ImageManipulator
	 * @return the current ImageSource
	 */
	public ImageSource getSource();
	
	/**
	 * sets the ImageSource for the ImageManipulator
	 * @param source the ImageSource to set
	 */
	public void setSource(ImageSource source);
	
}
