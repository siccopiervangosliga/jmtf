/**
 * Java Motion Tracking Framework
 */
package jmtf.helper;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Enumeration;

import javax.swing.JFrame;

import jmtf.JMTFImage;
import jmtf.JMTFImage.ROI;
import jmtf.TrackingDataSet;
import jmtf.tracker.Blob;

/**
 * @author Luca Rossetto
 *
 */
public class CombinedDataDisplay extends JFrame {
	private static final long serialVersionUID = -9069053238154336890L;
	
	private float zoom;
	private BufferedImage img, canvas;
	private TrackingDataSet[] data;
	private ROI roi;
	
	public CombinedDataDisplay(String title, float zoom){
		super(title);
		this.zoom = zoom;
		this.setVisible(true);
	}
	
	public CombinedDataDisplay(String title){
		this(title, 1);
	}
	
	public BufferedImage updateData(JMTFImage img, TrackingDataSet...data){
		this.data = data;
		if(this.img == null){
			this.img = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		}
		img.toBufferedImage(this.img);
		this.roi = img.getROI();
		this.setSize((int) (this.img.getWidth() * this.zoom),
				(int) (this.img.getHeight() * this.zoom));
		
		if (this.img != null) {
			float i = 0;
			this.canvas = new BufferedImage((int) (this.img.getWidth() * this.zoom),
					(int) (this.img.getHeight() * this.zoom), BufferedImage.TYPE_INT_RGB);
			
			Graphics g2 = this.canvas.getGraphics();
			
			g2.drawImage(this.img, 0, 0,
					(int) (this.img.getWidth() * this.zoom),
					(int) (this.img.getHeight() * this.zoom), null);
			
			for(TrackingDataSet set : this.data){
				g2.setColor(Color.getHSBColor((i++) / this.data.length, 1, 1));
				
				Enumeration<Integer> keys = set.tracks.keys();
				while(keys.hasMoreElements()){
					int k = keys.nextElement();
					Blob b = set.tracks.get(k);
					if(b == null){
						continue;
					}
					int x = (int)(b.getCenter()[0]*this.zoom);
					int y = (int)(b.getCenter()[1]*this.zoom);
					
					g2.drawLine(x, y-5, x, y+5);
					g2.drawLine(x-5, y, x+5, y);
					
					g2.drawString("id: " + k, x + 10, y + 10);
				}
			}
			
			g2.setColor(Color.RED);
			g2.drawRect((int)(roi.minX * this.zoom), (int)(roi.minY * this.zoom), (int)(roi.getWidth() * this.zoom), (int)(roi.getHeight() * this.zoom));
		}
		
		repaint();
		return this.canvas;
	}
	
	@Override
	public void paint(Graphics g) {
		
			g.drawImage(canvas, 0, 0, null);
		
	}

}
