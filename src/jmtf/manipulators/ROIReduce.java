/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * removes black areas from the ROI as far as possible
 * @author Luca Rossetto
 *
 */
public class ROIReduce extends AbstractImageManipulator {

	public ROIReduce(ImageSource source) {
		super(source);
	}

	/* (non-Javadoc)
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		
		int x, y;
		
		left:
			for(x = input.getROI().minX; x <= input.getROI().maxX; ++x){
				for(y = input.getROI().minY; y <= input.getROI().maxY; ++y){
					if(input.getPixel(x, y) != 0){
						break left;
					}
				}
			}
		input.getROI().minX = x;
		
		right:
			for(x = input.getROI().maxX; x >= input.getROI().minX; --x){
				for(y = input.getROI().maxY; y >= input.getROI().minY; --y){
					if(input.getPixel(x, y) != 0){
						break right;
					}
				}
			}
		input.getROI().maxX = x;
		
		top:
			for(y = input.getROI().minY; y < input.getROI().maxY; ++y){
				for(x = input.getROI().minX; x < input.getROI().maxX; ++x){
					if(input.getPixel(x, y) != 0){
						break top;
					}
				}
			}
		input.getROI().minY = y;
		
		bottom:
			for(y = input.getROI().maxY; y > input.getROI().minY; --y){
				for(x = input.getROI().maxX; x > input.getROI().minX; --x){
					if(input.getPixel(x, y) != 0){
						break bottom;
					}
				}
			}
		input.getROI().maxY = y;

	}

}
