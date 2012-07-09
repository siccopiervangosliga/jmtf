/**
 * Java Motion Tracking Framework
 */
package tests;

import java.io.File;
import java.io.FileNotFoundException;

import jmtf.exporter.CSVExporter;
import jmtf.helper.ImageDisplay;
import jmtf.helper.ImageToFileExporter;
import jmtf.helper.TrackingDataDisplay;
import jmtf.manipulators.BackgroundSubtractor;
import jmtf.manipulators.color.ColorSpaceTransform;
import jmtf.manipulators.color.ImageChannelToGrayscaleImage;
import jmtf.sources.FolderImageSource;
import jmtf.tracker.GrowingRegionBlobTracker;
import jmtf.transformers.BrightestBlobFilter;
import jmtf.transformers.SingleLinearInterpolator;
import jmtf.transformers.SingleLinearOutlierDetector;

/**
 * @author Luca Rossetto
 *
 */
public class Pipeline1 {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		File folder = new File("videos\\1920x1080at30nr2");
		int width = 1920, height = 1080;
		File output = new File(folder, "track_pipeline1");

		long start = System.currentTimeMillis();
		
		System.out.println("Start tracking");
		
		FolderImageSource source = new FolderImageSource(folder);
		ColorSpaceTransform cst = new ColorSpaceTransform(source, -0.5869f, -0.6160f, -0.5255f, 0.6668f, 0.0004f, -0.7452f, 0.4593f, -0.7877f, 0.4105f);
		BackgroundSubtractor bs = new BackgroundSubtractor(cst, width, height);

		ImageChannelToGrayscaleImage c2i = new ImageChannelToGrayscaleImage(bs, ImageChannelToGrayscaleImage.GREEN);
		
		ImageDisplay display1 = new ImageDisplay(source, "source", 0.5f);
		ImageDisplay display2 = new ImageDisplay(cst, "cst", 0.5f);
		ImageDisplay display3 = new ImageDisplay(c2i, "c2i", 0.5f);
		ImageDisplay display4 = new ImageDisplay(bs, "bs", 0.5f);

		GrowingRegionBlobTracker tracker = new GrowingRegionBlobTracker(c2i, 100);
		
		TrackingDataDisplay tdisplay1 = new TrackingDataDisplay(tracker, "tracker", 0.5f);
		
		BrightestBlobFilter bbf = new BrightestBlobFilter(tracker);
		SingleLinearOutlierDetector slo = new SingleLinearOutlierDetector(bbf, 200);
		SingleLinearInterpolator sli = new SingleLinearInterpolator(slo, 10);
		
		File output_folder = new File("pipeline1_out");
		
		ImageToFileExporter ex1 = new ImageToFileExporter(output_folder, "cst", "png");
		ImageToFileExporter ex2 = new ImageToFileExporter(output_folder, "c2i", "png");
		ImageToFileExporter ex3 = new ImageToFileExporter(output_folder, "bg", "png");
		ImageToFileExporter ex4 = new ImageToFileExporter(output_folder, "bs", "png");
		
		cst.addImageUpdateListener(ex1);
		c2i.addImageUpdateListener(ex2);
		bs.addBackgroundUpdateListener(ex3);
		bs.addImageUpdateListener(ex4);
		
		CSVExporter csv = new CSVExporter(sli, output);
		csv.exportAll();
		
		
		display1.dispose();
		display2.dispose();
		display3.dispose();
		display4.dispose();
		tdisplay1.dispose();
		
		System.out.println("pipeline 1 done in " + (System.currentTimeMillis() - start) + "ms");
	}

}
