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
public class LloydMaxQuantisation extends AbstractImageManipulator {

	private int K_r, K_g, K_b, keepForFrames, framecount = 0;
	private HistogramCalculator histCalc;
	private int[] z_k_r, z_k_g, z_k_b, q_k_r, q_k_g, q_k_b;


	public LloydMaxQuantisation(ImageSource source, int numerOfColorsPerRedChannel, int numerOfColorsPerGreenChannel, int numerOfColorsPerBlueChannel, int keepforFrames) {
		super(source);
		this.K_r = numerOfColorsPerRedChannel;
		this.K_g = numerOfColorsPerGreenChannel;
		this.K_b = numerOfColorsPerBlueChannel;
		this.keepForFrames = keepforFrames;
		this.histCalc = new HistogramCalculator(null);
		this.q_k_r = new int[this.K_r];
		this.q_k_g = new int[this.K_g];
		this.q_k_b = new int[this.K_b];
		this.z_k_r = new int[this.K_r + 1];
		this.z_k_g = new int[this.K_g + 1];
		this.z_k_b = new int[this.K_b + 1];
	}
	
	public LloydMaxQuantisation(ImageSource source, int numerOfColorsPerChannel, int keepforFrames){
		this(source, numerOfColorsPerChannel, numerOfColorsPerChannel, numerOfColorsPerChannel, keepforFrames);
	}
	
	public LloydMaxQuantisation(ImageSource source, int numerOfColorsPerChannel){
		this(source, numerOfColorsPerChannel, numerOfColorsPerChannel, numerOfColorsPerChannel, 1);
	}

	public LloydMaxQuantisation(ImageSource source){
		this(source, 32);
	}
	
	/* (non-Javadoc)
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		//reevaluate mapping
		if(this.framecount++ % this.keepForFrames == 0){
			
			this.histCalc.manipulate(input); //calculates new histogram
			
			//init z_k
			for(int i = 1; i < this.K_r; ++i){
				this.z_k_r[i] = (255/this.K_r)*i;
			}
			this.z_k_r[this.K_r] = 255;
			
			if(!input.isGrayscale()){
				for(int i = 1; i < this.K_g; ++i){
					this.z_k_g[i] = (255/this.K_g)*i;
				}
				this.z_k_g[this.K_g] = 255;
				
				for(int i = 1; i < this.K_b; ++i){
					this.z_k_b[i] = (255/this.K_b)*i;
				}
				this.z_k_b[this.K_b] = 255;
			}
			
			int last_error, error = Integer.MAX_VALUE;
			do{
				last_error = error;
				
				error = lmqIteration(this.z_k_r, this.q_k_r, this.histCalc.getRedHist(), this.K_r);
				
			}while(last_error > error);

			if(!input.isGrayscale()){
				error = Integer.MAX_VALUE;
				do{
					last_error = error;
					
					error = lmqIteration(this.z_k_g, this.q_k_g, this.histCalc.getGreenHist(), this.K_g);
					
				}while(last_error > error);
				error = Integer.MAX_VALUE;
				do{
					last_error = error;
					
					error = lmqIteration(this.z_k_b, this.q_k_b, this.histCalc.getBlueHist(), this.K_b);
					
				}while(last_error > error);
			}
			
		}
		
		int r = 0, g = 0, b = 0;
		//int[] pixels = new int[input.getPixels().length];
		//for(int i = 0; i < input.getPixels().length; ++i){
		for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
			for (int y = input.getROI().minY; y <= input.getROI().maxY; ++y) {
				
			
			int col = JMTFImage.getRed(input.getPixel(x, y));
			for(int j = 0; j < this.K_r; ++j){
				if(col >= z_k_r[j] && col <= z_k_r[j+1]){
					r = q_k_r[j];
					break;
				}
			}
			if(input.isGrayscale()){
				g = r;
				b = r;
			}else{
				for(int j = 0; j < this.K_g; ++j){
					if(col >= z_k_g[j] && col <= z_k_g[j+1]){
						g = q_k_g[j];
						break;
					}
				}
				
				for(int j = 0; j < this.K_b; ++j){
					if(col >= z_k_b[j] && col <= z_k_b[j+1]){
						b = q_k_b[j];
						break;
					}
				}
			}
			
			//pixels[i] = JMTFImage.getColor(r, g, b);
			input.setPixel(x, y, JMTFImage.getColor(r, g, b));
		}
		}
		
		//input.setPixels(pixels);

	}
	
	private int lmqIteration(int[] z_k, int[] q_k, int[] p, int K){
		
		//calculate q_k
		for(int k = 0; k < K; ++k){
			float divident = 0, divisor = 0;
			for(int z = z_k[k]; z <= z_k[k+1]; ++z){
				divident += (z*p[z]);
				divisor += p[z];
			}
			q_k[k] = Math.round(divident / divisor);
		}
		
		//calculate z_k
		for(int i = 1; i < K; ++i){
			z_k[i] = (q_k[i-1] + q_k[i])/2;
		}
		
		//calculate error
		int error = 0;
		for(int k = 0; k < K; ++k){
			for(int z = z_k[k]; z < z_k[k+1]; ++z){
				error += (z - q_k[k])*(z - q_k[k])*p[z];
			}
			
		}
		
		return error;
	}

}
