/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.background;

import jmtf.ImageSource;
import jmtf.JMTFImage;
import jmtf.manipulators.GaussianBlur;

/**
 * @author Luca Rossetto
 *
 */
public class BluredMedian3WayBackgroundSubtractor extends
		Median3WayBackgroundSubtractor {

	private GaussianBlur gb;
	
	public BluredMedian3WayBackgroundSubtractor(ImageSource source, int width,
			int height, int intervall, int threshold, int radius) {
		super(source, width, height, intervall, threshold);
		this.gb = new GaussianBlur(null, radius);
	}
	
	public BluredMedian3WayBackgroundSubtractor(ImageSource source, int width, int height){
		this(source, width, height, 30);
	}
	
	public BluredMedian3WayBackgroundSubtractor(ImageSource source, int width, int height, int intervall){
		this(source, width, height, intervall, 50, 10);
	}

	@Override
	public void manipulate(JMTFImage input) {
		if(this.counter++ % this.intervall == 0){
			updateBackground(input);
		}
		
		//int[] pixels_bg = this.background.getPixels();
		//int[] pixels_out = input.getPixels();
		
		JMTFImage blurInput = input.copy();
		
		this.gb.manipulate(blurInput);
		//int[] pixels = blurInput.getPixels();
		
		/*for(int i = 0; i < pixels_bg.length; ++i){
			int d = JMTFImage.squaredColorDistance(pixels[i], pixels_bg[i]);
			if(d < this.threshold){
				pixels_out[i] = 0;
			}
		}*/
		
		for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
			for (int y = input.getROI().minY; y <= input.getROI().maxY; ++y) {

				if(JMTFImage.squaredColorDistance(blurInput.getPixel(x, y), this.background.getPixel(x, y)) < this.threshold){
					input.setPixel(x, y, 0);
				}
				
			}
		}
	}

	@Override
	protected void updateBackground(JMTFImage input) {
		super.updateBackground(input);
		
		this.gb.manipulate(this.background);
	}
	
}
