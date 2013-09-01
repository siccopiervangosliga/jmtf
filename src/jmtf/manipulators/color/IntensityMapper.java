/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.color;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;
import jmtf.manipulators.IntensityMap;

/**
 * @author Luca Rossetto
 *
 */
public class IntensityMapper extends AbstractImageManipulator {

	private IntensityMap redMap, greenMap, blueMap;
	
	
	public IntensityMapper(ImageSource source, IntensityMap redMap, IntensityMap greenMap, IntensityMap blueMap) {
		super(source);
		this.redMap = redMap;
		this.greenMap = greenMap;
		this.blueMap = blueMap;
	}
	
	public IntensityMapper(ImageSource source, IntensityMap map){
		super(source);
		this.redMap = map;
		this.greenMap = map;
		this.blueMap = map;
	}

	/* (non-Javadoc)
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		int r, g, b;
		int[] pixels = input.getPixels();
		int[] redMap = this.redMap.getMap(),
				greenMap = this.greenMap.getMap(),
				blueMap = this.blueMap.getMap();
		
		
		//for(int i = 0; i < pixels.length; ++i){
		int col;
		for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
			for (int y = input.getROI().minY; y <= input.getROI().maxY; ++y) {
		
				col = input.getPixel(x, y);
				
			r = JMTFImage.getRed(col);
			g = JMTFImage.getGreen(col);
			b = JMTFImage.getBlue(col);
			input.setPixel(x, y, JMTFImage.getColor(redMap[r], greenMap[g], blueMap[b]));
			}
		}
		//}
		//input.setPixels(pixels);

	}
	
	public IntensityMap getRedMap(){
		return this.redMap;
	}
	
	public IntensityMap getGreenMap(){
		return this.greenMap;
	}
	
	public IntensityMap getBlueMap(){
		return this.blueMap;
	}
	
	public void setRedMap(IntensityMap redMap){
		this.redMap = redMap;
	}
	
	public void setGreenMap(IntensityMap greenMap){
		this.greenMap = greenMap;
	}
	
	public void setBlueMap(IntensityMap blueMap){
		this.blueMap = blueMap;
	}

}
