/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.color;

import jmtf.AbstractSpacialImageFunction;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * @author Luca Rossetto
 *
 */
public class ColorMax extends AbstractSpacialImageFunction{

	
	public ColorMax(ImageSource source, int size) {
		super(source, size);
	}

	/* (non-Javadoc)
	 * @see jmtf.AbstractSpacialImageFunction#func(int[], int)
	 */
	@Override
	protected int func(int[] colors, int len) {
		int bestCol = colors[0];
		int maxDist = JMTFImage.squaredColorDistance(0, bestCol);
		int dist = 0;
		for(int i = 1; i < len; ++i){
			if((dist = JMTFImage.squaredColorDistance(0, colors[i])) > maxDist){
				maxDist = dist;
				bestCol = colors[i];
			}
		}
		return bestCol;
	}

}
