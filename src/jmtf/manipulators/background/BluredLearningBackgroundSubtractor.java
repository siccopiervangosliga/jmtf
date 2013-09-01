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
public class BluredLearningBackgroundSubtractor extends
		LearningBackgroundSubtractor {

	private GaussianBlur gb;

	public BluredLearningBackgroundSubtractor(ImageSource source, int width,
			int height){
		super(source, width, height);
		this.gb = new GaussianBlur(null, 10);
	}
	
	public BluredLearningBackgroundSubtractor(ImageSource source, int width,
			int height, int threshold) {
		this(source, width, height, threshold, 10);
	}
	
	public BluredLearningBackgroundSubtractor(ImageSource source, int width,
			int height, int threshold, int blurradius) {
		super(source, width, height, threshold);
		this.gb = new GaussianBlur(null, blurradius);
	}

	@Override
	public void manipulate(JMTFImage input) {

		updateBackground(input);
		
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

				if(JMTFImage.squaredColorDistance(input.getPixel(x, y), this.background.getPixel(x, y)) < this.threshold){
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
