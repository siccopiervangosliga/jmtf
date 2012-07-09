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
			int c;
			for (int i = 0; i < input.getPixels().length; ++i) {
				c = (JMTFImage.getRed(input.getPixels()[i])
						+ JMTFImage.getRed(input.getPixels()[i]) + JMTFImage
						.getRed(input.getPixels()[i])) / 3;
				input.getPixels()[i] = JMTFImage.getColor(c, c, c);
			}
			input.setGrayscale(true);
		}
	}

}
