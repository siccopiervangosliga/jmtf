/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators;

import java.util.ArrayList;
import java.util.Iterator;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.ImageUpdateListener;
import jmtf.JMTFImage;

/**
 * @author Luca Rossetto
 *
 */
public class BackgroundSubtractor extends AbstractImageManipulator {

	private static final int WHITE = JMTFImage.getColor(255, 255, 255);
	private static final int MAX_DIF = 3*255*255*10;

	private int width, height;
	
	private int threshold;
	
	private JMTFImage model1, model2, background;
	private float[] model1_certainty, model2_certainty;
	
	private ArrayList<ImageUpdateListener> listeners = new ArrayList<ImageUpdateListener>();

	public BackgroundSubtractor(ImageSource source, int width, int height){
		this(source, width, height, 2500);
	}
	
	public BackgroundSubtractor(ImageSource source, int width, int height, int threshold){
		super(source);
		this.width = width;
		this.height = height;
		this.model1 = new JMTFImage(this.width, this.height);
		this.model2 = new JMTFImage(this.width, this.height);
		this.background = new JMTFImage(this.width, this.height);
		this.model1_certainty = new float[width*height];
		this.model2_certainty = new float[width*height];
		this.threshold = threshold;
		int[] pixels = this.model2.getPixels();
		for(int i = 0; i < pixels.length; ++i){
			pixels[i] = BackgroundSubtractor.WHITE;
		}
		this.model2.setPixels(pixels);
	}

	/* (non-Javadoc)
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {

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
					goodCertainty[pos] *= Math.max(0.0f, (0.9f - (float)((dist1-this.threshold)/MAX_DIF)));
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
		int[] pixels = input.getPixels();
		for(int i = 0; i < pixels_bg.length; ++i){
			if((this.model1_certainty[i] > this.model2_certainty[i])){
				pixels_bg[i] = this.model1.getPixels()[i];
			}else{
				pixels_bg[i] = this.model2.getPixels()[i];
			}
			
			if(JMTFImage.squaredColorDistance(pixels[i], pixels_bg[i]) < this.threshold){
				pixels[i] = 0;
			}
		}
		this.background.setPixels(pixels_bg);
		this.background.setFrameNumber(input.getFrameNumber());
		input.setPixels(pixels);
		this.updateBackgroundListeners();
	}
	
	public void addBackgroundUpdateListener(ImageUpdateListener listener){
		this.listeners.add(listener);
	}
	
	public void removeBackgroundUpdateListener(ImageUpdateListener listener){
		this.listeners.remove(listener);
	}
	
	private void updateBackgroundListeners(){
		Iterator<ImageUpdateListener> iter = this.listeners.iterator();
		while(iter.hasNext()){
			iter.next().update(this.background);
		}
	}
	
}
