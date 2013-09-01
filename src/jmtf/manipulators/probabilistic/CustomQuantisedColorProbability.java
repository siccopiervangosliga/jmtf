/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.probabilistic;


/**
 * @author Luca Rossetto
 * uses additional quantisation which can be more coarse to artificially reduce the range of colors
 */
public class CustomQuantisedColorProbability extends ColorProbability{

	private int cellDiameter;
	
	public CustomQuantisedColorProbability(int cellDiameter, boolean normalize){
		super(normalize);
		this.cellDiameter = cellDiameter;
	}
	
	protected int getIndex(int red, int green, int blue){
		return super.getIndex(red / cellDiameter, green / cellDiameter, blue / cellDiameter);
	}
	
}
