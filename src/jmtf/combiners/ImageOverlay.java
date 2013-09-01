/**
 * Java Motion Tracking Framework
 */
package jmtf.combiners;

import jmtf.AbstractImageCombiner;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * overlays image from source 2 over the one from source 1 where image2 is not black
 * @author Luca Rossetto
 *
 */
public class ImageOverlay extends AbstractImageCombiner {

	public ImageOverlay(ImageSource source1, ImageSource source2) {
		super(source1, source2);
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
				
				if(c2 == 0){
					output.setPixel(x, y, c1);
				}else{
					output.setPixel(x, y, c2);
				}
				
			}
		}

	}

}
