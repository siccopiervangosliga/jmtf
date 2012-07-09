/**
 * Java Motion Tracking Framework
 */
package jmtf.helper;

import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import jmtf.ImageUpdateListener;
import jmtf.ImageUpdater;
import jmtf.JMTFImage;

/**
 * @author Luca Rossetto
 * 
 */
public class ImageDisplay extends JFrame implements ImageUpdateListener {


	private static final long serialVersionUID = -2062263887955682847L;
	private BufferedImage img;
	private float zoom = 1.0f;
	private String title;

	/*
	 * (non-Javadoc)
	 * 
	 * @see jmtf.ImageUpdateListener#update(java.awt.image.BufferedImage)
	 */
	@Override
	public void update(JMTFImage img) {
		if(img == null){
			return;
		}	
		if(this.img == null){
			this.img = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		}
		img.toBufferedImage(this.img);
		this.setSize((int) (this.img.getWidth() * this.zoom),
				(int) (this.img.getHeight() * this.zoom));
		repaint();

	}

	@Override
	public void paint(Graphics g) {
		if (this.img != null) {
			g.drawImage(this.img, 0, 0,
					(int) (this.img.getWidth() * this.zoom),
					(int) (this.img.getHeight() * this.zoom), null);
		}
	}

	/**
	 * @param title Title of the window
	 * @throws HeadlessException
	 */
	public ImageDisplay(String title) throws HeadlessException {
		this(title, 1.0f);
	}

	/**
	 * @param title title of the window 
	 * @param zoom zoom of the image to display
	 */
	public ImageDisplay(String title, float zoom) {
		super(title + " (" + (zoom * 100) + "%)");
		this.zoom = zoom;
		this.title = title;
		this.setVisible(true);
	}

	/**
	 * 
	 * @param updater the ImageUpdater to which it adds itself
	 * @param title title of the window 
	 * @param zoom zoom of the image to display
	 */
	public ImageDisplay(ImageUpdater updater, String title, float zoom){
		this(title, zoom);
		updater.addImageUpdateListener(this);
	}
	
	/**
	 * returns the current zoom factor for the image to display
	 * @return the zoom factor
	 */
	public float getZoom() {
		return zoom;
	}

	/**
	 * sets the zoom factor for the display
	 * @param zoom the zoom factor
	 */
	public void setZoom(float zoom) {
		this.zoom = zoom;
		this.setTitle(this.title + " (" + (this.zoom * 100) + "%)");
	}

}
