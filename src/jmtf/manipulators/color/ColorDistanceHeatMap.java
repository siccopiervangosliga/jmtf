/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.color;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * Transforms the input image into a geayscale heatmap where the specified color
 * is white, a color with greater distance than threshold is black and the
 * distances in between get scaled linearly
 * 
 * @author Luca Rossetto
 * 
 */
public class ColorDistanceHeatMap extends AbstractImageManipulator {

	private int color;
	private float threshold;

	public ColorDistanceHeatMap(ImageSource source, int color, int threshold) {
		super(source);
		this.threshold = threshold;
		this.color = color;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		//int[] pixels = input.getPixels();
		//for (int i = 0; i < pixels.length; ++i) {
		for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
			for (int y = input.getROI().minY; y <= input.getROI().maxY; ++y) {
				double dist = Math.sqrt((float) JMTFImage.squaredColorDistance(
						this.color, input.getPixel(x, y)));
				if (dist > this.threshold) {
					input.setPixel(x, y, 0);
				} else {
					int col = (int) Math
							.round(((this.threshold - dist) / threshold) * 255f);
					input.setPixel(x, y, JMTFImage.getColor(col, col, col));
				}
	
			}
		}
		input.setGrayscale(true);
	}

}
