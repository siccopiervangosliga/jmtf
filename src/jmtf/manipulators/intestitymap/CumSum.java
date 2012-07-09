/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.intestitymap;

import jmtf.manipulators.AbstractIntensityMapManipulator;
import jmtf.manipulators.IntensityMap;

/**
 * calculates the cumulative sum of an input intensityMap
 * @author Luca Rossetto
 *
 */
public class CumSum extends AbstractIntensityMapManipulator {


	public CumSum(IntensityMap source, int selector) {
		super(source, selector);
	}

	/* (non-Javadoc)
	 * @see jmtf.manipulators.AbstractIntensityMapManipulator#manipulate(int[])
	 */
	@Override
	public int[] manipulate(int[] input) {
		int[] _return = new int[input.length];
		_return[0] = input[0];
		
		for(int i = 1; i < input.length; ++i){
			_return[i] = _return[i-1] + input[i];
		}
		
		if(_return[0] == _return[_return.length - 1]){//all zeros
			for(int i = 0; i < _return.length; ++i){
				_return[i] = 0;
			}
			return _return;
		}
		
		for(int i = 0; i < input.length; ++i){
			_return[i] = Math.round((_return[i] * 255.0f) / _return[_return.length - 1]);
		}
		
		return _return;
		
	}

}
