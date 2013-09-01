/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.template;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * @author Luca Rossetto
 *
 */
public class TemplateMatcher extends AbstractImageManipulator {

	private JMTFImage template;
	private int offsetX, offsetY;
	
	public TemplateMatcher(ImageSource source, JMTFImage template) {
		super(source);
		this.template = template;
		this.offsetX = this.template.getWidth() / 2;
		this.offsetY = this.template.getHeight() / 2;
	}

	/* (non-Javadoc)
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		JMTFImage result = new JMTFImage(input.getWidth(), input.getHeight(), true);
		
		int minScore = Integer.MAX_VALUE, maxScore = 0;
		for (int x = input.getROI().minX; x <= input.getROI().maxX - this.template.getWidth(); ++x) {
			for (int y = input.getROI().minY; y <= input.getROI().maxY - this.template.getHeight(); ++y) {
		//for(int x = 0; x < input.getWidth() - this.template.getWidth(); ++x){
			//for(int y = 0; y < input.getHeight() - this.template.getHeight(); ++y){
				
				int score = 0;
				
				for(int xx = 0; xx < this.template.getWidth(); ++xx){
					for(int yy = 0; yy < this.template.getHeight(); ++yy){
						
						score += Math.sqrt(JMTFImage.squaredColorDistance(this.template.getPixel(xx, yy), input.getPixel(xx + x, yy + y))) + 1;
						
					}
				}
				
				result.setPixel(x + this.offsetX, y + this.offsetY, score);
				
				if(score < minScore){
					minScore = score;
				}
				if(score > maxScore){
					maxScore = score;
				}
				
			}
		}
		
		//normalise image
		float range = maxScore - minScore;
		int[] pixels = result.getPixels();
		for(int i = 0; i < pixels.length; ++i){
			if(pixels[i] == 0){
				continue;
			}
			
			float val = ((float)pixels[i] - minScore) / range;
			
			int col = (int) Math.round(255 - Math.pow(val, 0.2f) * 255);
			
			pixels[i] = JMTFImage.getColor(col, col, col);
			
		}
		
		input.setPixels(pixels);

	}

}
