/**
 * Java Motion Tracking Framework
 */
package jmtf;

/**
 * @author Luca Rossetto
 *
 */
public interface TrackingDataUpdater {
	/**
	 * Adds a TrackingDataUpdateListener to the list of listeners to notify
	 * @param listener TrackingDataUpdateListener to add to the list
	 */
	void addTrackingDataUpdateListener(TrackingDataUpdateListener listener);
	
	/**
	 * Removes a TrackingDataUpdateListener to the list of listeners to notify
	 * @param listener TrackingDataUpdateListener to remove from the list
	 */
	void removeTrackingDataUpdateListener(TrackingDataUpdateListener listener);
}
