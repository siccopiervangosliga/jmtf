/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.color;

import jmtf.ImageSource;
import jmtf.manipulators.intestitymap.CumSum;

/**
 * @author Luca Rossetto
 *
 */
public class HistogramEquilisation extends IntensityMapper {

	private HistogramCalculator histcalc;
	
	public HistogramEquilisation(ImageSource source) {
		super(new HistogramCalculator(source), null);
		this.histcalc = (HistogramCalculator) super.getSource();
		this.setRedMap(new CumSum(histcalc, HistogramCalculator.RETURN_RED));
		this.setGreenMap(new CumSum(histcalc, HistogramCalculator.RETURN_GREEN));
		this.setBlueMap(new CumSum(histcalc, HistogramCalculator.RETURN_BLUE));
		
	}
	
	@Override
	public ImageSource getSource(){
		return this.histcalc.getSource();
	}
}
