/**
 * Java Motion Tracking Framework
 */
package jmtf.exporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;

import jmtf.TrackingDataSet;
import jmtf.TrackingDataSource;

/**
 * @author Luca Rossetto
 * Exports all tracking data from a TrackingDataSource as CSV
 * the format is frame,blob id, blob x coordinate, blob y coordinate
 */
public class CSVExporter {

	private PrintWriter writer;
	private TrackingDataSource source;
	
	public CSVExporter(TrackingDataSource source, OutputStream stream){
		this.source = source;
		this.writer = new PrintWriter(stream);
	}
	
	public CSVExporter(TrackingDataSource source, File file) throws FileNotFoundException{
		this.source = source;
		this.writer = new PrintWriter(file);
	}
	/**
	 * Exports the next tracking data set
	 * @return true if successful
	 */
	public boolean exportOne(){
		TrackingDataSet data = this.source.getNextTrackingDataSet();
		if(data == null){
			return false;
		}
		
		Enumeration<Integer> keys = data.tracks.keys();
		while(keys.hasMoreElements()){
			int key = keys.nextElement();
			this.writer.print(data.image.getFrameNumber());
			this.writer.print(',');
			this.writer.print(key);
			this.writer.print(',');
			
			int[] pos = data.tracks.get(key).getCenter();
			
			this.writer.print(pos[0]);
			this.writer.print(',');
			this.writer.println(pos[1]);
			
			this.writer.flush();
			
		}
		return true;
	}
	
	/**
	 * exports all remaining tracking data sets
	 */
	public void exportAll(){
		
		while(exportOne()){/* do nothing */};
	}
	
	public void finalize() throws Throwable{
		this.writer.flush();
		this.writer.close();
		super.finalize();
	}
}
