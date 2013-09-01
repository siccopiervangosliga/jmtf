/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.color;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * removes all pixels where the maximum value is not in the specified channel
 * 
 * @author Luca Rossetto
 * 
 */
public class DominantChannelCutout extends AbstractImageManipulator {

	public static final int RED = 0, GREEN = 1, BLUE = 2, NOT_RED = 3, NOT_GREEN = 4, NOT_BLUE = 5;

	private int dominantChannel;
	private boolean invert = false;

	public DominantChannelCutout(ImageSource source, int dominantChannel) {
		super(source);
		this.dominantChannel = dominantChannel;
		if(this.dominantChannel > 2){
			this.dominantChannel -= 3;
			this.invert = true;
		}
		if (this.dominantChannel < 0 || this.dominantChannel > 5) {
			throw new IllegalArgumentException("Unknown channel "
					+ this.dominantChannel);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		// int[] pixels = input.getPixels();
		int r, g, b, col;

		// for(int i = 0; i < pixels.length; ++i){
		for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
			for (int y = input.getROI().minY; y <= input.getROI().maxY; ++y) {
				col = input.getPixel(x, y);
				r = JMTFImage.getRed(col);
				g = JMTFImage.getGreen(col);
				b = JMTFImage.getBlue(col);

				if ((max(r, g, b) != this.dominantChannel) ^ this.invert) {
					input.setPixel(x, y, 0);
				}
			}
		}

	}

	private int max(int v1, int v2, int v3) {
		int max = Math.max(v1, Math.max(v2, v3));
		if (v1 == max && v2 != max && v3 != max) {
			return 0;
		}
		if (v1 != max && v2 == max && v3 != max) {
			return 1;
		}
		if (v1 != max && v2 != max && v3 == max) {
			return 2;
		}
		return -1;
	}

}
