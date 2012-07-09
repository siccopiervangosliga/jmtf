/**
 * Java Motion Tracking Framework
 */
package jmtf.transformers;

import java.util.Enumeration;

import jmtf.AbstractTrackingDataTransformer;
import jmtf.JMTFImage;
import jmtf.TrackingDataSet;
import jmtf.TrackingDataSource;

/**
 * Removes all blobs from a tracking data set except the one with the highest pixel intensity at its centre.
 * @author Luca Rossetto
 *
 */
public class BrightestBlobFilter extends AbstractTrackingDataTransformer {


	public BrightestBlobFilter(TrackingDataSource source) {
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
		
		Enumeration<Integer> keys = input.tracks.keys();
		int val, best_val = -1, best_key = -1, key, pos[];
		while(keys.hasMoreElements()){
			key = keys.nextElement();
			pos = input.tracks.get(key).getCenter();
			val = JMTFImage.getRed(input.image.getPixel(pos[0], pos[1]));
			if(val > best_val){
				if(best_key != -1){
					input.tracks.remove(best_key);
				}
				best_key = key;
				best_val = val;
			}else{
				input.tracks.remove(key);
			}
		}

	}

}
