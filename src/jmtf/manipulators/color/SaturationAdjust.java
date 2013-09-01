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
public class SaturationAdjust extends AbstractImageManipulator {

	private float factor;

	/**
	 * @param source
	 * @param factor
	 *            factor to multiply saturation value with
	 */
	public SaturationAdjust(ImageSource source, float factor) {
		super(source);
		this.factor = factor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {

		//int[] pixels = input.getPixels();
		int col;
		float[] hsv = new float[3];

		for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
			for (int y = input.getROI().minY; y <= input.getROI().maxY; ++y) {
				// for(int i = 0; i < pixels.length; ++i){
				col = input.getPixel(x, y);
				Color.RGBtoHSB(JMTFImage.getRed(col),
						JMTFImage.getGreen(col),
						JMTFImage.getBlue(col), hsv);
				input.setPixel(x, y, Color.HSBtoRGB(hsv[0],
						Math.max(0f, Math.min(1f, hsv[1] * this.factor)),
						hsv[2]));
			}
		}

	}

}
