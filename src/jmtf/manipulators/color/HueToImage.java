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
	 * @param saturation
	 * @param brightness
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

		//for(int i = 0; i < input.getPixels().length; ++i){
		for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
			for (int y = input.getROI().minY; y <= input.getROI().maxY; ++y) {
			float h = (input.getPixel(x, y) & 0xFF) / 255f; //read only channel assuming the input image is grayscale
			
			input.setPixel(x, y, Color.getHSBColor(h, this.saturation, this.brightness).getRGB());			
		}
			}
		
		
	}

}
