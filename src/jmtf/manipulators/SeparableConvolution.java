/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * Performs a separable convolution using a one dimensional kernel given as a float-array
 * @author Luca Rossetto
 *
 */
public class SeparableConvolution extends AbstractImageManipulator {

	private float[] kernel;
	
	/**
	 * 
	 * @param source the ImageSource to use
	 * @param kernel the convolution kernel
	 */
	public SeparableConvolution(ImageSource source, float[] kernel) {
		super(source);
		this.kernel = kernel;
	}
	
	

	/* (non-Javadoc)
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {

		int[] tmp = new int[input.getPixels().length];
		float[] vals = new float[3];
		int u, color;
		
		int kernel_size = this.kernel.length;
		
		for(int x = 0; x < input.getWidth(); ++x){				//rows
			for(int y = 0; y < input.getHeight(); ++y){			//cols
				vals[0] = 0; vals[1] = 0; vals[2] = 0;
				for(int i = 0; i < kernel_size; ++i){	//kernel
					
					u = x + i - kernel_size / 2;
					color = input.getPixel(u, y);
					vals[0] += JMTFImage.getRed(color) * this.kernel[i];
					vals[1] += JMTFImage.getGreen(color) * this.kernel[i];
					vals[2] += JMTFImage.getBlue(color) * this.kernel[i];
					
				}
				
				tmp[x + input.getWidth() * y] =JMTFImage.getColor(Math.max((int)vals[0], 0), Math.max((int)vals[1], 0), Math.max((int)vals[2], 0));
			}			
		}
		
		
		
		for(int x = 0; x < input.getWidth(); ++x){				
			for(int y = 0; y < input.getHeight(); ++y){			
				vals[0] = 0; vals[1] = 0; vals[2] = 0;
				for(int i = 0; i < kernel_size; ++i){	//kernel
					
						if((y + i - kernel_size / 2) < input.getHeight() && (y + i - kernel_size / 2) >= 0){
							color = tmp[(x + (y + i - kernel_size / 2)*input.getWidth())];
							
							vals[0] += JMTFImage.getRed(color) * this.kernel[i];
							vals[1] += JMTFImage.getGreen(color) * this.kernel[i];
							vals[2] += JMTFImage.getBlue(color) * this.kernel[i];
							
						}
						
					
				}
				
				input.setPixel(x, y,JMTFImage.getColor(Math.max((int)vals[0], 0), Math.max((int)vals[1], 0), Math.max((int)vals[2], 0)));
			}			
		}
		
		
		
	}

}
