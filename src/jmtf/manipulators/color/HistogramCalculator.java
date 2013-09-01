/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.color;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;
import jmtf.manipulators.IntensityMap;

/**
 * Calculates the histogram of every color channel of the image. does not alter
 * the input image.
 * 
 * @author Luca Rossetto
 * 
 */
public class HistogramCalculator extends AbstractImageManipulator implements
		IntensityMap {

	public static final int RETURN_RED = 0, RETURN_GREEN = 1, RETURN_BLUE = 2;

	private int[] hist_red = new int[256], hist_green = new int[256],
			hist_blue = new int[256];
	private int select_return = 0;

	public HistogramCalculator(ImageSource source) {
		this(source, RETURN_RED);
	}

	public HistogramCalculator(ImageSource source, int select_return) {
		super(source);
		if (select_return < 3 && select_return > -1) {
			this.select_return = select_return;
		} else {
			this.select_return = 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		for (int i = 0; i < 256; ++i) {
			this.hist_red[i] = 0;
			this.hist_green[i] = 0;
			this.hist_blue[i] = 0;
		}

		// int[] pixels = input.getPixels();

		// for(int pixel : pixels){
		int pixel;
		for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
			for (int y = input.getROI().minY; y <= input.getROI().maxY; ++y) {

				pixel = input.getPixel(x, y);

				this.hist_red[JMTFImage.getRed(pixel)]++;
				this.hist_green[JMTFImage.getGreen(pixel)]++;
				this.hist_blue[JMTFImage.getBlue(pixel)]++;
			}
		}

	}

	public int[] getRedHist() {
		return this.hist_red;
	}

	public int[] getGreenHist() {
		return this.hist_green;
	}

	public int[] getBlueHist() {
		return this.hist_blue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jmtf.manipulators.IntensityMap#getMap()
	 */
	@Override
	public int[] getMap() {
		return this.getMap(this.select_return);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jmtf.manipulators.IntensityMap#getMap(int)
	 */
	@Override
	public int[] getMap(int selector) {
		switch (selector) {
		case RETURN_RED: {
			return this.getRedHist();
		}
		case RETURN_GREEN: {
			return this.getGreenHist();
		}
		case RETURN_BLUE: {
			return this.getBlueHist();
		}
		default: {
			return this.getRedHist();
		}
		}
	}

}
