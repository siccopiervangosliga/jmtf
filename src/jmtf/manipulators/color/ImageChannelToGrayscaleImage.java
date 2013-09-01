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
		
		for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
			for (int y = input.getROI().minY; y <= input.getROI().maxY; ++y) {
			int c = 0;
			switch(this.channel){
			
			case RED:{
				c = JMTFImage.getRed(input.getPixel(x, y));
				break;
			}
			case GREEN:{
				c = JMTFImage.getGreen(input.getPixel(x, y));
				break;
			}
			case BLUE:{
				c = JMTFImage.getBlue(input.getPixel(x,y));
				break;
			}
			}
			input.setPixel(x, y, JMTFImage.getColor(c, c, c));
		}
		}

	}

}
