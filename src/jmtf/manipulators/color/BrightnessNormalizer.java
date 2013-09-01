/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.color;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * Stretches the brightness-histogram of an Image over its full range
 * @author Luca Rossetto
 *
 */
public class BrightnessNormalizer extends AbstractImageManipulator {

	/**
	 * @param source
	 */
	public BrightnessNormalizer(ImageSource source) {
		super(source);
	}

	/* (non-Javadoc)
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		float min = 255f, max = 0, brightness;
		//int[] pixels = input.getPixels();
		//for (int pixel : pixels) {
		for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
			for (int y = input.getROI().minY; y <= input.getROI().maxY; ++y) {
				int col = input.getPixel(x, y);
				brightness = (float) (JMTFImage.getRed(col) + JMTFImage.getGreen(col) + JMTFImage.getBlue(col)) / 3.0f;
				min = Math.min(min, brightness);
				max = Math.max(max, brightness);
			}
		}
		if(max == min || (Math.abs(min) <= 0.01f && Math.abs(max) >= 254.99f)){
			return;
		}
		float factor = 255f/(max - min);
		
		//for(int i = 0; i < pixels.length; ++i){
		for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
			for (int y = input.getROI().minY; y <= input.getROI().maxY; ++y) {
				int col = input.getPixel(x, y);
				input.setPixel(x, y, JMTFImage.getColor(Math.min(Math.max((int)(((JMTFImage.getRed(col)-min)*factor)),0),255), Math.min(Math.max((int)(((JMTFImage.getGreen(col)-min)*factor)),0),255), Math.min(Math.max((int)(((JMTFImage.getBlue(col)-min)*factor)),0),255)));
			}
		}
	}

}
