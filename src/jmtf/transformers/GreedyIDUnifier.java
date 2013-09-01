/**
 * Java Motion Tracking Framework
 */
package jmtf.transformers;

import java.util.Enumeration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jmtf.AbstractTrackingDataTransformer;
import jmtf.TrackingDataSet;
import jmtf.TrackingDataSource;
import jmtf.tracker.Blob;

/**
 * @author Luca Rossetto
 *
 */
public class GreedyIDUnifier extends AbstractTrackingDataTransformer {

	private TrackingDataSet lastSet;

	public GreedyIDUnifier(TrackingDataSource source) {
		super(source);
	}

	/* (non-Javadoc)
	 * @see jmtf.AbstractTrackingDataTransformer#transform(jmtf.TrackingDataSet)
	 */
	@Override
	public void transform(TrackingDataSet input) {
		
		if(input == null){
			return;
		}
		
		if(lastSet != null){
			
			ConcurrentHashMap<Integer, Blob> new_tracks = new ConcurrentHashMap<Integer, Blob>();
			int biggestId = 0;
			
			
			Set<Integer> keys = lastSet.tracks.keySet();
			for(int key : keys){
				if(input.tracks.isEmpty()){
					break;
				}
				
				Blob b = lastSet.tracks.get(key);
				
				int minDistance = Integer.MAX_VALUE, distance;
				int minKey = -1;
				
				Set<Integer> currentKeys = input.tracks.keySet();
				for(int currentKey : currentKeys){
					distance = squaredDistance(b, input.tracks.get(currentKey));
					if(distance < minDistance){
						minDistance = distance;
						minKey = currentKey;
					}
				}
				
				new_tracks.put(key, input.tracks.remove(minKey));
				biggestId = Math.max(biggestId, key);
			}
			if(!input.tracks.isEmpty()){
				Enumeration<Blob> remaining = input.tracks.elements();
				while(remaining.hasMoreElements()){
					new_tracks.put(++biggestId, remaining.nextElement());
				}
			}
			
			input.tracks = new_tracks;
		}
		lastSet = input;
	}
	
	private static int squaredDistance(Blob b1, Blob b2){
		int[] cb1 = b1.getCenter(), cb2 = b2.getCenter();
		return (cb1[0] - cb2[0]) * (cb1[0] - cb2[0]) + (cb1[1] - cb2[1]) * (cb1[1] - cb2[1]);
	}

}
