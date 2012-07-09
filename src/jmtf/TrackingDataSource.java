/**
 * Java Motion Tracking Framework
 */
package jmtf;


/**
 * @author Luca Rossetto
 *
 */
public interface TrackingDataSource extends TrackingDataUpdater {

	public TrackingDataSet getTrackingDataSet();
	public TrackingDataSet getNextTrackingDataSet();
	
}
