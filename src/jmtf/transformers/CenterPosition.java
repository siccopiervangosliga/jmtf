/**
 * Java Motion Tracking Framework
 */
package jmtf.transformers;

import java.util.Enumeration;

import jmtf.AbstractTrackingDataTransformer;
import jmtf.TrackingDataSet;
import jmtf.TrackingDataSource;

/**
 * Moves the origin of all blobs from the top left corner to the center of the image.
 * @author Luca Rossetto
 *
 */
public class CenterPosition extends AbstractTrackingDataTransformer {

	
	public CenterPosition(TrackingDataSource source) {
		super(source);
	}

	/* (non-Javadoc)
	 * @see jmtf.AbstractTrackingDataTransformer#transform(jmtf.TrackingDataSet)
	 */
	@Override
	public void transform(TrackingDataSet input) {
		if(input == null || input.image == null || input.tracks == null){
			return;
		}
		float translate_x = -input.image.getWidth() / 2.0f, translate_y = -input.image.getHeight() / 2.0f;
		
		Enumeration<Integer> keys = input.tracks.keys();
		while(keys.hasMoreElements()){
			input.tracks.get(keys.nextElement()).translate(translate_x, translate_y);
		}

	}

}
