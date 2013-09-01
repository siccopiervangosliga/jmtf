/**
 * Java Motion Tracking Framework
 */
package jmtf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;


/**
 * @author Luca Rossetto
 * Main Image Class for JMTF
 */
public class JMTFImage {

	private int width, height;

	private int[] pixels;
	
	private boolean isGrayscale = false;
	
	private int frame_number = 0;
	
	private String id;
	
	private ROI roi;
	
	/**
	 * defines a Region of Interest
	 * @author Luca Rossetto
	 *
	 */
	public class ROI implements Cloneable{
		
		public ROI(){
			minX = 0;
			minY = 0;
			maxX = width - 1;
			maxY = height - 1;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + maxX;
			result = prime * result + maxY;
			result = prime * result + minX;
			result = prime * result + minY;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			ROI other = (ROI) obj;
			if (!getOuterType().equals(other.getOuterType())) {
				return false;
			}
			if (maxX != other.maxX) {
				return false;
			}
			if (maxY != other.maxY) {
				return false;
			}
			if (minX != other.minX) {
				return false;
			}
			if (minY != other.minY) {
				return false;
			}
			return true;
		}

		public ROI(int minx, int maxx, int miny, int maxy){
			minX = minx;
			maxX = maxx;
			minY = miny;
			maxY = maxy;
			
		}
		public int minX, maxX, minY, maxY;
		
		public ROI clone(){
			return new ROI(minX, maxX, minY, maxY);
		}

		public int getWidth(){
			return maxX - minX + 1;
		}
		
		public int getHeight(){
			return maxY - minY + 1;
		}
		
		public int getNumberOfPixels(){
			return getWidth() * getHeight();
		}
		
		@Override
		public String toString() {
			return "(" + minX + ", " + minY + ") to (" + maxX + ", " + maxY + ")";
		}

		private JMTFImage getOuterType() {
			return JMTFImage.this;
		}


		public boolean isInside(int x, int y) {
			return x >= minX && x <= maxX && y >= minY && y <= maxY;
		}
	}
	
	/**
	 * Creates a JMTFImage from a BufferedImage
	 * @param image
	 */
	public JMTFImage(BufferedImage image){
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.pixels = image.getRGB(0, 0, this.width, this.height, null, 0, this.width);
		this.roi = new ROI();
	}
	/**
	 * Creates a JMTFImage with specified dimensions
	 * @param width width of the new image
	 * @param height height of the new image
	 */
	public JMTFImage(int width, int height){
		this.width = width;
		this.height = height;
		this.pixels = new int[this.width * this.height];
		this.roi = new ROI();
	}
	/**
	 * Creates a JMTFImage with specified dimensions and specified grayscale flag
	 * @param width width of the new image
	 * @param height height of the new image
	 * @param isGrayScale true if all channels of the image have the same value
	 */
	public JMTFImage(int width, int height, boolean isGrayScale){
		this(width, height);
		this.isGrayscale = isGrayScale;
	}
	
	/**
	 * Creates a JMTFImage with specified dimensions, specified grayscale flag and an id
	 * @param width width of the new image
	 * @param height height of the new image
	 * @param isGrayScale true if all channels of the image have the same value
	 * @param frame_number the frame number of the image
	 */
	public JMTFImage(int width, int height, boolean isGrayScale, int frame_number){
		this(width, height, isGrayScale);
		this.frame_number = frame_number;
	}
	
	/**
	 * Creates a JMTFImage with specified dimensions, specified grayscale flag and an id
	 * @param width width of the new image
	 * @param height height of the new image
	 * @param isGrayScale true if all channels of the image have the same value
	 * @param frame_number the frame number of the image
	 * @param id the unique id of an image
	 */
	public JMTFImage(int width, int height, boolean isGrayScale, int frame_number, String id){
		this(width, height, isGrayScale, frame_number);
		this.id = id;
	}
	
	/**
	 * returns the grayscale-flag
	 * @return true if it is a grayscaleimage
	 */
	public boolean isGrayscale() {
		return isGrayscale;
	}
	/**
	 * sets the graysclae-flag
	 * @param isGreyscale flag
	 */
	public void setGrayscale(boolean isGreyscale) {
		this.isGrayscale = isGreyscale;
	}
	/**
	 * Returns an int-Array containing all the Pixelvalues
	 * @return array with Pixelvalues
	 */
	public int[] getPixels(){
		return this.pixels;
	}
	
	/**
	 * Returns the value for the specified pixel (x,y)
	 * @param x the x-coordinate of the pixel
	 * @param y the y-coordinate of the pixel
	 * @return the value of the pixel, 0 if the coordinates are out of range
	 */
	public int getPixel(int x, int y){
		if(x < 0 || y < 0 || x > this.width || y > this.height){
			return 0;
		}
		int pos = x + this.width * y;
		if(pos < this.pixels.length){
			return this.pixels[pos];
		}
		return 0;
	}
	
	/**
	 * Sets the value for the specified pixel(x,y)
	 * @param x the x-coordinate of the pixel
	 * @param y the y-coordinate of the pixel
	 * @param color the value of the pixel to set
	 */
	public void setPixel(int x, int y, int color){
		int pos = x + this.width * y;
		if(pos < this.pixels.length){
			this.pixels[pos] = color;
		}
	}
	/**
	 * Sets a new pixel-array
	 * @param pixels new pixel array
	 */
	public void setPixels(int[] pixels) {
		this.pixels = pixels;
	}
	/**
	 * Extracts the red-channel form a pixelvalue
	 * @param color the pixelvalue
	 * @return the red channel
	 */
	public static int getRed(int color){
		return (color >> 16 & 0xFF);
	}
	
	/**
	 * Extracts the green-channel form a pixelvalue
	 * @param color the pixelvalue
	 * @return the green channel
	 */
	public static int getGreen(int color){
		return (color >> 8 & 0xFF);
	}
	
	/**
	 * Extracts the blue-channel form a pixelvalue
	 * @param color the pixelvalue
	 * @return the blue channel
	 */
	public static int getBlue(int color){
		return (color & 0xFF);
	}
	
	/**
	 * Combines red, green and blue channel to a pixelvalue
	 * @param red
	 * @param green
	 * @param blue
	 * @return the pixelvalue
	 */
	public static int getColor(int red, int green, int blue){
		return (blue & 0xFF) | ((green & 0xFF) << 8) | ((red & 0xFF) << 16);
	}
	
	/**
	 * Returns the width of the Image
	 * @return width of the image
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of the Image
	 * @return height of the image
	 */
	public int getHeight() {
		return height;
	}

	public JMTFImage copy(){
		JMTFImage _ret = new JMTFImage(this.width, this.height);
		_ret.pixels = this.pixels.clone();
		_ret.roi = roi.clone();
		_ret.frame_number = this.frame_number;
		_ret.isGrayscale = this.isGrayscale;
		return _ret;
	}
	
	public void writeToFile(File file, String format) throws IOException{
		ImageIO.write(this.toBufferedImage(null), format, file);
	}
	
	public BufferedImage toBufferedImage(BufferedImage img){
		if(img == null || img.getWidth() != this.getWidth() || img.getHeight() != this.getHeight()){
			img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
		}
		img.setRGB(0, 0, this.getWidth(), this.getHeight(), this.getPixels(), 0, this.getWidth());
		return img;
	}
	
	public int getAvgColor(){
		int r = 0, g = 0, b = 0;
		/*for(int i = 0; i < this.pixels.length; ++i){
			r += getRed(this.pixels[i]);
			g += getGreen(this.pixels[i]);
			b += getBlue(this.pixels[i]);
		}*/
		for (int x = roi.minX; x <= roi.maxX; ++x) {
			for (int y = roi.minY; y <= roi.maxY; ++y) {
				int c = getPixel(x, y);
				r += getRed(c);
				g += getGreen(c);
				b += getBlue(c);
			}
		}
		return getColor(r / this.pixels.length, g / this.pixels.length, b / this.pixels.length);
	}
	
	public int getMedianColor(){
		/*int[] r = new int[this.pixels.length], g  = new int[this.pixels.length], b = new int[this.pixels.length];
		for(int i = 0; i < this.pixels.length; ++i){
			r[i] = getRed(this.pixels[i]);
			g[i] = getGreen(this.pixels[i]);
			b[i] = getBlue(this.pixels[i]);
		}*/
		
		int[] r = new int[roi.getNumberOfPixels()], g  = new int[roi.getNumberOfPixels()], b = new int[roi.getNumberOfPixels()];
		int i = 0;
		for (int x = roi.minX; x <= roi.maxX; ++x) {
			for (int y = roi.minY; y <= roi.maxY; ++y) {
				int c = getPixel(x, y);
				r[i] = getRed(c);
				g[i] = getGreen(c);
				b[i++] = getBlue(c);
			}
		}
		
		Arrays.sort(r);
		Arrays.sort(g);
		Arrays.sort(b);
		
		int med = getColor(r[this.pixels.length / 2], g[this.pixels.length / 2], b[this.pixels.length / 2]);
		
		r = null;
		g = null;
		b = null;
		
		int index = 0, dist, min_dist = Integer.MAX_VALUE;
		for(i = 0; i < this.pixels.length; ++i){
			dist = squaredColorDistance(this.pixels[i], med);
			if(dist < min_dist){
				if(dist == 0){
					return med;
				}
				index = i;
				min_dist = dist;
			}
		}
		
		return this.pixels[index];
	}
	
	/**
	 * Returns the frame number of an JMTFImage
	 * @return the frame number
	 */
	public int getFrameNumber(){
		return this.frame_number;
	}
	
	public void setFrameNumber(int frame_number){
		this.frame_number = frame_number;
	}
	
	/**
	 * Returns the unique id of an image
	 * @return the id
	 */
	public String getId(){
		return this.id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public ROI getROI(){
		return this.roi;
	}
	
	/**
	 * crops the image to region of interest
	 */
	public void cropToROI(){
		int[] newPixels = new int[roi.getWidth() * roi.getHeight()];
		int i = 0;
		for(int y = roi.minY; y <= roi.maxY; ++y){
			for(int x = roi.minX; x <= roi.maxX; ++x){
				newPixels[i++] = getPixel(x, y);
			}
		}
		this.pixels = newPixels;
		this.width = roi.getWidth();
		this.height = roi.getHeight();
		this.roi = new ROI();
	}
	
	/**
	 * Calculates (r1-r2)*(r1-r2) + (g1-g2)*(g1-g2) + (b1-b2)*(b1-b2)
	 * @param color1
	 * @param color2
	 * @return the squared euclidean distance between color1 and color2
	 */
	public static int squaredColorDistance(int color1, int color2){
		int r1 = JMTFImage.getRed(color1), g1 = JMTFImage.getGreen(color1), b1 = JMTFImage.getBlue(color1);
		int r2 = JMTFImage.getRed(color2), g2 = JMTFImage.getGreen(color2), b2 = JMTFImage.getBlue(color2);
		return (r1-r2)*(r1-r2) + (g1-g2)*(g1-g2) + (b1-b2)*(b1-b2);
	}
	
	@Override
	protected JMTFImage clone(){
		return this.copy();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + frame_number;
		result = prime * result + height;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (isGrayscale ? 1231 : 1237);
		result = prime * result + Arrays.hashCode(pixels);
		result = prime * result + ((roi == null) ? 0 : roi.hashCode());
		result = prime * result + width;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		JMTFImage other = (JMTFImage) obj;
		if (frame_number != other.frame_number) {
			return false;
		}
		if (height != other.height) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (isGrayscale != other.isGrayscale) {
			return false;
		}
		if (!Arrays.equals(pixels, other.pixels)) {
			return false;
		}
		if (roi == null) {
			if (other.roi != null) {
				return false;
			}
		} else if (!roi.equals(other.roi)) {
			return false;
		}
		if (width != other.width) {
			return false;
		}
		return true;
	}
	/**
	 * @return
	 */
	public boolean isEmpty() {
		return this.width == 0 || this.height == 0;
	}
	
	

}
