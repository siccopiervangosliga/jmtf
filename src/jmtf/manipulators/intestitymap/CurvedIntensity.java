/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.intestitymap;

import jmtf.manipulators.IntensityMap;

/**
 * @author Luca Rossetto
 *
 */
public class CurvedIntensity implements IntensityMap {

	private int[] map = new int[256];
	
	public CurvedIntensity(float exponent){
		for(int i = 0; i < 256; ++i){
			map[i] = (int) Math.round(Math.pow(((float) i) / 255f , exponent) * 255f);
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
