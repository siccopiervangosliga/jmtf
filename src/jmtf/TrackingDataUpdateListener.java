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
	public void update(TrackingDataSet data);
}
