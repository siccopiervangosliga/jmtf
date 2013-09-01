/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.background;

import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * @author Luca Rossetto
 *
 */
public class LearningBackgroundSubtractor extends AbstractBackgroundSubtractor {

	private static final int WHITE = JMTFImage.getColor(255, 255, 255);
	private static final float MAX_DIF = 3*255*255*10;

	protected int threshold;
	
	private JMTFImage model1, model2;
	private float[] model1_certainty, model2_certainty;
	
	

	public LearningBackgroundSubtractor(ImageSource source, int width, int height){
		this(source, width, height, 2500);
	}
	
	/**
	 * 
	 * @param source the {@link ImageSource} from which to read
	 * @param width the width of the expected image
	 * @param height the height of the expected image
	 * @param threshold the threshold used to update a model and distinguish the foreground from the background
	 */
	public LearningBackgroundSubtractor(ImageSource source, int width, int height, int threshold){
		super(source, width, height);

		this.model1 = new JMTFImage(this.width, this.height);
		this.model2 = new JMTFImage(this.width, this.height);
		this.model1_certainty = new float[width*height];
		this.model2_certainty = new float[width*height];
		this.threshold = threshold;
		int[] pixels = this.model2.getPixels();
		for(int i = 0; i < pixels.length; ++i){
			pixels[i] = LearningBackgroundSubtractor.WHITE;
		}
		this.model2.setPixels(pixels);
	}

	/* (non-Javadoc)
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {

		updateBackground(input);
		
		//int[] pixels = input.getPixels();
		//int[] pixels_bg = this.background.getPixels();
		
		/*for(int i = 0; i < pixels.length; ++i){
			if(JMTFImage.squaredColorDistance(pixels[i], pixels_bg[i]) < this.threshold){
				pixels[i] = 0;
			}
		}
		
		input.setPixels(pixels);*/
		
		for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
			for (int y = input.getROI().minY; y <= input.getROI().maxY; ++y) {

				if(JMTFImage.squaredColorDistance(input.getPixel(x, y), this.background.getPixel(x, y)) < this.threshold){
					input.setPixel(x, y, 0);
				}
				
			}
		}
		
	}
	
	protected void updateBackground(JMTFImage input){
		JMTFImage certainModel, uncertainModel;
		float[] goodCertainty, badCertainty;
		int dist1, dist2, color, pos;
		for(int x = 0; x < input.getWidth(); ++x){
			for(int y = 0; y < input.getHeight(); ++y){
				color = input.getPixel(x, y);
				pos = x + this.width * y;
								
				if(this.model1_certainty[pos] > this.model2_certainty[pos]){
					certainModel = this.model1;
					uncertainModel = this.model2;
					goodCertainty = this.model1_certainty;
					badCertainty = this.model2_certainty;
				}else{
					certainModel = this.model2;
					uncertainModel = this.model1;
					goodCertainty = this.model2_certainty;
					badCertainty = this.model1_certainty;
				}
				
				dist1 = JMTFImage.squaredColorDistance(certainModel.getPixel(x, y), color);
				dist2 = JMTFImage.squaredColorDistance(uncertainModel.getPixel(x, y), color);
				
				if(dist1 < this.threshold){
					goodCertainty[pos] += (1.0f / (float)(dist1 + 1));
				}else{
					goodCertainty[pos] *= Math.max(0.0f, (0.9f - (dist1-this.threshold) / MAX_DIF));
				}
				
				
				if(dist2 < this.threshold){
					badCertainty[pos] += (1.0f / (float)(dist2 + 1));
				}else{
					uncertainModel.setPixel(x, y, color);
					badCertainty[pos] = 1.0f;
				}
			}
		}
		
		int[] pixels_bg = this.background.getPixels();
		
		for(int i = 0; i < pixels_bg.length; ++i){
			if((this.model1_certainty[i] > this.model2_certainty[i])){
				pixels_bg[i] = this.model1.getPixels()[i];
			}else{
				pixels_bg[i] = this.model2.getPixels()[i];
			}
		}
		
		this.background.setPixels(pixels_bg);
		this.background.setFrameNumber(input.getFrameNumber());
		this.updateBackgroundListeners();
	}
	
}
