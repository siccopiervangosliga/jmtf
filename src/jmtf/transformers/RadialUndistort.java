/**
 * Java Motion Tracking Framework
 */
package jmtf.transformers;

import java.util.Enumeration;

import jmtf.AbstractTrackingDataTransformer;
import jmtf.TrackingDataSet;
import jmtf.TrackingDataSource;

/**
 * Performs radial undistortion using a list of parameters k
 * D = r^2 * k1 + r^4 * k2 + ...
 * @author Luca Rossetto
 *
 */
public class RadialUndistort extends AbstractTrackingDataTransformer {

	private float[] k;
	
	/**
	 * @param source TrackingDataSource to use
	 * @param k list of parameters for undistortion
	 */
	public RadialUndistort(TrackingDataSource source, float... k) {
		super(source);
		this.k = k;
	}

	/* (non-Javadoc)
	 * @see jmtf.AbstractTrackingDataTransformer#transform(jmtf.TrackingDataSet)
	 */
	@Override
	public void transform(TrackingDataSet input) {
		if(input == null || input.tracks == null){
			return;
		}
		
		Enumeration<Integer> keys = input.tracks.keys();
		while(keys.hasMoreElements()){
			int key = keys.nextElement();
			int[] pos = input.tracks.get(key).getCenter();
			float r = (float) Math.sqrt(pos[0]*pos[0] + pos[1]*pos[1]), D = 0;
			int exp = 0;
			
			for(float k : this.k){
				D += (Math.pow(r, (exp += 2))*k);
			}
			
			float translate_x = D * pos[0];
			float translate_y = D * pos[1];
			
			input.tracks.get(key).translate(translate_x, translate_y);
			
		}

	}

}
