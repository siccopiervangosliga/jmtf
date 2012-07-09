/**
 * Java Motion Tracking Framework
 */
package jmtf.combiners;

import jmtf.AbstractImageCombiner;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * @author Luca Rossetto
 * Calculates a * source1 * source2
 */
public class ImageMultiplier extends AbstractImageCombiner {

	private float a;
	
	public ImageMultiplier(ImageSource source1, ImageSource source2, float a) {
		super(source1, source2);
		this.a = a;
	}

	/* (non-Javadoc)
	 * @see jmtf.AbstractImageCombiner#combine(jmtf.JMTFImage, jmtf.JMTFImage, jmtf.JMTFImage)
	 */
	@Override
	public void combine(JMTFImage input1, JMTFImage input2, JMTFImage output) {
		for(int x = 0; x < output.getWidth(); ++x){
			for(int y = 0; y < output.getHeight(); ++y){
				int c1 = input1.getPixel(x, y);
				int c2 = input2.getPixel(x, y);
				
				int r = Math.max(0, Math.min(255, (int)((this.a * (JMTFImage.getRed(c1)/255f) * (JMTFImage.getRed(c2)/255f))*255f)));
				int g = Math.max(0, Math.min(255, (int)((this.a * (JMTFImage.getGreen(c1)/255f) * (JMTFImage.getGreen(c2)/255f))*255f)));
				int b = Math.max(0, Math.min(255, (int)((this.a * (JMTFImage.getBlue(c1)/255f) * (JMTFImage.getBlue(c2)/255f))*255f)));
				
				output.setPixel(x, y, JMTFImage.getColor(r, g, b));
			}
		}

	}

}
