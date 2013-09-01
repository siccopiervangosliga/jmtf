/**
 * Java Motion Tracking Framework
 */
package jmtf.transformers;

import java.util.Enumeration;

import jmtf.AbstractTrackingDataTransformer;
import jmtf.TrackingDataSet;
import jmtf.TrackingDataSource;
import jmtf.tracker.Blob;

/**
 * @author Luca Rossetto
 *
 */
public class RoundestBlobFilter extends AbstractTrackingDataTransformer {

	private static final double coeff = 4.0 / Math.PI;
	
	/**
	 * @param source
	 */
	public RoundestBlobFilter(TrackingDataSource source) {
		super(source);
	}

	/* (non-Javadoc)
	 * @see jmtf.AbstractTrackingDataTransformer#transform(jmtf.TrackingDataSet)
	 */
	@Override
	public void transform(TrackingDataSet input) {
		if(input == null || input.image == null || input.tracks == null || input.tracks.size() < 2){
			return;
		}
		
		double best_dist = 99999.9;
		int key, hist[], best_key = -1;
		Enumeration<Integer> keys = input.tracks.keys();
		Blob b;
		while(keys.hasMoreElements()){
			key = keys.nextElement();
			b = input.tracks.get(key);
			
			hist = b.getHistogram();
			int cum = 0;
			boolean remove = true;
			for(int i = 1; i < hist.length; ++i){
				cum += hist[i];
				if(cum == 0){
					continue;
				}
				double a = b.getWidth() * b.getHeight();
				double d = Math.abs((a / cum) - coeff);
				
				if(d < best_dist){
					remove = false;
					best_dist = d;
				}
			}
			
			
			
			if(!remove){
				if(best_key != -1){
					input.tracks.remove(best_key);
				}
				best_key = key;
			}else{
				input.tracks.remove(key);
			}
		}

	}

}
