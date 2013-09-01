/**
 * Java Motion Tracking Framework
 */
package jmtf.tracker;

import java.util.ArrayList;
import java.util.Iterator;

import jmtf.ImageSource;
import jmtf.TrackingDataSet;
import jmtf.TrackingDataSource;
import jmtf.TrackingDataUpdateListener;

/**
 * @author Luca Rossetto
 *
 */
public abstract class AbstractTracker implements TrackingDataSource{

	private ArrayList<TrackingDataUpdateListener> listeners = new ArrayList<TrackingDataUpdateListener>();
	
	@Override
	public void addTrackingDataUpdateListener(
			TrackingDataUpdateListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeTrackingDataUpdateListener(
			TrackingDataUpdateListener listener) {
		this.listeners.remove(listener);
	}


	protected ImageSource source;
	protected TrackingDataSet trackingDataSet;
	
	protected abstract TrackingDataSet track();

	/* (non-Javadoc)
	 * @see jmtf.TrackingDataSource#getTrackingDataSet()
	 */
	@Override
	public TrackingDataSet getTrackingDataSet() {
		return this.trackingDataSet;
	}

	/* (non-Javadoc)
	 * @see jmtf.TrackingDataSource#getNextTrackingDataSet()
	 */
	@Override
	public TrackingDataSet getNextTrackingDataSet() {
		this.trackingDataSet = this.track();
		notifyListeners();
		return this.trackingDataSet;
	}
	
	private void notifyListeners(){
		Iterator<TrackingDataUpdateListener> iter = this.listeners.iterator();
		while(iter.hasNext()){
			iter.next().update(this.trackingDataSet);
		}
	}
	
}
