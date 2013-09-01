/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.color;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * Sets all gray pixels inside a color threshold to solid black
 * 
 * @author Luca Rossetto
 * 
 */
public class GrayCutout extends AbstractImageManipulator {

	private int threshold;
	private boolean invert;

	/**
	 * 
	 * @param source
	 * @param threshold
	 *            threshold, if negative range is inverted
	 */
	public GrayCutout(ImageSource source, int threshold) {
		super(source);
		this.threshold = Math.abs(threshold);
		this.invert = threshold < 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		// int[] pixels = input.getPixels();
		int r, g, b, brightness, col;

		// for(int i = 0; i < pixels.length; ++i){
		for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
			for (int y = input.getROI().minY; y <= input.getROI().maxY; ++y) {
				col = input.getPixel(x, y);
				r = JMTFImage.getRed(col);
				g = JMTFImage.getGreen(col);
				b = JMTFImage.getBlue(col);

				brightness = (r + g + b) / 3;

				if ((Math.abs(r - brightness) <= this.threshold
						&& Math.abs(g - brightness) <= threshold && Math.abs(b
						- brightness) <= this.threshold)
						^ this.invert) {
					input.setPixel(x, y, 0);
				}
			}
		}

	}

}
