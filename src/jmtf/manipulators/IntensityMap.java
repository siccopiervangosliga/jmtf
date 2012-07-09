/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators;

/**
 * @author Luca Rossetto
 *
 */
public interface IntensityMap {
	
	public static final int[] nullmap = new int[256];

	public static final IntensityMap NULLMAP = new IntensityMap() {
		
		@Override
		public int[] getMap(int selector) {
			return getMap();
		}
		
		@Override
		public int[] getMap() {
			return IntensityMap.nullmap;
		}
	};
	
	public int[] getMap();
	
	public int[] getMap(int selector);
	
}
