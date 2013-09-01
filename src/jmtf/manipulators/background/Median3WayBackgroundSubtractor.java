/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.background;

import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * @author Luca Rossetto
 * Estimates Background using the median of three images taken in a specified interval.
 * The interval should correlate with the tramerate of the original input
 */
public class Median3WayBackgroundSubtractor extends AbstractBackgroundSubtractor {

	protected int intervall, threshold, counter;
	private JMTFImage img1, img2, img3;
	
	/**
	 * 
	 * @param source the {@link ImageSource} from which to read
	 * @param width the expected with of the image
	 * @param height the expected height of the image
	 * @param intervall the interval in which to update the background using the current input image and the last two used images
	 * @param threshold the threshold used for comparison of the input and the estimated background
	 */
	public Median3WayBackgroundSubtractor(ImageSource source, int width, int height, int intervall, int threshold) {
		super(source, width, height);
		this.intervall = intervall;
		this.threshold = threshold * threshold;
		this.counter = 0;
		this.img1 = new JMTFImage(this.width, this.height);
		this.img2 = new JMTFImage(this.width, this.height);
	}
	
	public Median3WayBackgroundSubtractor(ImageSource source, int width, int height){
		this(source, width, height, 30);
	}
	
	public Median3WayBackgroundSubtractor(ImageSource source, int width, int height, int intervall){
		this(source, width, height, intervall, 50);
	}

	/* (non-Javadoc)
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		if(this.counter++ % this.intervall == 0){
			updateBackground(input);
		}
		
		/*int[] pixels_bg = this.background.getPixels();
		int[] pixels = input.getPixels();
		
		for(int i = 0; i < pixels_bg.length; ++i){
			int d = JMTFImage.squaredColorDistance(pixels[i], pixels_bg[i]);
			if(d < this.threshold){
				pixels[i] = 0;
			}
		}*/
		
		for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
			for (int y = input.getROI().minY; y <= input.getROI().maxY; ++y) {

				if(JMTFImage.squaredColorDistance(input.getPixel(x, y), this.background.getPixel(x, y)) < this.threshold){
					input.setPixel(x, y, 0);
				}
				
			}
		}


	}
	
	protected void updateBackground(JMTFImage input){
		this.img3 = null;
		this.img3 = this.img2;
		this.img2 = this.img1;
		this.img1 = input.copy();
		
		int[] bgPixels = this.background.getPixels();
		
		for(int i = 0; i < bgPixels.length; ++i){
			bgPixels[i] = color3median(
					this.img3.getPixels()[i], 
					this.img2.getPixels()[i], 
					this.img1.getPixels()[i]);
		}
		this.background.setFrameNumber(counter / intervall);
		updateBackgroundListeners();
		
	}
	
	protected static int int3median(int a, int b, int c){
		int tmp = 0;
		if(a > b){tmp = a; a = b; b = tmp;}
		if(b > c){/*tmp = b;*/ b = c; /*c = tmp;*/}
		if(a > b){/*tmp = a;*/ /*a = b;*/ b = a;}
		return b;
	}
	
	protected static int color3median(int c1, int c2, int c3){
		int med = JMTFImage.getColor(
				int3median(JMTFImage.getRed(c1), JMTFImage.getRed(c2), JMTFImage.getRed(c3)),
				int3median(JMTFImage.getGreen(c1), JMTFImage.getGreen(c2), JMTFImage.getGreen(c3)),
				int3median(JMTFImage.getBlue(c1), JMTFImage.getBlue(c2), JMTFImage.getBlue(c3)));
		
		int d1 = JMTFImage.squaredColorDistance(c1, med), d2 = JMTFImage.squaredColorDistance(c2, med), d3 = JMTFImage.squaredColorDistance(c3, med);
		
		int dmed = int3median(d1, d2, d3);
		
		if(d1 == dmed){ return c1;}
		if(d2 == dmed){ return c2;}
		return c3;
	}

}
