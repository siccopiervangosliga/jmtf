/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.intestitymap;

import jmtf.manipulators.IntensityMap;

/**
 * @author Luca Rossetto
 *
 */
public class IntensityThreshold implements IntensityMap {

	private int[] map = new int[256];
	
	public IntensityThreshold(int min, int max){
		for(int i = min; i <= max; ++i){
			map[i] = i;
		}
	}
	
	/* (non-Javadoc)
	 * @see jmtf.manipulators.IntensityMap#getMap()
	 */
	@Override
	public int[] getMap() {
		return map;
	}

	/* (non-Javadoc)
	 * @see jmtf.manipulators.IntensityMap#getMap(int)
	 */
	@Override
	public int[] getMap(int selector) {
		return map;
	}

}
