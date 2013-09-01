/**
 * Java Motion Tracking Framework
 */
package jmtf.helper;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.util.Enumeration;

import javax.swing.JFrame;

import jmtf.TrackingDataSet;
import jmtf.TrackingDataUpdateListener;
import jmtf.TrackingDataUpdater;
import jmtf.tracker.Blob;

/**
 * @author Luca Rossetto
 *
 */
public class TrackingDataDisplay extends JFrame implements
		TrackingDataUpdateListener {

	private static final long serialVersionUID = -907058728823674308L;
	private TrackingDataSet track;
	private BufferedImage img;
	private float zoom = 1.0f;
	private String title;
	private Color col = Color.red;

	/* (non-Javadoc)
	 * @see jmtf.TrackingDataUpdateListener#update(jmtf.TrackingDataSet)
	 */
	@Override
	public void update(TrackingDataSet data) {
		if(data == null){
			return;
		}
		this.track = data.copy();
		if(this.img == null){
			this.img = new BufferedImage(data.image.getWidth(), data.image.getHeight(), BufferedImage.TYPE_INT_RGB);
		}
		data.image.toBufferedImage(this.img);
		this.setSize((int) (this.img.getWidth() * this.zoom),
				(int) (this.img.getHeight() * this.zoom));
		repaint();
	}
	

	/**
	 * @param title Title of the window
	 */
	public TrackingDataDisplay(String title){
		this(title, 1.0f);
	}

	/**
	 * @param title title of the window 
	 * @param zoom zoom of the image to display
	 */
	public TrackingDataDisplay(String title, float zoom) {
		super(title + " (" + (zoom * 100) + "%)");
		this.zoom = zoom;
		this.title = title;
		this.setVisible(true);
	}

	/**
	 * 
	 * @param updater the TrackingDataUpdater to which it adds itself
	 * @param title title of the window 
	 * @param zoom zoom of the image to display
	 */
	public TrackingDataDisplay(TrackingDataUpdater updater, String title, float zoom){
		this(title, zoom);
		updater.addTrackingDataUpdateListener(this);
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

	public void setColor(Color c){
		this.col = c;
	}
	
	@Override
	public void paint(Graphics g) {
		if (this.img != null) {

			g.drawImage(this.img, 0, 0,
					(int) (this.img.getWidth() * this.zoom),
					(int) (this.img.getHeight() * this.zoom), null);
			
			g.setColor(this.col);
			
			Enumeration<Integer> keys = this.track.tracks.keys();
			while(keys.hasMoreElements()){
				int k = keys.nextElement();
				Blob b = this.track.tracks.get(k);
				if(b == null){
					continue;
				}
				int x = (int)(b.getCenter()[0]*this.zoom);
				int y = (int)(b.getCenter()[1]*this.zoom);
				
				g.drawLine(x, y-5, x, y+5);
				g.drawLine(x-5, y, x+5, y);
				
				g.drawString("id: " + k, x + 6, y + 6);
			}
			
		}
	}

}
