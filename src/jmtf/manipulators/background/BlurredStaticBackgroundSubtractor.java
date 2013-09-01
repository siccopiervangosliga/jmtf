/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.background;

import jmtf.ImageSource;
import jmtf.JMTFImage;
import jmtf.manipulators.GaussianBlur;

/**
 * uses a supplied image as background
 * 
 * @author Luca Rossetto
 * 
 */
public class BlurredStaticBackgroundSubtractor extends
		AbstractBackgroundSubtractor {

	private int threshold;
	private GaussianBlur gb;

	public BlurredStaticBackgroundSubtractor(ImageSource source,
			JMTFImage background, int threshold, int radius) {
		super(source, background.getWidth(), background.getHeight());
		this.background = background.copy();
		this.threshold = threshold * threshold;
		this.gb = new GaussianBlur(null, radius);
		this.gb.manipulate(this.background);
	}

	public BlurredStaticBackgroundSubtractor(ImageSource source,
			JMTFImage background, int threshold) {
		this(source, background, threshold, 10);
	}

	public BlurredStaticBackgroundSubtractor(ImageSource source,
			JMTFImage background) {
		this(source, background, 50);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		//int[] pixels_bg = this.background.getPixels();
		//int[] pixels_out = input.getPixels();

		JMTFImage blurInput = input.copy();

		this.gb.manipulate(blurInput);
		//int[] pixels = blurInput.getPixels();

		/*
		 * for(int i = 0; i < pixels_bg.length; ++i){ int d =
		 * JMTFImage.squaredColorDistance(pixels[i], pixels_bg[i]); if(d <
		 * this.threshold){ pixels_out[i] = 0; } }
		 */

		for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
			for (int y = input.getROI().minY; y <= input.getROI().maxY; ++y) {

				if(JMTFImage.squaredColorDistance(blurInput.getPixel(x, y), this.background.getPixel(x, y)) < this.threshold){
					input.setPixel(x, y, 0);
				}
				
			}
		}

	}

}
