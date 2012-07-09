/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.color;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * [r', g', b'] = A*[r, g, b]'
 * @author Luca Rossetto
 *
 */
public class ColorSpaceTransform extends AbstractImageManipulator {

	private float a11, a12, a13, a21, a22, a23, a31, a32, a33;
	
	/**
	 * @param source
	 */
	public ColorSpaceTransform(ImageSource source, float a11, float a12, float a13, float a21, float a22, float a23, float a31, float a32, float a33) {
		super(source);
		this.a11 = a11; this.a12 = a12; this.a13 = a13;
		this.a21 = a21; this.a22 = a22; this.a23 = a23;
		this.a31 = a31; this.a32 = a32; this.a33 = a33;
	}

	/* (non-Javadoc)
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		int[] pixels = input.getPixels();
		
		int r, g, b, r_, g_, b_;
		for(int i = 0; i < pixels.length; ++i){
			r = JMTFImage.getRed(pixels[i]);
			g = JMTFImage.getGreen(pixels[i]);
			b = JMTFImage.getBlue(pixels[i]);
			
			r_ = Math.max(Math.min((int)(this.a11*r + this.a12 * g + this.a13 * b), 255), 0);
			g_ = Math.max(Math.min((int)(this.a21*r + this.a22 * g + this.a23 * b), 255), 0);
			b_ = Math.max(Math.min((int)(this.a31*r + this.a32 * g + this.a33 * b), 255), 0);
			
			pixels[i] = JMTFImage.getColor(r_, g_, b_);
		}
		
		input.setPixels(pixels);
		input.setGrayscale(false);
	}

}
