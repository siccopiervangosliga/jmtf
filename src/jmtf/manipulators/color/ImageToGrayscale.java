/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.color;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * @author Luca Rossetto
 * 
 */
public class ImageToGrayscale extends AbstractImageManipulator {

	public ImageToGrayscale(ImageSource source) {
		super(source);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jmtf.ImageManipulator#manipulate(java.awt.image.BufferedImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {

		if (input != null && !input.isGrayscale()) {
			int c = 0, col;
			// for (int i = 0; i < input.getPixels().length; ++i) {
			for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
				for (int y = input.getROI().minY; y <= input.getROI().maxY; ++y) {
					col = input.getPixel(x, y);
					c = (JMTFImage.getRed(col) + JMTFImage.getRed(col) + JMTFImage
							.getRed(col)) / 3;
					input.setPixel(x, y, JMTFImage.getColor(c, c, c));
				}
			}
			input.setGrayscale(true);
		}
	}

}
