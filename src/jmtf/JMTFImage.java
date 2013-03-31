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
	
	/**
	 * Creates a JMTFImage from a BufferedImage
	 * @param image
	 */
	public JMTFImage(BufferedImage image){
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.pixels = image.getRGB(0, 0, this.width, this.height, null, 0, this.width);
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
	}
	/**
	 * Creates a JMTFImage with specified dimensions and specified grayscale flag
	 * @param width width of the new image
	 * @param height height of the new image
	 * @param isGreyScale true if all channels of the image have the same value
	 */
	public JMTFImage(int width, int height, boolean isGrayScale){
		this(width, height);
		this.isGrayscale = isGrayScale;
	}
	
	/**
	 * Creates a JMTFImage with specified dimensions, specified grayscale flag and an id
	 * @param width width of the new image
	 * @param height height of the new image
	 * @param isGreyScale true if all channels of the image have the same value
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
	 * @param isGreyScale true if all channels of the image have the same value
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
	 * @param isGrayscale flag
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
		_ret.frame_number = this.frame_number;
		_ret.isGrayscale = this.isGrayscale;
		return _ret;
	}
	
	public void writeToFile(File file, String format) throws IOException{
		ImageIO.write(this.toBufferedImage(null), format, file);
	}
	
	public BufferedImage toBufferedImage(BufferedImage img){
		if(img == null){
			img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
		}
		img.setRGB(0, 0, this.getWidth(), this.getHeight(), this.getPixels(), 0, this.getWidth());
		return img;
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + frame_number;
		result = prime * result + (isGrayscale ? 1231 : 1237);
		result = prime * result + Arrays.hashCode(pixels);
		result = prime * result + width;
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JMTFImage other = (JMTFImage) obj;
		if (height != other.height)
			return false;
		if (frame_number != other.frame_number)
			return false;
		if (isGrayscale != other.isGrayscale)
			return false;
		if (width != other.width)
			return false;
		if (!Arrays.equals(pixels, other.pixels))
			return false;
		return true;
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
	
	

}
