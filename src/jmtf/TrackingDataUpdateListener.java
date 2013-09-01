/**
 * Java Motion Tracking Framework
 */
package jmtf;

/**
 * @author Luca Rossetto
 *
 */
public interface TrackingDataUpdateListener {
	/**
	 * Updates the TrackingDataSet when a new one is available
	 * @param data
	 */
	void update(TrackingDataSet data);
}
