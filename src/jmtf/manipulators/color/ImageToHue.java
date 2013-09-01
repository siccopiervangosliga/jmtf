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
 */
public class ImageToHue extends AbstractImageManipulator {

	/**
	 * @param source
	 */
	public ImageToHue(ImageSource source) {
		super(source);
	}

	/* (non-Javadoc)
	 * @see jmtf.ImageManipulator#manipulate(java.awt.image.BufferedImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		
		
		int r, g, b, h, c;

		
		
		//for(int i = 0; i < input.getPixels().length; ++i){
		for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
			for (int y = input.getROI().minY; y <= input.getROI().maxY; ++y) {
			
			c = input.getPixel(x, y);
			
			if((c & 0xffffff) == 0){
				input.setPixel(x, y, 0);
				continue;
			}
			
			r = JMTFImage.getRed(c);
			g = JMTFImage.getGreen(c);
			b = JMTFImage.getBlue(c);
			
			h = (int)(Color.RGBtoHSB(r, g, b, null)[0] * 255f);
			
			input.setPixel(x, y, JMTFImage.getColor(h, h, h));
			
		}
		}
		
		input.setGrayscale(true);
		
	}

}
