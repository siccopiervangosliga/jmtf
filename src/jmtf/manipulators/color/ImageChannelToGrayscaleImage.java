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
public class ImageChannelToGrayscaleImage extends AbstractImageManipulator {

	public static final int RED = 0, GREEN = 1, BLUE = 2;

	private int channel;
	
	public ImageChannelToGrayscaleImage(ImageSource source, int channel) {
		super(source);
		this.channel = channel;
	}

	
	
	/* (non-Javadoc)
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		if(input == null){
			return;
		}
		
		for(int i = 0; i < input.getPixels().length; ++i){
			int c = 0;
			switch(this.channel){
			
			case RED:{
				c = JMTFImage.getRed(input.getPixels()[i]);
				break;
			}
			case GREEN:{
				c = JMTFImage.getGreen(input.getPixels()[i]);
				break;
			}
			case BLUE:{
				c = JMTFImage.getBlue(input.getPixels()[i]);
				break;
			}
			}
			input.getPixels()[i] = JMTFImage.getColor(c, c, c);
		}

	}

}
