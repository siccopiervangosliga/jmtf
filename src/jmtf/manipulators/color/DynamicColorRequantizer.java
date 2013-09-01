/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * @author Luca Rossetto
 * 
 *         algorithm based upon
 *         "Color Image Quantization by Minimizing the Maximum Intercluster Distance"
 *         by ZHIGANG XIANG
 * 
 *         ACM Transactions on Graphics (TOG) Volume 16 Issue 3, July 1997
 * 
 *         some changes were made to the described algorithm.
 * 
 * 
 */
public class DynamicColorRequantizer extends AbstractImageManipulator {

	private int numColors;
	private boolean useMean = true;

	public DynamicColorRequantizer(ImageSource source, int numColors) {
		super(source);
		this.numColors = numColors;
	}

	/**
	 * set if color mean should be used to evaluate cluster centres (on by
	 * default). lightly slower if enabled, might produce better results
	 * 
	 * @param b
	 */
	public void useMean(boolean b) {
		useMean = b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {

		ArrayList<LinkedList<Integer>> clusters = new ArrayList<LinkedList<Integer>>(
				this.numColors);
		LinkedList<Integer> cluster = new LinkedList<Integer>();

		HashSet<Integer> colors = new HashSet<Integer>();
		for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
			for (int y = input.getROI().minY; y <= input.getROI().maxX; ++y) {
				colors.add(input.getPixel(x, y));
			}
		}

		// init first cluster with all colors
		for (int col : colors) {
			cluster.add(col);
		}

		clusters.add(cluster);

		for (int x = 1; x < this.numColors; ++x) {
			LinkedList<Integer> currentCluster = clusters.get(x - 1);
			for (int y = 0; y < x - 2; ++y) {
				if (currentCluster.size() < clusters.get(y).size()) {
					currentCluster = clusters.get(y);
				}
			}
			int head = getHeadColor(currentCluster);
			// search next init head
			int nextHead = head;
			int maxDist = 0;
			for (int col : currentCluster) {
				int dist = JMTFImage.squaredColorDistance(head, col);
				if (dist > maxDist) {
					maxDist = dist;
					nextHead = col;
				}
			}
			currentCluster.remove((Integer) nextHead);
			LinkedList<Integer> nextCluster = new LinkedList<Integer>();
			nextCluster.add(nextHead);

			for (int y = 0; y < x; ++y) {
				currentCluster = clusters.get(y);
				head = getHeadColor(currentCluster);
				Iterator<Integer> iter = currentCluster.iterator();
				while (iter.hasNext()) {
					int col = iter.next();
					if (JMTFImage.squaredColorDistance(head, col) > JMTFImage
							.squaredColorDistance(nextHead, col)) {
						iter.remove();
						nextCluster.add(col);
					}
				}
			}
			clusters.add(nextCluster);
		}

		HashMap<Integer, Integer> mapping = new HashMap<Integer, Integer>();
		for (LinkedList<Integer> list : clusters) {
			int head = getHeadColor(list);
			for (int col : list) {
				mapping.put(col, head);
			}
		}

		for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
			for (int y = input.getROI().minY; y <= input.getROI().maxX; ++y) {
				input.setPixel(x, y, mapping.get(input.getPixel(x, y)));
			}
		}

	}

	public void setNumberOfColors(int num) {
		this.numColors = num;
	}

	public int getNumberOfColors() {
		return this.numColors;
	}

	private int getHeadColor(LinkedList<Integer> colors) {
		if (!useMean) {
			return colors.getFirst();
		}
		int r = 0, g = 0, b = 0;
		for (int col : colors) {
			r += JMTFImage.getRed(col);
			g += JMTFImage.getGreen(col);
			b += JMTFImage.getBlue(col);
		}
		int mean = JMTFImage.getColor(r / colors.size(), g / colors.size(), b
				/ colors.size());
		int minDist = Integer.MAX_VALUE, bestCol = colors.get(0);
		for (int col : colors) {
			int dist = JMTFImage.squaredColorDistance(mean, col);
			if (dist < minDist) {
				minDist = dist;
				bestCol = col;
			}
		}
		return bestCol;
	}

}
