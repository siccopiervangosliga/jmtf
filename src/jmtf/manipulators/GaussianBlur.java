/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators;

import jmtf.ImageSource;

/**
 * Performs a Gaussian Blur
 * @author Luca Rossetto
 *
 */
public class GaussianBlur extends SeparableConvolution {

	/**
	 * 
	 * @param source source image
	 * @param radius radius of the gaussian curve
	 */
	public GaussianBlur(ImageSource source, int radius) {
		
		super(source, pascal_row(radius));
	}
	
	private static float[] pascal_row(int row){
		
		float[] _ret = new float[row + 1];
		
		float divisor = (float) Math.pow(2, row);
		
		for(int k = 0; k < row / 2 + 1; ++k){
			
			_ret[k] = (float)(factorial(row)/(factorial(k)*factorial(row - k)))/divisor;
			_ret[row - k] = _ret[k];
		}
		
		
		
		return _ret;
	}
	
	/**
	 * calculates the factorial of n
	 * @param n
	 * @return n!
	 */
	private static double factorial(long n){
		if(n < 2){
			return 1;
		}
		for(long i = n-1; i > 1; --i){
			n *= i;
		}
		return n;
	}

}
