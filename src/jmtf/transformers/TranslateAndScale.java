/**
 * Java Motion Tracking Framework
 */
package jmtf.transformers;

import java.util.Enumeration;

import jmtf.AbstractTrackingDataTransformer;
import jmtf.TrackingDataSet;
import jmtf.TrackingDataSource;
import jmtf.tracker.Blob;

/**
 * @author Luca Rossetto
 *
 */
public class TranslateAndScale extends AbstractTrackingDataTransformer {

	private float translate_x, translate_y, scale_x, scale_y;
	
	public TranslateAndScale(TrackingDataSource source, float translate_x, float translate_y, float scale_x, float scale_y){
		super(source);
		this.translate_x = translate_x;
		this.translate_y = translate_y;
		this.scale_x = scale_x;
		this.scale_y = scale_y;
	}
	
	/* (non-Javadoc)
	 * @see jmtf.AbstractTrackingDataTransformator#transform(java.util.Hashtable)
	 */
	@Override
	public void transform(TrackingDataSet input) {
		Blob b;
		Enumeration<Integer> keys = input.tracks.keys();
		while(keys.hasMoreElements()){
			b = input.tracks.get(keys.nextElement());
			b.translate(this.translate_x, this.translate_y);
			b.scale(this.scale_x, this.scale_y);
		}
	}
	

}
