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
public class AvgReRasterizer extends AbstractImageReRasterizer {

	public AvgReRasterizer(ImageSource source, int cellDiameter) {
		super(source, cellDiameter);
	}

	/* (non-Javadoc)
	 * @see jmtf.manipulators.raster.AbstractImageReRasterizer#rerasterize(int[], int)
	 */
	@Override
	protected int rerasterize(int[] colors, int len) {

		float rs = 0, gs = 0, bs = 0;
		
		for(int i = 0; i < len; ++i){
			rs += JMTFImage.getRed(colors[i]);
			gs += JMTFImage.getRed(colors[i]);
			bs += JMTFImage.getRed(colors[i]);
		}
		
		int avgCol = JMTFImage.getColor(Math.round(rs / len), Math.round(gs / len), Math.round(bs / len));
		
		int bestId = 0, bestDist = JMTFImage.squaredColorDistance(avgCol, colors[0]), dist;
		
		for(int i = 1; i < len; ++i){
			dist = JMTFImage.squaredColorDistance(avgCol, colors[i]);
			if(dist == 0){
				return colors[i];
			}else if(dist < bestDist){
				bestDist = dist;
				bestId = i;
			}
		}
		return colors[bestId];
	}

}
