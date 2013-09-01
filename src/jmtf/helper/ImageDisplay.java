/**
 * Java Motion Tracking Framework
 */
package jmtf.helper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

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
	private JPanel canvas;
	private float zoom = 1.0f;
	private String title;
	private JMTFImage.ROI roi;
	private static final BufferedImage emptyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

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
//		if(this.img == null){
//			this.img = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
//		}
		if(!img.isEmpty()){
			this.img = img.toBufferedImage(null);
			this.roi = img.getROI();
			canvas.setPreferredSize(new Dimension((int)((this.img.getWidth() + 1) * this.zoom),(int) ((this.img.getHeight() + 1) * this.zoom)));
		}else{
			this.img = emptyImage;
			roi = null;
			canvas.setPreferredSize(new Dimension(1,1));
		}
		
		canvas.repaint();
		pack();
	}


	/**
	 * @param title Title of the window
	 */
	public ImageDisplay(String title){
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
		this.canvas = new JPanel(){

			/**
			 * 
			 */
			private static final long serialVersionUID = 2111042948007355381L;

			@Override
			public void paintComponent(Graphics g) {
				if (img != null) {
					g.drawImage(img, 0, 0,
							(int) (img.getWidth() * ImageDisplay.this.zoom),
							(int) (img.getHeight() * ImageDisplay.this.zoom), null);
					g.setColor(Color.RED);
					if(roi != null){
						g.drawRect((int)(roi.minX * ImageDisplay.this.zoom), (int)(roi.minY * ImageDisplay.this.zoom), (int)(roi.getWidth() * ImageDisplay.this.zoom), (int)(roi.getHeight() * ImageDisplay.this.zoom));
					}
					
				}
			}
			
		};
		getContentPane().add(canvas);
		
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
