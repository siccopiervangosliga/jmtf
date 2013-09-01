/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.background;

import jmtf.ImageManipulator;
import jmtf.ImageUpdateListener;
import jmtf.ImageUpdater;
import jmtf.JMTFImage;

/**
 * @author Luca Rossetto
 *
 */
public interface BackgroundSubtractor extends ImageManipulator, ImageUpdateListener, ImageUpdater {

	void addBackgroundUpdateListener(ImageUpdateListener listener);
	
	void removeBackgroundUpdateListener(ImageUpdateListener listener);
	
	JMTFImage getCurrentBackground();
	
}
