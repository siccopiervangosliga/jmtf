/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.color;

import java.awt.Color;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * @author Luca Rossetto
 *
 */
public class HueToImage extends AbstractImageManipulator {

	private float saturation, brightness;
	
	/**
	 * @param source
	 */
	public HueToImage(ImageSource source, float saturation, float brightness) {
		super(source);
		this.brightness = brightness;
		this.saturation = saturation;
	}

	/* (non-Javadoc)
	 * @see jmtf.ImageManipulator#manipulate(java.awt.image.BufferedImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		
		float h;
		
		for(int i = 0; i < input.getPixels().length; ++i){
			h = (input.getPixels()[i] & 0xFF) / 255f; //read only blue channel assuming the input image is grayscale
			
			input.getPixels()[i] = Color.getHSBColor(h, this.saturation, this.brightness).getRGB();
			
		}
		
		
	}

}
