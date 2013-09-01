/**
 * Java Motion Tracking Framework
 */
package jmtf.tracker;

/**
 * @author Luca Rossetto
 *
 */
public interface Blob {

	int[] getCenter();
	int getArea();
	int getMaxIntensity();
	int getWidth();
	int getHeight();
	int[] getHistogram();
	void scale(float scale_x, float scale_y);
	void translate(float translate_x, float translate_y);
	
}
