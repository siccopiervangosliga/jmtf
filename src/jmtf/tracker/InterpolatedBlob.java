/**
 * Java Motion Tracking Framework
 */
package jmtf.tracker;

/**
 * Represents a Blob with only a position and no area which is created during interpolation
 * @author Luca Rossetto
 *
 */
public class InterpolatedBlob implements Blob {

	private int[] pos;
	
	public InterpolatedBlob(int[] pos){
		this.pos = pos; 
	}
	
	/* (non-Javadoc)
	 * @see jmtf.tracker.Blob#getCenter()
	 */
	@Override
	public int[] getCenter() {
		return this.pos;
	}

	/* (non-Javadoc)
	 * @see jmtf.tracker.Blob#getArea()
	 */
	@Override
	public int getArea() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see jmtf.tracker.Blob#scale(float, float)
	 */
	@Override
	public void scale(float scale_x, float scale_y) {
		this.pos[0] *= scale_x;
		this.pos[1] *= scale_y;
	}

	/* (non-Javadoc)
	 * @see jmtf.tracker.Blob#translate(float, float)
	 */
	@Override
	public void translate(float translate_x, float translate_y) {
		this.pos[0] += translate_x;
		this.pos[1] += translate_y;
	}

}
