/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.color;

import java.awt.Color;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * removes all pixels with a lower saturation value (in HSV space) than the specified value.
 * negative value inverts result.
 * @author Luca Rossetto
 *
 */
public class SaturationCutout extends AbstractImageManipulator {

	private float minSatturation;
	private boolean invert;
	
	public SaturationCutout(ImageSource source, float minSatturation) {
		super(source);
		this.minSatturation = Math.abs(minSatturation);
		this.invert = minSatturation < 0;
	}

	/* (non-Javadoc)
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		//int[] pixels = input.getPixels();
		int r, g, b, col;
		float[] hsv = new float[3];
		
		//for(int i = 0; i < pixels.length; ++i){
			
			for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
				for (int y = input.getROI().minY; y <= input.getROI().maxY; ++y) {
					
					col = input.getPixel(x, y);
					
					r = JMTFImage.getRed(col);
					g = JMTFImage.getGreen(col);
					b = JMTFImage.getBlue(col);
					
					Color.RGBtoHSB(r, g, b, hsv);
					
					if(this.invert ^ (hsv[1] < this.minSatturation)){
						input.setPixel(x, y, 0);
					}
				}
			}
			
			
		//}

	}

}
