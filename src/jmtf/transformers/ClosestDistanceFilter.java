/**
 * Java Motion Tracking Framework
 */
package jmtf.transformers;

import java.util.Enumeration;

import jmtf.AbstractTrackingDataTransformer;
import jmtf.TrackingDataSet;
import jmtf.TrackingDataSource;

/**
 * Keeps only the one blob in the tracking data set which was closest to the last one.
 * @author Luca Rossetto
 *
 */
public class ClosestDistanceFilter extends AbstractTrackingDataTransformer {

	private int[] pos;
	
	/**
	 * @param source TrackingDataSource from which to read
	 * @param initial_position the position where the object to keep is initially expected
	 */
	public ClosestDistanceFilter(TrackingDataSource source, int[] initial_position) {
		super(source);
		this.pos = initial_position;
	}

	/* (non-Javadoc)
	 * @see jmtf.AbstractTrackingDataTransformer#transform(jmtf.TrackingDataSet)
	 */
	@Override
	public void transform(TrackingDataSet input) {

		if(input == null || input.tracks == null || input.tracks.size() < 2){
			return;
		}
		
		int best_id = -1, best_dist = Integer.MAX_VALUE, key, distance;
		Enumeration<Integer> keys = input.tracks.keys();
		
		while(keys.hasMoreElements()){
			key = keys.nextElement();
			distance = distance(this.pos, input.tracks.get(key).getCenter());
			if(distance < best_dist){
				best_dist = distance;
				if(best_id != -1){
					input.tracks.remove(best_id);
				}
				best_id = key;
			}else{
				input.tracks.remove(key);
			}
		}
		this.pos = input.tracks.get(best_id).getCenter();

	}
	
	private static int distance(int[] pos1, int[] pos2){
		int x = pos1[0] - pos2[0], y = pos1[1] - pos2[1];
		return x*x + y*y;
	}

}
