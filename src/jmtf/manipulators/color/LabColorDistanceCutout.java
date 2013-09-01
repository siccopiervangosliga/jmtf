/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.color;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * @author Luca Rossetto
 * 
 */
public class LabColorDistanceCutout extends AbstractImageManipulator {

	private float L, a, b, threshold;
	private boolean invert = false;

	/**
	 * @param source
	 */
	public LabColorDistanceCutout(ImageSource source, int color, float threshold) {
		super(source);
		this.threshold = threshold * threshold;
		this.invert = threshold < 0;

		float r = JMTFImage.getRed(color) / 255f, g = JMTFImage.getGreen(color) / 255f, b = JMTFImage
				.getBlue(color) / 255f;
		if (r > 0.04045f) {
			r = (float) Math.pow((r + 0.055) / 1.055, 2.4);
		} else {
			r /= 12.92f;
		}
		if (g > 0.04045f) {
			g = (float) Math.pow((g + 0.055) / 1.055, 2.4);
		} else {
			g /= 12.92f;
		}
		if (b > 0.04045f) {
			b = (float) Math.pow((b + 0.055) / 1.055, 2.4);
		} else {
			b /= 12.92f;
		}

		float x = (r * 412.4f + g * 357.6f + b * 180.5f) / 95.047f;
		float y = (r * 212.6f + g * 715.2f + b * 77.2f) / 100f;
		float z = (r * 19.3f + g * 119.2f + b * 950.5f) / 108.883f;

		if (x > 0.008856f) {
			x = (float) Math.pow(x, 1d / 3d);
		} else {
			x = (x * 7.787f) + (16f / 116f);
		}
		if (y > 0.008856f) {
			y = (float) Math.pow(y, 1d / 3d);
		} else {
			y = (y * 7.787f) + (16f / 116f);
		}
		if (z > 0.008856f) {
			z = (float) Math.pow(z, 1d / 3d);
		} else {
			z = (z * 7.787f) + (16f / 116f);
		}

		this.L = (16f * y) - 16f;
		this.a = 500f * (x - y);
		this.b = 200 * (y - z);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		// int[] pixels = input.getPixels();
		// for(int i = 0; i < pixels.length; ++i){
		int col;

		for (int _x = input.getROI().minX; _x <= input.getROI().maxX; ++_x) {
			for (int _y = input.getROI().minY; _y <= input.getROI().maxY; ++_y) {

				col = input.getPixel(_x, _y);

				float r = JMTFImage.getRed(col) / 255f, g = JMTFImage
						.getGreen(col) / 255f, b = JMTFImage.getBlue(col) / 255f;
				if (r > 0.04045f) {
					r = (float) Math.pow((r + 0.055) / 1.055, 2.4);
				} else {
					r /= 12.92f;
				}
				if (g > 0.04045f) {
					g = (float) Math.pow((g + 0.055) / 1.055, 2.4);
				} else {
					g /= 12.92f;
				}
				if (b > 0.04045f) {
					b = (float) Math.pow((b + 0.055) / 1.055, 2.4);
				} else {
					b /= 12.92f;
				}

				float x = (r * 412.4f + g * 357.6f + b * 180.5f) / 95.047f;
				float y = (r * 212.6f + g * 715.2f + b * 77.2f) / 100f;
				float z = (r * 19.3f + g * 119.2f + b * 950.5f) / 108.883f;

				if (x > 0.008856f) {
					x = (float) Math.pow(x, 1d / 3d);
				} else {
					x = (x * 7.787f) + (16f / 116f);
				}
				if (y > 0.008856f) {
					y = (float) Math.pow(y, 1d / 3d);
				} else {
					y = (y * 7.787f) + (16f / 116f);
				}
				if (z > 0.008856f) {
					z = (float) Math.pow(z, 1d / 3d);
				} else {
					z = (z * 7.787f) + (16f / 116f);
				}

				float _L = (16f * y) - 16f;
				float _a = 500f * (x - y);
				float _b = 200 * (y - z);

				if (this.invert ^ dist(_L, _a, _b) > this.threshold) {
					input.setPixel(_x, _y, 0);
				}
			}
		}
	}

	private float dist(float L, float a, float b) {
		return (this.L - L) * (this.L - L) + (this.a - a) * (this.a - a)
				+ (this.b - b) * (this.b - b);
	}

}
