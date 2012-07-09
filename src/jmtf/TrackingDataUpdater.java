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
	public void addTrackingDataUpdateListener(TrackingDataUpdateListener listener);
	
	/**
	 * Removes a TrackingDataUpdateListener to the list of listeners to notify
	 * @param listener TrackingDataUpdateListener to remove from the list
	 */
	public void removeTrackingDataUpdateListener(TrackingDataUpdateListener listener);
}
