/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.probabilistic;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * @author Luca Rossetto
 * generates a heatmap based on a {@link ColorProbability}
 */
public class ProbabilityHeatMap extends AbstractImageManipulator {

	private ColorProbability prob;
	

	public ProbabilityHeatMap(ImageSource source, ColorProbability prob) {
		super(source);
		this.prob = prob;
	}

	/* (non-Javadoc)
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
			for (int y = input.getROI().minY; y <= input.getROI().maxY; ++y) {
				int col = input.getPixel(x, y);
				int val = Math.round(prob.getProbability(col) * 255);
				input.setPixel(x, y, JMTFImage.getColor(val, val, val));
			}
		}

	}

}
