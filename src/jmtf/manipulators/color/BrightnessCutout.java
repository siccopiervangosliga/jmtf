/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.color;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * removes all pixel with a smaller average intensity than the specified value. Result is inverted when value is negative
 * @author Luca Rossetto
 *
 */
public class BrightnessCutout extends AbstractImageManipulator {

	private int minBrightness;
	private boolean invert;

	public BrightnessCutout(ImageSource source, int minBrightness) {
		super(source);
		this.minBrightness = Math.abs(minBrightness);
		this.invert = minBrightness < 0;
	}

	/* (non-Javadoc)
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		//int[] pixels = input.getPixels();
		int r, g, b, brightness, col;
		
		//for(int i = 0; i < pixels.length; ++i){
		for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
			for (int y = input.getROI().minY; y <= input.getROI().maxY; ++y) {
				col = input.getPixel(x, y);
				r = JMTFImage.getRed(col);
				g = JMTFImage.getGreen(col);
				b = JMTFImage.getBlue(col);
				
				brightness = (r + g + b) / 3;
				
				if(this.invert ^ (brightness < this.minBrightness)){
					input.setPixel(x, y, 0);
				}
			}
		}

	}

}
