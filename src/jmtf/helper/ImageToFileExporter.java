/**
 * Java Motion Tracking Framework
 */
package jmtf.helper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import jmtf.ImageUpdateListener;
import jmtf.JMTFImage;

/**
 * @author Luca Rossetto
 *
 */
public class ImageToFileExporter implements ImageUpdateListener {

	private File folder;
	private String prefix, format;
	private BufferedImage buf;
	
	public ImageToFileExporter(File folder, String prefix, String format){
		this.folder = folder;
		this.prefix = prefix;
		this.format = format;
	}
	
	/* (non-Javadoc)
	 * @see jmtf.ImageUpdateListener#update(jmtf.JMTFImage)
	 */
	@Override
	public void update(JMTFImage img) {
		if(img == null){
			return;
		}
		buf = img.toBufferedImage(buf);
		try {
			ImageIO.write(buf, this.format, new File(this.folder, this.prefix + img.getFrameNumber() + "." + this.format));
		} catch (IOException e) {
			System.err.println("Could not write Image");
			e.printStackTrace();
		}
	}

}
