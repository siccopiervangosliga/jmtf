/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.color;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * Maps pixelvalues from a grayscale Image from a specified range to a single
 * value
 * 
 * @author Luca Rossetto
 * 
 */
public class GrayScaleMapper extends AbstractImageManipulator {

	private int defaultColor = 0;
	private int[][] ranges;

	/**
	 * 
	 * @param source
	 *            the input source
	 * @param defaultColor
	 *            color set to pixel if input color is in no set range
	 * @param ranges
	 *            mapping ranges in the form of {minimum, maximum, valuetoset}
	 */
	public GrayScaleMapper(ImageSource source, int defaultColor,
			int[]... ranges) {
		super(source);
		this.defaultColor = JMTFImage.getColor(defaultColor, defaultColor,
				defaultColor);
		this.ranges = ranges;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		if (input.isGrayscale()) {
			// int[] pixels = input.getPixels();
			// for (int i = 0; i < pixels.length; ++i) {
			for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
				for (int y = input.getROI().minY; y <= input.getROI().maxY; ++y) {
					int color = JMTFImage.getRed(input.getPixel(x, y));
					//pixels[i] = this.defaultColor;
					input.setPixel(x, y, defaultColor);
					for (int j = 0; j < this.ranges.length; ++j) {
						if (this.ranges[j].length >= 3) {
							if (color >= this.ranges[j][0]
									&& color <= this.ranges[j][1]) {
								input.setPixel(x, y, JMTFImage.getColor(
										this.ranges[j][2], this.ranges[j][2],
										this.ranges[j][2]));
								break;
							}
						}
					}
				}
			}
		}
	}

}
