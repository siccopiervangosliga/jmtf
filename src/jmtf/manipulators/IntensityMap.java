/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators;

/**
 * @author Luca Rossetto
 *
 */
public interface IntensityMap {
	
	int[] nullmap = new int[256];

	IntensityMap NULLMAP = new IntensityMap() {
		
		@Override
		public int[] getMap(int selector) {
			return getMap();
		}
		
		@Override
		public int[] getMap() {
			return IntensityMap.nullmap;
		}
	};
	
	int[] getMap();
	
	int[] getMap(int selector);
	
}
