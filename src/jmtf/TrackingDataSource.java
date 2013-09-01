/**
 * Java Motion Tracking Framework
 */
package jmtf;


/**
 * @author Luca Rossetto
 *
 */
public interface TrackingDataSource extends TrackingDataUpdater {

	TrackingDataSet getTrackingDataSet();
	TrackingDataSet getNextTrackingDataSet();
	
}
