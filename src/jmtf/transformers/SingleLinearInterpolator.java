/**
 * Java Motion Tracking Framework
 */
package jmtf.transformers;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import jmtf.AbstractTrackingDataBuffer;
import jmtf.JMTFImage;
import jmtf.TrackingDataSet;
import jmtf.TrackingDataSource;
import jmtf.tracker.Blob;
import jmtf.tracker.InterpolatedBlob;

/**
 * Interpolates one blob linearly over frames where no blob was tracked. If there are multiple blobs in a TrackingDataSet, the first one is used.
 * A maximal number of frames over how many is going to be interpolated can be specified.
 * @author Luca Rossetto
 *
 */
public class SingleLinearInterpolator extends AbstractTrackingDataBuffer {

	private int max_frames;
	private int[] last_pos;
	private int last_frame = -1;
	private LinkedList<TrackingDataSet> tmp_queue = new LinkedList<TrackingDataSet>();
	
	/**
	 * @param source the {@link TrackingDataSource} from which to read
	 * @param max_frames after how many frames without a new position not to interpolate anymore
	 */
	public SingleLinearInterpolator(TrackingDataSource source, int max_frames) {
		super(source);
		this.max_frames = max_frames;
	}
	
	public SingleLinearInterpolator(TrackingDataSource source){
		this(source, -1);
	}

	/* (non-Javadoc)
	 * @see jmtf.AbstractTrackingDataBuffer#updateBuffer(jmtf.TrackingDataSet)
	 */
	@Override
	public void updateBuffer(TrackingDataSet input) {
		if(input == null || input.tracks == null || input.image == null){
			this.trackingDataqueue.offer(null);
			return;
		}
		
		if(input.tracks.size() == 0){
			this.imageQueue.offer(input.image);
			return;
		}
		int number_of_frames = this.imageQueue.size();
		
		if(number_of_frames == 0){
			this.trackingDataqueue.offer(input);
			return;
		}
		
		int current_frame = input.image.getFrameNumber();
		
		
		//the first element from the List
		Blob current_blob = input.tracks.elements().nextElement();
		int[] current_pos = current_blob.getCenter();
		
		if(number_of_frames > max_frames && max_frames > 0){ //no not interpolate
			this.trackingDataqueue.offer(input);
			//if gap is to big, insert empty results
			while(!this.imageQueue.isEmpty()){
				this.trackingDataqueue.offer(new TrackingDataSet(new ConcurrentHashMap<Integer, Blob>(), this.imageQueue.poll()));
			}
			
		}else{ //interpolate
			
			this.tmp_queue.offer(input);
			if (this.last_pos != null) {
				float frame_difference = current_frame - this.last_frame;
				float dx = (current_pos[0] - last_pos[0]) / frame_difference, dy = (current_pos[1] - last_pos[1])
						/ frame_difference;
				while (!this.imageQueue.isEmpty()) {
					JMTFImage img = this.imageQueue.poll();
					int[] pos = new int[2];
					float t = (img.getFrameNumber() - last_frame);
					pos[0] = Math.round(last_pos[0] + (t * dx));
					pos[1] = Math.round(last_pos[1] + (t * dy));
					ConcurrentHashMap<Integer, Blob> ht = new ConcurrentHashMap<Integer, Blob>();
					ht.put(1, new InterpolatedBlob(pos));
					this.trackingDataqueue.offer(new TrackingDataSet(ht, img));
				}
				//ensures that no TrackingDataSet goes missing
				while(!this.tmp_queue.isEmpty()){
					this.trackingDataqueue.offer(this.tmp_queue.poll());
				}
			}
		}
		
		this.last_frame = current_frame;
		this.last_pos = current_pos;
	}
}
