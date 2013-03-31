/**
 * Java Motion Tracking Framework
 */
package jmtf.transformers;

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
public class SimpleBlobClusterer extends AbstractTrackingDataTransformer {

	private int squaredDistance;
	
	/**
	 * 
	 * @param source {@link TrackingDataSource} source
	 * @param distance distance in pixel in which to consider the blob belonging to cluster
	 */
	public SimpleBlobClusterer(TrackingDataSource source, int distance) {
		super(source);
		this.squaredDistance = distance * distance;
	}

	/* (non-Javadoc)
	 * @see jmtf.AbstractTrackingDataTransformer#transform(jmtf.TrackingDataSet)
	 */
	@Override
	public void transform(TrackingDataSet input) {
		
		if(input == null){
			return;
		}
		
		ConcurrentHashMap<Integer, Blob> new_tracks = new ConcurrentHashMap<Integer, Blob>();
		int keyCounter = 0;
		
		Set<Integer> keys = input.tracks.keySet();
		
		for(int key : keys){
			int minDistance = Integer.MAX_VALUE;
			int minKey = -1;
			Blob b = input.tracks.get(key);
			
			Set<Integer> new_keys = new_tracks.keySet();
			for(int new_key : new_keys){
				Blob b2 = new_tracks.get(new_key);
				int distance = squaredDistance(b, b2);
				if(distance < minDistance){
					minDistance = distance;
					minKey = new_key;
				}
			}
			
			if(minKey != -1 && minDistance <= this.squaredDistance){
				((ClusterBlob)new_tracks.get(minKey)).addBlobs(b);
			}else{
				new_tracks.put(keyCounter++, new ClusterBlob(b));
			}
			
		}
		
		input.tracks = new_tracks;

	}
	
	private int squaredDistance(Blob b1, Blob b2){
		int[] cb1 = b1.getCenter(), cb2 = b2.getCenter();
		return (cb1[0] - cb2[0]) * (cb1[0] - cb2[0]) + (cb1[1] - cb2[1]) * (cb1[1] - cb2[1]);
	}

}
