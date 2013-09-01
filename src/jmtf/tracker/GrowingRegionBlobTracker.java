/**
 * Java Motion Tracking Framework
 */
package jmtf.tracker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;


import jmtf.ImageSource;
import jmtf.JMTFImage;
import jmtf.TrackingDataSet;
import jmtf.manipulators.color.ImageToGrayscale;

/**
 * @author Luca Rossetto
 *
 */
public class GrowingRegionBlobTracker extends AbstractTracker{

	private int threshold = 255;
	
	private ImageToGrayscale i2g;
	private HashMap<Integer, LinkedList<LineBlob>> lineBlobs = new HashMap<Integer, LinkedList<LineBlob>>();
	
	public GrowingRegionBlobTracker(ImageSource source, int threshold){
		this.source = source;
		this.i2g = new ImageToGrayscale(this.source);
		this.threshold = threshold;
	}
	
	public GrowingRegionBlobTracker(ImageSource source){
		this(source, 255);
	}
	
	protected TrackingDataSet track(){
		JMTFImage img = this.i2g.getNextImage();
		if(img == null){
			return null;
		}
		
		this.lineBlobs.clear();
		
		int blobCount = 0;
		
		
		
		for(int y = img.getROI().minY; y <= img.getROI().maxY; ++y){
			for(int x = img.getROI().minX; x <= img.getROI().maxX; ++x){
				
				int val = JMTFImage.getRed(img.getPixel(x, y));
				
				if(val >= threshold){
					int start = x;
					
					for(; val >= threshold && ++x <= img.getROI().maxX; val = JMTFImage.getRed(img.getPixel(x, y)));
					
					int end = x - 1;
					
					if(!this.lineBlobs.containsKey(y)){
						this.lineBlobs.put(y, new LinkedList<LineBlob>());
					}
					
					int blobid = -1;
					
					/*check for overlapping LineBlobs in line above to determine blobid*/
					if(this.lineBlobs.containsKey(y-1)){
						LineBlob lb;
						Iterator<LineBlob> iter = this.lineBlobs.get(y-1).iterator();
						while(iter.hasNext()){
							lb = iter.next();
							if(!(lb.min > end || lb.max < start)){ //check if range is overlapping
								if(blobid == -1){
									blobid = lb.id;
								}else{ //found multiple overlapping lineblobs
									mergeLineBlobs(blobid, lb.id);
								}
							}
						}
					}
					if(blobid == -1){
						blobid = ++blobCount;
					}
					this.lineBlobs.get(y).add(new LineBlob(start, end, blobid));
				}
			}
		}
		
		/*merge lineblobs to blobs*/
		HashMap<Integer, RectBlob> blobMap = new HashMap<Integer, RectBlob>();
		
		Iterator<Integer> lines = this.lineBlobs.keySet().iterator();
		Iterator<LineBlob> lineblobs;
		LineBlob lb;
		RectBlob b;
		
		while(lines.hasNext()){
			int y = lines.next();
			lineblobs = this.lineBlobs.get(y).iterator();
			while(lineblobs.hasNext()){
				lb = lineblobs.next();
				if(!blobMap.containsKey(lb.id)){
					b = new RectBlob(lb.min, y, lb.max, y, 0, lb.id);
					blobMap.put(lb.id, b);
				}else{
					b = blobMap.get(lb.id);
				}
		
				b.max_x = Math.max(b.max_x, lb.max);
				b.max_y = Math.max(b.max_y, y);
				b.min_x = Math.min(b.min_x, lb.min);
				b.min_y = Math.min(b.min_y, y);
			}
		}
		
		/*relable blob ids*/
		ConcurrentHashMap<Integer, Blob> blobs = new ConcurrentHashMap<Integer, Blob>();
		Iterator<Integer> keys = blobMap.keySet().iterator();
		int i = 0;
		while(keys.hasNext()){
			b = blobMap.get(keys.next());
			b.id = i;
			b.updateImageRelatedData(img);
			blobs.put(i++, b);
		}
		return new TrackingDataSet(blobs, img);
		
	}
	
	private void mergeLineBlobs(int mainid, int slaveid) {
		Iterator<Integer> lines = this.lineBlobs.keySet().iterator();
		Iterator<LineBlob> blobs;
		LineBlob lb;
		
		while(lines.hasNext()){
			blobs = this.lineBlobs.get(lines.next()).iterator();
			while(blobs.hasNext()){
				lb = blobs.next();
				if(lb.id == slaveid){
					lb.id = mainid;
				}
			}
		}
	}
}
class LineBlob{
	
	public int min, max, id;
	
	LineBlob(int min, int max, int id){
		this.min = min;
		this.max = max;
		this.id = id;
	}
	
}

