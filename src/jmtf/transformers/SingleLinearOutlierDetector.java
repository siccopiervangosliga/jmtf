/**
 * Java Motion Tracking Framework
 */
package jmtf.transformers;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import jmtf.AbstractTrackingDataBuffer;
import jmtf.TrackingDataSet;
import jmtf.TrackingDataSource;
import jmtf.tracker.Blob;

/**
 * Eliminates a tracking data set when it contains an outlier. A point is regarded as outlier when it travels its distance to its surrounding points is greater than a specified threshold.
 * This filter assumes that there is only one blob per tracking data set. In case of multiple ones, the first one is taken.
 * @author Luca Rossetto
 *
 */
public class SingleLinearOutlierDetector extends AbstractTrackingDataBuffer {

	private LinkedList<TrackingDataSet> tempQueue;
	private int last_frame = -1;
	private int max_dist;
	
	/**
	 * 
	 * @param source the TrackingDataSource from which to read
	 * @param maximal_distance the maxinal distance in pixel a blob can jump back and forth within one frame
	 */
	public SingleLinearOutlierDetector(TrackingDataSource source, int maximal_distance) {
		super(source);
		this.tempQueue = new LinkedList<TrackingDataSet>();
		this.max_dist = maximal_distance * maximal_distance;
	}

	/* (non-Javadoc)
	 * @see jmtf.AbstractTrackingDataBuffer#updateBuffer(jmtf.TrackingDataSet)
	 */
	@Override
	public void updateBuffer(TrackingDataSet input) {
		if(input == null || input.tracks == null || input.image == null){
			while(!this.tempQueue.isEmpty()){
				this.trackingDataqueue.offer(this.tempQueue.poll());
			}
			this.trackingDataqueue.offer(null);
			return;
		}

		
		if(input.image.getFrameNumber() - 1 != this.last_frame || input.tracks.isEmpty()){ //no detection possible
			while(!this.tempQueue.isEmpty()){
				this.trackingDataqueue.offer(this.tempQueue.poll());
			}
			this.trackingDataqueue.offer(input);
			this.last_frame = input.image.getFrameNumber();
			
			return;
		}
		
		this.tempQueue.offer(input);
		this.last_frame = input.image.getFrameNumber();
		
		while(this.tempQueue.size() > 3){
			this.trackingDataqueue.offer(this.tempQueue.poll());
		}
		
		if(this.tempQueue.size() < 3){
			return;
		}
		
		
		
		//detection
		int[] pos0 = this.tempQueue.get(0).tracks.elements().nextElement().getCenter();
		int[] pos1 = this.tempQueue.get(1).tracks.elements().nextElement().getCenter();
		int[] pos2 = this.tempQueue.get(2).tracks.elements().nextElement().getCenter();
		
		int dist_01 = distance(pos0, pos1), dist_02 = distance(pos0, pos2), dist_12 = distance(pos1, pos2);
		
		boolean remove_0 = (dist_01 > this.max_dist && dist_02 > this.max_dist);
		boolean remove_1 = (dist_01 > this.max_dist && dist_12 > this.max_dist);
		boolean remove_2 = (dist_12 > this.max_dist && dist_02 > this.max_dist);
		
		int  return_count = 0;
		
		if(remove_0){
			this.tempQueue.set(0, new TrackingDataSet(new ConcurrentHashMap<Integer, Blob>(), this.tempQueue.get(0).image));
			return_count = 1;
		}
		
		if(remove_1){
			this.tempQueue.set(1, new TrackingDataSet(new ConcurrentHashMap<Integer, Blob>(), this.tempQueue.get(1).image));
			return_count = 2;
		}
		
		if(remove_2){
			this.tempQueue.set(2, new TrackingDataSet(new ConcurrentHashMap<Integer, Blob>(), this.tempQueue.get(2).image));
			return_count = 3;
		}
		
		for(int i = 0; i < return_count; ++i){
			this.trackingDataqueue.offer(this.tempQueue.poll());
		}

	}

	private int distance(int[] pos1, int[] pos2){
		int x = pos1[0] - pos2[0];
		int y = pos1[1] - pos2[1];
		return (x*x) + (y*y);
	}
	
}
