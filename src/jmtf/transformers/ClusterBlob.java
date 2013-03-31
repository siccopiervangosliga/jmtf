/**
 * Java Motion Tracking Framework
 */
package jmtf.transformers;

import java.util.LinkedList;

import jmtf.tracker.Blob;

/**
 * Combines a cluster of blobs to one
 * @author Luca Rossetto
 *
 */
public class ClusterBlob implements Blob {

	private LinkedList<Blob> blobs = new LinkedList<Blob>();
	private int[] center = new int[2];
	private int area, maxIntensity;
	
	public ClusterBlob(Blob...blobs){
		addBlobs(blobs);
	}
	
	public void addBlobs(Blob...blobs){
		for(Blob b : blobs){
			if(b != this){
				this.blobs.add(b);
			}
		}
		update();
	}
	
	public void removeBlobs(Blob...blobs){
		for(Blob b : blobs){
			this.blobs.remove(b);
		}
		update();
	}
	
	private void update(){
		int[] tmp;
		center[0] = 0;
		center[1] = 0;
		area = 0;
		maxIntensity = 0;
		
		for(Blob b : this.blobs){
			tmp = b.getCenter();
			center[0] += tmp[0];
			center[1] += tmp[1];
			area += b.getArea();
			maxIntensity = Math.max(maxIntensity, b.getMaxIntensity());
		}
		center[0] /= this.blobs.size();
		center[1] /= this.blobs.size();
	}
	
	/* (non-Javadoc)
	 * @see jmtf.tracker.Blob#getCenter()
	 */
	@Override
	public int[] getCenter() {
		return this.center;
	}

	public int getArea() {
		return this.area;
	}

	/* (non-Javadoc)
	 * @see jmtf.tracker.Blob#scale(float, float)
	 */
	@Override
	public void scale(float scale_x, float scale_y) {
		for(Blob b : this.blobs){
			b.scale(scale_x, scale_y);
		}
	}

	/* (non-Javadoc)
	 * @see jmtf.tracker.Blob#translate(float, float)
	 */
	@Override
	public void translate(float translate_x, float translate_y) {
		for(Blob b : this.blobs){
			b.translate(translate_x, translate_y);
		}

	}

	/* (non-Javadoc)
	 * @see jmtf.tracker.Blob#getMaxIntensity()
	 */
	@Override
	public int getMaxIntensity() {
		return maxIntensity;
	}

}
