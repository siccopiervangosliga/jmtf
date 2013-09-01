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
public class MaskingTemplateMatcher extends AbstractImageManipulator {

	private JMTFImage template;
	private int offsetX, offsetY;
	private static final int WHITE = 0x00FFFFFF;
	
	public MaskingTemplateMatcher(ImageSource source, JMTFImage template) {
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
		int minScore = Integer.MAX_VALUE, bestX = -1, bestY = -1;
		
		for (int x = input.getROI().minX; x <= input.getROI().maxX - this.template.getWidth(); ++x) {
			for (int y = input.getROI().minY; y <= input.getROI().maxY - this.template.getHeight(); ++y) {
		//for(int x = 0; x < input.getWidth() - this.template.getWidth(); ++x){
		//	for(int y = 0; y < input.getHeight() - this.template.getHeight(); ++y){
				
				int score = 0;
				
				for(int xx = 0; xx < this.template.getWidth(); ++xx){
					for(int yy = 0; yy < this.template.getHeight(); ++yy){
						
						score += Math.sqrt(JMTFImage.squaredColorDistance(this.template.getPixel(xx, yy), input.getPixel(xx + x, yy + y))) + 1;
						
					}
				}
				
				if(score < minScore){
					minScore = score;
					bestX = x;
					bestY = y;
				}
				
				
			}
		}
		
		//generate mask
		JMTFImage result = new JMTFImage(input.getWidth(), input.getHeight(), true);
		for(int x = 0; x < this.template.getWidth(); ++x){
			for(int y = 0; y < this.template.getHeight(); ++y){
				result.setPixel(x + bestX, y + bestY, WHITE);
			}
		}
		
		input.getROI().minX = bestX;
		input.getROI().minY = bestY;
		input.getROI().maxX = bestX + template.getWidth() - 1;
		input.getROI().maxY = bestY + template.getHeight() - 1;
		
		input.setPixels(result.getPixels());
		input.setGrayscale(true);

	}

}
