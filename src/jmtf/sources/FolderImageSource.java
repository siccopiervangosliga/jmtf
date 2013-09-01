/**
 * Java Motion Tracking Framework
 */
package jmtf.sources;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

import jmtf.AbstractImageSource;
import jmtf.JMTFImage;

/**
 * Reads all Images in provided Folder
 * 
 * @author Luca Rossetto
 *
 */
public class FolderImageSource extends AbstractImageSource {
	
	private File folder;
	private FileFilter filter;
	private File[] images;
	private int index = 0;
	
	/**
	 * Constructor
	 * @param folder folder from which to read images
	 * @param filter filefilter to filter files in folder
	 */
	public FolderImageSource(File folder, FileFilter filter){
		this.filter = filter;
		this.folder = folder;
		
		images = this.folder.listFiles(this.filter);
	}
	
	/**
	 * Constructor with default filefilter
	 * @param folder folder from which to read images
	 */
	public FolderImageSource(File folder){
		this(folder, new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				String name = pathname.getAbsolutePath().toLowerCase();
				return name.endsWith("jpg") || name.endsWith("png") || name.endsWith("bmp");
			}
		});
	}

	/* (non-Javadoc)
	 * @see jmtf.AbstractImageSource#hasMoreImages()
	 */
	@Override
	public boolean hasMoreImages() {
		return this.images != null && index < this.images.length;
	}

	/* (non-Javadoc)
	 * @see jmtf.AbstractImageSource#getNextImage()
	 */
	@Override
	public JMTFImage getNextImage() {
		try {
			this.img = new JMTFImage(ImageIO.read(this.images[index]));
			this.img.setFrameNumber(this.index);
			this.img.setId(this.images[index++].getName());
			notifyListeners();
			return this.img;
		} catch (IOException e) {}
		return null;
	}
	
	public void setIndex(int index){
		this.index = index;
	}

}
