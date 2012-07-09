/**
 * Java Motion Tracking Framework
 */
package jmtf;

/**
 * @author Luca Rossetto
 *
 */
public abstract class AbstractTrackingDataTransformer extends AbstractTrackingDataSource implements
		TrackingDataSource {
	

	public AbstractTrackingDataTransformer(TrackingDataSource source) {
		super(source);
	}

	/* (non-Javadoc)
	 * @see jmtf.TrackingDataSource#getNextTrackingDataSet()
	 */
	@Override
	public TrackingDataSet getNextTrackingDataSet() {
		this.trackingDataSet = this.source.getNextTrackingDataSet();
		this.transform(this.trackingDataSet);
		notifyListeners();
		return this.trackingDataSet;
	}
	
	abstract public void transform(TrackingDataSet input);

}
