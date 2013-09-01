/**
 * Java Motion Tracking Framework
 */
package jmtf.helper;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.imageio.ImageIO;

import jmtf.TrackingDataSet;
import jmtf.TrackingDataUpdateListener;
import jmtf.tracker.Blob;

/**
 * @author Luca Rossetto
 * 
 */
public class TrackingDataSetToFileExporter implements
		TrackingDataUpdateListener {

	private File folder;
	private String prefix, format;
	private BufferedImage img;
	private TrackingDataSet track;
	private int size;
	private Color col = Color.red;
	private int lineWidth = 1;
	
	/**
	 * 
	 * @param folder folder in which to save the images
	 * @param prefix prefix to add to the filename
	 * @param format format in which to save the images
	 * @param size size of the mark for the blobs
	 */
	public TrackingDataSetToFileExporter(File folder, String prefix, String format, int size){
		this.folder = folder;
		this.prefix = prefix;
		this.format = format;
		this.size = size;
	}
	
	public TrackingDataSetToFileExporter(File folder, String prefix, String format){
		this(folder, prefix, format, 5);
	}
	
	public void setColor(Color c){
		this.col = c;
	}
	
	public void setSize(int size){
		this.size = size;
	}
	
	public void setLineWidth(int width){
		this.lineWidth = width;
	}
	
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
		
		Graphics g = this.img.getGraphics();
		
		g.setColor(this.col);
		
		Enumeration<Integer> keys = this.track.tracks.keys();
		while(keys.hasMoreElements()){
			Blob b = this.track.tracks.get(keys.nextElement());
			int x = b.getCenter()[0];
			int y = b.getCenter()[1];
			
			for(int i = -(lineWidth - 1) / 2; i <= lineWidth / 2; ++i){
				g.drawLine(x + i, y - this.size, x + i, y + this.size);
				g.drawLine(x - this.size, y + i, x + this.size, y + i);
			}
			
		}
		
		try {
			ImageIO.write(this.img, this.format, new File(this.folder, this.prefix + this.track.image.getFrameNumber() + "." + this.format));
		} catch (IOException e) {
			System.err.println("Could not write Image");
			e.printStackTrace();
		}

	}

}
