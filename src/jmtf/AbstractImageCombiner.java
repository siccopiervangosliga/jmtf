/**
 * Java Motion Tracking Framework
 */
package jmtf;


/**
 * @author Luca Rossetto
 * combines the images from two sources into one. 
 */
public abstract class AbstractImageCombiner extends AbstractImageSource {

	private ImageSource source1, source2;
	
	
	protected AbstractImageCombiner(ImageSource source1, ImageSource source2){
		this.source1 = source1;
		this.source2 = source2;
	}
	
	/* (non-Javadoc)
	 * @see jmtf.AbstractImageSource#hasMoreImages()
	 */
	@Override
	public boolean hasMoreImages() {
		boolean s1 = this.source1.hasMoreImages(), s2 = this.source2.hasMoreImages();
		return s1 && s2;
	}

	/* (non-Javadoc)
	 * @see jmtf.AbstractImageSource#getNextImage()
	 */
	@Override
	public JMTFImage getNextImage() {
		JMTFImage in1 = this.source1.getNextImage(), in2 = this.source2.getNextImage();
		this.img = new JMTFImage(Math.min(in1.getWidth(), in2.getWidth()), Math.min(in1.getHeight(), in2.getHeight()));
		this.img.setFrameNumber(in1.getFrameNumber());
		this.img.getROI().minX = Math.min(in1.getROI().minX, in2.getROI().minX);
		this.img.getROI().minY = Math.min(in1.getROI().minY, in2.getROI().minY);
		this.img.getROI().maxX = Math.min(Math.max(in1.getROI().maxX, in2.getROI().maxX), this.img.getROI().maxX);
		this.img.getROI().maxY = Math.min(Math.max(in1.getROI().maxY, in2.getROI().maxY), this.img.getROI().maxY);
		combine(in1, in2, this.img);
		notifyListeners();
		return this.img;
	}
	
	public abstract void combine(JMTFImage input1, JMTFImage input2, JMTFImage output);

}
