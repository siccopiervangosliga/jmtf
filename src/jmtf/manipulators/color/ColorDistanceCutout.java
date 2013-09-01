/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.color;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * Sets every pixel with a color which is further away from a specified color than a specified threshold to solid black
 * @author Luca Rossetto
 *
 */
public class ColorDistanceCutout extends AbstractImageManipulator {

	private int color, threshold;
	private boolean invert = false;

	public ColorDistanceCutout(ImageSource source, int color, int threshold) {
		super(source);
		this.color = color;
		this.threshold = threshold*threshold;
		this.invert = threshold < 0;
	}

	/* (non-Javadoc)
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		//int[] pixels = input.getPixels();
	//	for(int i = 0; i < pixels.length; ++i){
		for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
			for (int y = input.getROI().minY; y <= input.getROI().maxY; ++y) {
				if(this.invert ^ JMTFImage.squaredColorDistance(this.color, input.getPixel(x, y)) > this.threshold){
					input.setPixel(x, y, 0);
				}
			}
		}

	}

}
