/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.raster;

import java.util.ArrayList;
import java.util.Iterator;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.ImageUpdateListener;
import jmtf.JMTFImage;

/**
 * rerasterizes an image with a given cell size
 * @author Luca Rossetto
 *
 */
public abstract class AbstractImageReRasterizer extends
		AbstractImageManipulator {

	protected int cellDiameter;
	protected JMTFImage rerastered;
	
	private ArrayList<ImageUpdateListener> rerlisteners = new ArrayList<ImageUpdateListener>();

	protected AbstractImageReRasterizer(ImageSource source, int cellDiameter) {
		super(source);
		this.cellDiameter = cellDiameter;
		if(this.cellDiameter < 1){
			throw new IllegalArgumentException("cellDiameter must be > 0");
		}
	}

	/* (non-Javadoc)
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		if(input == null || this.cellDiameter == 1){
			return;
		}
		int width = input.getROI().getWidth();
		int height = input.getROI().getHeight();
		
		int cellsX = (int) Math.ceil(((float)width) / this.cellDiameter);
		int cellsY = (int) Math.ceil(((float)height) / this.cellDiameter);
		
		this.rerastered = new JMTFImage(cellsX, cellsY);
		
		int[] pixels = new int[this.cellDiameter * this.cellDiameter];
		
		for(int cx = 0; cx < cellsX; ++cx){
			for(int cy = 0; cy < cellsY; ++cy){
				
				int len = 0;
				
				for(int x = cx * this.cellDiameter; x < Math.min(width, (cx + 1) * this.cellDiameter); ++x){
					for(int y = cy * this.cellDiameter; y < Math.min(height, (cy + 1) * this.cellDiameter); ++y){
						pixels[len++] = input.getPixel(x + input.getROI().minX, y + input.getROI().minY);
					}
				}
				
				int col = rerasterize(pixels, len);
				this.rerastered.setPixel(cx, cy, col);
				
				for(int x = cx * this.cellDiameter; x < Math.min(width, (cx + 1) * this.cellDiameter); ++x){
					for(int y = cy * this.cellDiameter; y < Math.min(height, (cy + 1) * this.cellDiameter); ++y){
						input.setPixel(x + input.getROI().minX, y + input.getROI().minY, col);
					}
				}
				
			}
			
		}
		updateReRasterListeners();
	}
	
	/**
	 * performs the actual rerasterisation
	 * @param colors array of colors
	 * @param len length of the array (array might be longer than length, additional values are ignored)
	 * @return the color for the new cell
	 */
	protected abstract int rerasterize(int[] colors, int len);
	
	public void addReRasterUpdateListener(ImageUpdateListener listener){
		this.rerlisteners.add(listener);
	}
	
	public void removeReRasterUpdateListener(ImageUpdateListener listener){
		this.rerlisteners.remove(listener);
	}
	
	protected void updateReRasterListeners(){
		Iterator<ImageUpdateListener> iter = this.rerlisteners.iterator();
		while(iter.hasNext()){
			iter.next().update(this.rerastered);
		}
	}

	/**
	 * @return an image where every pixel represents one of the newly created cells
	 */
	public JMTFImage getCurrentReRasteredImage(){
		return this.rerastered;
	}

}
