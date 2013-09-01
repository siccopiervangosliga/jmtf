/**
 * Java Motion Tracking Framework
 */
package jmtf;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Luca Rossetto
 *
 */
public abstract class AbstractTrackingDataSource implements TrackingDataSource {

	protected TrackingDataSource source;
	protected TrackingDataSet trackingDataSet;
	private ArrayList<TrackingDataUpdateListener> listeners = new ArrayList<TrackingDataUpdateListener>();
	
	protected AbstractTrackingDataSource(TrackingDataSource source){
		this.source = source;
	}

	public void addTrackingDataUpdateListener(TrackingDataUpdateListener listener) {
		this.listeners.add(listener);
	}

	public void removeTrackingDataUpdateListener(TrackingDataUpdateListener listener) {
		this.listeners.remove(listener);
	}

	public abstract TrackingDataSet getNextTrackingDataSet();
	
	public TrackingDataSet getTrackingDataSet() {
		return this.trackingDataSet;
	}

	protected void notifyListeners() {
		Iterator<TrackingDataUpdateListener> iter = this.listeners.iterator();
		while(iter.hasNext()){
			iter.next().update(this.trackingDataSet);
		}
	}

}