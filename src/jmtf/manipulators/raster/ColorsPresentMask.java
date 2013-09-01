/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.raster;

import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * @author Luca Rossetto
 *
 */
public class ColorsPresentMask extends AbstractImageReRasterizer {


	private static final int WHITE = JMTFImage.getColor(255, 255, 255), BLACK = 0;
	
	private int threshold;
	private int[] col;

	
	public ColorsPresentMask(ImageSource source, int cellSize, int threshold, int...colors) {
		super(source, cellSize);
		this.threshold = threshold * threshold;
		this.col = colors;
	}


	/* (non-Javadoc)
	 * @see jmtf.manipulators.raster.AbstractImageReRasterizer#rerasterize(int[], int)
	 */
	@Override
	protected int rerasterize(int[] colors, int len) {
		
		boolean found = false;
		
		for(int checkCol : this.col){
			found = false;
			for(int i = 0; i < len; ++i){
				if(JMTFImage.squaredColorDistance(checkCol, colors[i]) <= this.threshold){
					found = true;
					break;
				}
			}
			if(!found){
				return BLACK;
			}
		}

		return WHITE;
	}


}
