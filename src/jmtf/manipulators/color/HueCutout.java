/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.color;

import java.awt.Color;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * Sets all pixels outside the specified hue range to solid black
 * @author Luca Rossetto
 *
 */
public class HueCutout extends AbstractImageManipulator {

	private int min, max;
	
	/**
	 * 
	 * @param source Imagesource
	 * @param min minimal value for hue to keep
	 * @param max maximal value for hue to keep
	 */
	public HueCutout(ImageSource source, int min, int max) {
		super(source);
		this.min = min;
		this.max = max;
	}

	/* (non-Javadoc)
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		int[] pixels = input.getPixels();
		int r, g, b, h;
		
		for(int i = 0; i < pixels.length; ++i){
			r = JMTFImage.getRed(pixels[i]);
			g = JMTFImage.getGreen(pixels[i]);
			b = JMTFImage.getBlue(pixels[i]);
			
			h = (int)(Color.RGBtoHSB(r, g, b, null)[0] * 255f);
			
			if(h < min || h > max){
				pixels[i] = 0;
			}
		}
	}

}
