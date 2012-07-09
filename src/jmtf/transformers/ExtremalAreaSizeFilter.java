/**
 * Java Motion Tracking Framework
 */
package jmtf.transformers;

import java.util.Enumeration;

import jmtf.AbstractTrackingDataTransformer;
import jmtf.TrackingDataSet;
import jmtf.TrackingDataSource;

/**
 * @author Luca Rossetto
 *
 */
public class ExtremalAreaSizeFilter extends AbstractTrackingDataTransformer {

	private boolean invert = false;
	
	/**
	 * @param source
	 */
	public ExtremalAreaSizeFilter(TrackingDataSource source) {
		super(source);
	}

	public ExtremalAreaSizeFilter(TrackingDataSource source, boolean minimalArea){
		super(source);
		this.invert = minimalArea;
	}

	/* (non-Javadoc)
	 * @see jmtf.AbstractTrackingDataTransformator#transform(java.util.Hashtable)
	 */
	@Override
	public void transform(TrackingDataSet input) {
		
		if(input == null){
			return;
		}
		
		int best_key = -1, key, area;
		int best_area = this.invert ? Integer.MAX_VALUE : -1;
		
		Enumeration<Integer> keys = input.tracks.keys();
		while(keys.hasMoreElements()){
			key = keys.nextElement();
			if(((area = input.tracks.get(key).getArea()) > best_area) ^ this.invert) {
				best_key = key;
				best_area = area;
			}
		}
		keys = input.tracks.keys();
		while(keys.hasMoreElements()){
			if((key = keys.nextElement()) != best_key){
				input.tracks.remove(key);
			}
		}

	}

}
