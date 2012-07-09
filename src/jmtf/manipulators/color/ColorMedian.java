/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.color;


import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * Replaces each pixel with the median value from the box with 2*size+1 diameter
 * @author Luca Rossetto
 *
 */
public class ColorMedian extends AbstractImageManipulator {

	private int size;

	public ColorMedian(ImageSource source, int size) {
		super(source);
		this.size = size;
	}


	/* (non-Javadoc)
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		if(input == null){
			return;
		}

		JMTFImage output = new JMTFImage(input.getWidth(), input.getHeight());
		
		int[] colors = new int[4*this.size*this.size + 2*this.size + 1]; //(size + 1)^2
		int len, i;
		for(int x = 0; x < input.getWidth(); ++x){
			for(int y = 0; y < input.getHeight(); ++y){
				
				len = (Math.min(input.getWidth(), x + size) - Math.max(0, x - this.size)) * (Math.min(input.getHeight(), y + size) - Math.max(0, y - this.size));
				i = 0;
				
				for(int x1 = Math.max(x - this.size, 0); x1 < Math.min(x + this.size, input.getWidth()); ++x1){
					for(int y1 = Math.max(y - this.size, 0); y1 < Math.min(y + this.size, input.getHeight()); ++y1){
						colors[i++] = input.getPixel(x1, y1);
					}
				}
				output.setPixel(x, y, median(colors, len));
			}
		}
		input.setPixels(output.getPixels());
	}
	
	
	private static int median(int[] colors, int len){
		
		int[] hist_red = new int[256], hist_green = new int[256], hist_blue = new int[256];
		
		for(int i = 0; i < len; ++i){
			hist_red[JMTFImage.getRed(colors[i])]++;
			hist_green[JMTFImage.getGreen(colors[i])]++;
			hist_blue[JMTFImage.getBlue(colors[i])]++;
		}
		
		int med = JMTFImage.getColor(median_from_hist(hist_red), median_from_hist(hist_green), median_from_hist(hist_blue));
	
		int dist, best_dist = Integer.MAX_VALUE, best_color = 0;
		for(int i = 0; i < len; ++i){
			dist = JMTFImage.squaredColorDistance(med, colors[i]);
			if(dist < best_dist){
				if(dist == 0){
					return colors[i];
				}
				best_color = colors[i];
				best_dist = dist;
			}
		}
		return best_color;
	}
	
	private static int median_from_hist(int[] hist){
		int pos_l = 0, pos_r = hist.length - 1;
		int sum_l = hist[pos_l], sum_r = hist[pos_r];
		
		while(pos_l < pos_r){
			if(sum_l < sum_r){
				sum_l += hist[++pos_l];
			}else{
				sum_r += hist[--pos_r];
			}
		}
		return pos_l;
	}
}
