/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.color;

import java.awt.Color;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * @author Luca Rossetto
 *
 *Converts the input image to a HueTimesSaturation Image
 *
 */
public class ImageToHueTimesSaturation extends AbstractImageManipulator {

		
	/**
	 * @param source
	 */
	public ImageToHueTimesSaturation(ImageSource source) {
		super(source);
	}

	/* (non-Javadoc)
	 * @see jmtf.ImageManipulator#manipulate(java.awt.image.BufferedImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		
		int r, g, b, v;
		float[] hsv = new float[3];
		
		
		for(int i = 0; i < input.getPixels().length; ++i){
			r = JMTFImage.getRed(input.getPixels()[i]);
			g = JMTFImage.getGreen(input.getPixels()[i]);
			b = JMTFImage.getBlue(input.getPixels()[i]);
			
			Color.RGBtoHSB(r, g, b, hsv);
			
			v = (int)((hsv[0] + 1) * hsv[1] * 128f); //1 is added to the Hue so so it goes from 1 to 2 and not from 0 to 1 so the value is only 0 when the saturation is 0
			
			input.getPixels()[i] = JMTFImage.getColor(v, v, v);
		}
		
		input.setGrayscale(true);
	}
}

