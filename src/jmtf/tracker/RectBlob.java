/**
 * Java Motion Tracking Framework
 */
package jmtf.tracker;

import jmtf.JMTFImage;

/**
 * @author Luca Rossetto
 *
 */
public class RectBlob implements Blob{
	public int min_x, min_y, max_x, max_y, intensity, id;
	private int[] hist = new int[256];
	
	public RectBlob(int min_x, int min_y, int max_x, int max_y, int intensity, int id){
		this.min_x = min_x;
		this.min_y = min_y;
		this.max_x = max_x;
		this.max_y = max_y;
		this.id = id;
	}
	
	public String toString(){
		return "RectBlob " + this.id + ": from (" + this.min_x + ", " + this.min_y + ") to (" + this.max_x + ", " + this.max_y + ")";
	}
	
	public int[] getCenter(){
		return new int[]{(this.min_x + this.max_x) / 2, (this.min_y + this.max_y) / 2};
	}
	
	public int getArea(){
		return (this.max_x - this.min_x) * (this.max_y - this.min_y);
	}
	
	public void scale(float scale_x, float scale_y){
		this.min_x *= scale_x;
		this.min_y *= scale_y;
		this.max_x *= scale_x;
		this.max_y *= scale_y;
	}
	
	public void translate(float translate_x, float translate_y){
		this.min_x += translate_x;
		this.min_y += translate_y;
		this.max_x += translate_x;
		this.max_y += translate_y;
	}
	
	public int[] getHistogram(){
		return this.hist;
	}

	/* (non-Javadoc)
	 * @see jmtf.tracker.Blob#getMaxIntensity()
	 */
	@Override
	public int getMaxIntensity() {
		return intensity;
	}
	
	void updateImageRelatedData(JMTFImage img){
		intensity = 0;
		for(int i = 0; i < this.hist.length; ++i){
			this.hist[i] = 0;
		}
		for(int y = min_y; y <= max_y; ++y){
			for(int x = min_x; x <= max_x; ++x){
				int val = JMTFImage.getRed(img.getPixel(x, y));
				this.intensity = Math.max(intensity, val);
				this.hist[val]++;
			}
		}
	}

	/* (non-Javadoc)
	 * @see jmtf.tracker.Blob#getWidth()
	 */
	@Override
	public int getWidth() {
		return max_x - min_x;
	}

	/* (non-Javadoc)
	 * @see jmtf.tracker.Blob#getHeight()
	 */
	@Override
	public int getHeight() {
		return max_y - min_y;
	}
}