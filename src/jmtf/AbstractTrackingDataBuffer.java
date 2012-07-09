/**
 * Java Motion Tracking Framework
 */
package jmtf;

import java.util.LinkedList;

/**
 * @author Luca Rossetto
 *
 */
public abstract class AbstractTrackingDataBuffer extends AbstractTrackingDataSource implements TrackingDataUpdater {

	protected LinkedList<TrackingDataSet> trackingDataqueue;
	protected LinkedList<JMTFImage> imageQueue;
	
	public AbstractTrackingDataBuffer(TrackingDataSource source) {
		super(source);
		this.trackingDataqueue = new LinkedList<TrackingDataSet>();
		this.imageQueue = new LinkedList<JMTFImage>();
	}

	public abstract void updateBuffer(TrackingDataSet input);

	@Override
	public TrackingDataSet getNextTrackingDataSet() {
		TrackingDataSet set;
		while(this.trackingDataqueue.size() == 0 && (set = this.source.getNextTrackingDataSet()) != null){
			updateBuffer(set);
		}
		this.trackingDataSet = this.trackingDataqueue.poll();
		notifyListeners();
		return this.trackingDataSet;
	}

	

}
