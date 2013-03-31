/**
 * Java Motion Tracking Framework
 */
package jmtf.tracker;

/**
 * @author Luca Rossetto
 *
 */
public interface Blob {

	public int[] getCenter();
	public int getArea();
	public int getMaxIntensity();
	public void scale(float scale_x, float scale_y);
	public void translate(float translate_x, float translate_y);
	
}
