/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * @author Luca Rossetto
 *
 */
public class NoiseRemover extends AbstractImageManipulator {

	private int radius, tolerance, iterations;
	
	/**
	 * 
	 * @param source
	 * @param radius
	 * @param tolerance
	 */
	public NoiseRemover(ImageSource source, int radius, int tolerance) {
		this(source, radius, tolerance, 1);
	}
	
	/**
	 * 
	 * @param source
	 * @param radius
	 * @param tolerance
	 * @param iterations
	 */
	public NoiseRemover(ImageSource source, int radius, int tolerance, int iterations){
		super(source);
		this.radius = radius;
		this.tolerance = tolerance;
		this.iterations = iterations > 0 ? iterations : 1;
	}

	/* (non-Javadoc)
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		
		if(input == null){
			return;
		}
		
		int[] in = input.getPixels();
		
		
		int[] out = null;
		
	
		for (int i = 0; i < this.iterations; i++) {
			out = in.clone();
			for (int x = 0; x < input.getWidth(); ++x) {
				for (int y = 0; y < input.getHeight(); ++y) {

					if ((in[x + y * input.getWidth()] & 0xffffff) == 0) {
						continue;
					}

					int count = -1; //start with -1 to compensate for counting actual point

					for (int xt = Math.max(0, x - this.radius); xt <= Math.min(
							input.getWidth()-1, x + this.radius); ++xt) {
						for (int yt = Math.max(0, y - this.radius); yt <= Math
								.min(input.getHeight()-1, y + this.radius); ++yt) {
							if ((in[xt + yt * input.getWidth()] & 0xffffff) != 0) {
								++count;
							}
						}
					}

					if (count < this.tolerance) {
						out[x + y * input.getWidth()] = 0;
					}

				}
			}
			in = out;
		}
	
		
		input.setPixels(out);

	}

}
