/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators;

/**
 * @author Luca Rossetto
 *
 */
public abstract class AbstractIntensityMapManipulator implements IntensityMap {

	private int[] map;
	private int selector;
	private IntensityMap intensitymap;
	
	protected AbstractIntensityMapManipulator(IntensityMap source, int selector){
		this.intensitymap = source;
		this.selector = selector;
	}
	
	protected AbstractIntensityMapManipulator(IntensityMap source){
		this(source, 0);
	}
	
	/* (non-Javadoc)
	 * @see jmtf.manipulators.IntensityMap#getMap()
	 */
	@Override
	public int[] getMap(int selector) {
		this.map = this.manipulate(this.intensitymap.getMap(selector));
		return this.map;
	}
	
	@Override
	public int[] getMap() {
		return this.getMap(this.selector);
	}

	/**
	 * Performs manipulation on Map
	 * @param input
	 * @return
	 */
	public abstract int[] manipulate(int[] input);
	
	

}
