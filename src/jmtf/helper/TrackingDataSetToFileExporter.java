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
		
		g.setColor(Color.red);
		
		Enumeration<Integer> keys = this.track.tracks.keys();
		while(keys.hasMoreElements()){
			Blob b = this.track.tracks.get(keys.nextElement());
			int x = (int)b.getCenter()[0];
			int y = (int)b.getCenter()[1];
			
			g.drawLine(x, y - this.size, x, y + this.size);
			g.drawLine(x - this.size, y, x + this.size, y);
		}
		
		try {
			ImageIO.write(this.img, this.format, new File(this.folder, this.prefix + this.track.image.getFrameNumber() + "." + this.format));
		} catch (IOException e) {
			System.err.println("Could not write Image");
			e.printStackTrace();
		}

	}

}
