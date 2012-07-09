/**
 * Java Motion Tracking Framework
 */
package jmtf;



/**
 * Abstract Class for ImageManipulators, implements basic functionality.
 * When implementing a new ImageSource inherit from this Class.
 * @author Luca Rossetto
 *
 */
public abstract class AbstractImageManipulator extends AbstractImageSource
		implements ImageManipulator, ImageUpdateListener {
	
	@Override
	public void update(JMTFImage img) {
		this.manipulate(img);
		notifyListeners();
	}

	private ImageSource source;

	public AbstractImageManipulator(ImageSource source){
		this.source = source;
	}
	
	/* (non-Javadoc)
	 * @see jmtf.AbstractImageSource#hasMoreImages()
	 */
	@Override
	public boolean hasMoreImages() {
		return this.source.hasMoreImages();
	}

	/* (non-Javadoc)
	 * @see jmtf.AbstractImageSource#getNextImage()
	 */
	@Override
	public JMTFImage getNextImage(){
		if(this.source.hasMoreImages()){
			this.img = this.source.getNextImage();
			this.manipulate(this.img);
			notifyListeners();
			return this.img;
		}
		return null;
	}

	@Override
	public ImageSource getSource() {
		return this.source;
	}

	@Override
	public void setSource(ImageSource source) {
		this.source = source;
	}
	


}
