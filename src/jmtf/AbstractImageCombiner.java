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
	
	
	public AbstractImageCombiner(ImageSource source1, ImageSource source2){
		this.source1 = source1;
		this.source2 = source2;
	}
	
	/* (non-Javadoc)
	 * @see jmtf.AbstractImageSource#hasMoreImages()
	 */
	@Override
	public boolean hasMoreImages() {
		return this.source1.hasMoreImages() && this.source2.hasMoreImages();
	}

	/* (non-Javadoc)
	 * @see jmtf.AbstractImageSource#getNextImage()
	 */
	@Override
	public JMTFImage getNextImage() {
		JMTFImage in1 = this.source1.getNextImage(), in2 = this.source2.getCurrentImage();
		this.img = new JMTFImage(Math.min(in1.getWidth(), in2.getWidth()), Math.min(in1.getHeight(), in2.getHeight()));
		combine(in1, in2, this.img);
		notifyListeners();
		return this.img;
	}
	
	public abstract void combine(JMTFImage input1, JMTFImage input2, JMTFImage output);

}
