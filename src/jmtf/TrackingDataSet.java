/**
 * Java Motion Tracking Framework
 */
package jmtf;

import java.util.Hashtable;

import jmtf.tracker.Blob;

/**
 * @author Luca Rossetto
 *
 */
public class TrackingDataSet {

	public Hashtable<Integer, Blob> tracks;
	public JMTFImage image;
	
	public TrackingDataSet(Hashtable<Integer, Blob> tracks, JMTFImage image){
		this.image = image;
		this.tracks = tracks;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		result = prime * result + ((tracks == null) ? 0 : tracks.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TrackingDataSet other = (TrackingDataSet) obj;
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!image.equals(other.image))
			return false;
		if (tracks == null) {
			if (other.tracks != null)
				return false;
		} else if (!tracks.equals(other.tracks))
			return false;
		return true;
	}

	/**
	 * @return a copy of the TrackningDataSet
	 */
	@SuppressWarnings("unchecked")
	public TrackingDataSet copy() {
		return new TrackingDataSet((Hashtable<Integer, Blob>) tracks.clone(), image.copy());
	}
	
}
