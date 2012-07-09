/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.color;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * @author Luca Rossetto
 *
 */
public class ColorMapper extends AbstractImageManipulator {

	private int[] colors;
	public static final int[] _16_COLORS = new int[]{0x000000, 0x800000, 0x008000, 0x808000, 0x000080, 0x800080, 0x008080, 0xC0C0C0, 0x808080, 0xFF0000, 0x00FF00, 0xFFFF00, 0x0000FF, 0xFF00FF, 0x00FFFF, 0xFFFFFF};

	public ColorMapper(ImageSource source, int[] colors) {
		super(source);
		this.colors = colors;
	}


	@Override
	public void manipulate(JMTFImage input) {

		int[] pixels = input.getPixels();
		for(int i = 0; i < pixels.length; ++i){
			int best_dist = Integer.MAX_VALUE, dist, best_color = 0;
			for(int color : this.colors){
				dist = JMTFImage.squaredColorDistance(color, pixels[i]);
				if(dist < best_dist){
					best_dist = dist;
					best_color = color;
				}
			}
			pixels[i] = best_color;
		}

	}

}
