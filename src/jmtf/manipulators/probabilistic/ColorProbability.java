/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.probabilistic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import jmtf.JMTFImage;

/**
 * estimates the probability of a color being part of an object bases on positive and negative examples
 * @author Luca Rossetto
 *
 */
public class ColorProbability {

	private HashMap<Integer, ProbabilityContainer> probabilities = new HashMap<Integer, ProbabilityContainer>();
	private final boolean normalized;
	
	private HashSet<Integer> positive = new HashSet<Integer>(), negative = new HashSet<Integer>();
	
	/**
	 * 
	 * @param normalise determines if the training images should be normalised (every color contained in an image is added only once per method call)
	 */
	public ColorProbability(boolean normalise){
		this.normalized = normalise;
	}
	
	public ColorProbability(){
		this(false);
	}
	
	public void addPositive(int red, int green, int blue){
		int index = getIndex(red, green, blue);
		addPositiveIndex(index);
	}

	protected void addPositiveIndex(int index) {
		if(!probabilities.containsKey(index)){
			probabilities.put(index, new ProbabilityContainer());
		}
		probabilities.get(index).addPositive();
	}
	
	public void addPositive(JMTFImage img){
		for(int y = img.getROI().minY; y <= img.getROI().maxY; ++y){
			for(int x = img.getROI().minX; x <= img.getROI().maxX; ++x){
				int col = img.getPixel(x, y);
				if(normalized){
					positive.add(getIndex(JMTFImage.getRed(col), JMTFImage.getGreen(col), JMTFImage.getBlue(col)));
				}else{
					addPositive(JMTFImage.getRed(col), JMTFImage.getGreen(col), JMTFImage.getBlue(col));
				}
			}
		}
		addTemp();
	}
	
	public void addNegative(JMTFImage img){
		for(int y = img.getROI().minY; y <= img.getROI().maxY; ++y){
			for(int x = img.getROI().minX; x <= img.getROI().maxX; ++x){
				int col = img.getPixel(x, y);
				if(normalized){
					negative.add(getIndex(JMTFImage.getRed(col), JMTFImage.getGreen(col), JMTFImage.getBlue(col)));
				}else{
					addNegative(JMTFImage.getRed(col), JMTFImage.getGreen(col), JMTFImage.getBlue(col));
				}
			}
		}
		addTemp();
	}
	/**
	 * adds an example to the estimation. The part of the image within the ROI is regarded as positive example, the rest as negative example.
	 * @param img
	 */
	public void add(JMTFImage img) {
		for(int x = 0; x < img.getWidth(); ++x){
			for(int y = 0; y < img.getHeight(); ++y){
				int col = img.getPixel(x, y);
				if(img.getROI().isInside(x, y)){
					if(normalized){
						positive.add(getIndex(JMTFImage.getRed(col), JMTFImage.getGreen(col), JMTFImage.getBlue(col)));
					}else{
						addPositive(JMTFImage.getRed(col), JMTFImage.getGreen(col), JMTFImage.getBlue(col));
					}
				}else{
					if(normalized){
						negative.add(getIndex(JMTFImage.getRed(col), JMTFImage.getGreen(col), JMTFImage.getBlue(col)));
					}else{
						addNegative(JMTFImage.getRed(col), JMTFImage.getGreen(col), JMTFImage.getBlue(col));
					}
				}
			}
		}
		addTemp();
	}
	/**
	 * same as add(img) but adds positive with more weight than negative
	 * @param img
	 * @param bias
	 */
	public void add(JMTFImage img, int bias){
		add(img);
		for(int i = 0; i < bias; ++i){
			addPositive(img);
		}
	}
	
	public void addNegative(int red, int green, int blue){
		int index = getIndex(red, green, blue);
		addNegativeIndex(index);
	}

	protected void addNegativeIndex(int index) {
		if(!probabilities.containsKey(index)){
			probabilities.put(index, new ProbabilityContainer());
		}
		probabilities.get(index).addNegative();
	}
	
	protected void addTemp(){
		if(!normalized){
			return;
		}
		for(int index : positive){
			addPositiveIndex(index);
		}
		positive.clear();
		for(int index : negative){
			addNegativeIndex(index);
		}
		negative.clear();
	}
	
	public float getProbability(int color){
		return getProbability(JMTFImage.getRed(color), JMTFImage.getGreen(color), JMTFImage.getBlue(color));
	}
	
	public float getProbability(int red, int green, int blue){
		int index = getIndex(red, green, blue);
		
		if(index == 0){
			return 0f;
		}
		
		if(probabilities.containsKey(index)){
			return probabilities.get(index).getProbability();
		}
		
		/* 			INTERPOLATION
		 *
		 *  	__ 6        __ 6
		 *	 	\     P    \    d     -- d
		 *	 	/__    n   /__   x	      n
		 * 	   n = 1	  x = 1
		 *  -----------------------------------
		 *  		 	__ 6
		 * 			 	\    d
		 * 			5 *	/__   x
		 * 			   x = 1
		 */	  
		
		
		int rLeft = red - 1;
		for(; rLeft >= 0; --rLeft){
			if(probabilities.containsKey(getIndex(rLeft, green, blue))){
				break;
			}
		}
		if(rLeft == -1 || !probabilities.containsKey(getIndex(rLeft, green, blue))){
			rLeft = -1;
		}
		
		int rRight = red + 1;
		for(; rRight < 256; ++rRight){
			if(probabilities.containsKey(getIndex(rRight, green, blue))){
				break;
			}
		}
		if(rRight == 256 || !probabilities.containsKey(getIndex(rRight, green, blue))){
			rRight = -1;
		}
		
		int gLeft = green - 1;
		for(; gLeft >= 0; --gLeft){
			if(probabilities.containsKey(getIndex(red, gLeft, blue))){
				break;
			}
		}
		if(gLeft == -1 || !probabilities.containsKey(getIndex(red, gLeft, blue))){
			gLeft = -1;
		}
		
		int gRight = green + 1;
		for(; gRight < 256; ++gRight){
			if(probabilities.containsKey(getIndex(red, gRight, blue))){
				break;
			}
		}
		if(gRight == 256 || !probabilities.containsKey(getIndex(red, gRight, blue))){
			gRight = -1;
		}
		
		int bLeft = blue - 1;
		for(; bLeft >= 0; --bLeft){
			if(probabilities.containsKey(getIndex(red, green, bLeft))){
				break;
			}
		}
		if(bLeft == -1 || !probabilities.containsKey(getIndex(red, green, bLeft))){
			bLeft = -1;
		}
		
		int bRight = blue + 1;
		for(; bRight < 256; ++bRight){
			if(probabilities.containsKey(getIndex(red, green, bRight))){
				break;
			}
		}
		if(bRight == 256 || !probabilities.containsKey(getIndex(red, green, bRight))){
			bRight = -1;
		}
		
		int dRLeft = rLeft == -1 ? -1 : red - rLeft;
		int dGLeft = gLeft == -1 ? -1 : green - gLeft;
		int dBLeft = bLeft == -1 ? -1 : blue - bLeft;
		
		int dRRight = rRight == -1 ? -1 : rRight - red;
		int dGRight = gRight == -1 ? -1 : gRight - green;
		int dBRight = bRight == -1 ? -1 : bRight - blue;
		/*
		System.out.println("------");
		System.out.println(red + ", " + green + ", " + blue);
		System.out.println(rLeft + " " + gLeft + " " + bLeft);
		System.out.println(rRight + " " + gRight + " " + bRight);*/
		
		int count = 0;
		int dSum = 0;
		
		if(dRLeft != -1){
			count++;
			dSum += dRLeft;
		}
		
		if(dRRight != -1){
			count++;
			dSum += dRRight;
		}
		
		if(dGLeft != -1){
			count++;
			dSum += dGLeft;
		}
		
		if(dGRight != -1){
			count++;
			dSum += dGRight;
		}
		
		if(dBLeft != -1){
			count++;
			dSum += dBLeft;
		}
		
		if(dBRight != -1){
			count++;
			dSum += dBRight;
		}
		
		if(count < 2){//insufficient data for interpolation
			//System.err.println("0.5");
			return 0.5f;
		}
		
		float _return = 0f;
		
		if(rLeft != -1){
			_return += (probabilities.get(getIndex(rLeft, green, blue)).getProbability() * (dSum - dRLeft));
		}
		
		if(rRight != -1){
			_return += (probabilities.get(getIndex(rRight, green, blue)).getProbability() * (dSum - dRRight));
		}
		
		if(gLeft != -1){
			_return += (probabilities.get(getIndex(red, gLeft, blue)).getProbability() * (dSum - dGLeft));
		}
		
		if(gRight != -1){
			_return += (probabilities.get(getIndex(red, gRight, blue)).getProbability() * (dSum - dGRight));
		}
		
		if(bLeft != -1){
			_return += (probabilities.get(getIndex(red, green, bLeft)).getProbability() * (dSum - dBLeft));
		}
		
		if(bRight != -1){
			_return += (probabilities.get(getIndex(red, green, bRight)).getProbability() * (dSum - dBRight));
		}
		//System.err.println(_return / ((count - 1) * dSum) + " | " + dSum + " | " + count);
		return _return / ((count - 1) * dSum);
		
	}
	
	protected int getIndex(int red, int green, int blue){
		if(red < 0 || green < 0 || blue < 0 || red > 255 || green > 255 || blue > 255){
			throw new ArrayIndexOutOfBoundsException("(" + red + ", " + green + ", " + blue + ")");
		}
		
		return red + (green * 256) + (blue * 256 * 256);
	}
	/**
	 * stores the content of the probability cube to a file
	 * @param file the file to store to
	 * @throws FileNotFoundException
	 */
	public void toFile(File file) throws FileNotFoundException{
		PrintStream ps = new PrintStream(file);
		Iterator<Integer> iter = probabilities.keySet().iterator();
		while(iter.hasNext()){
			int val = iter.next();
			ProbabilityContainer pc = probabilities.get(val);
			for(int i = 0; i < pc.positive; ++i){
				ps.print(val);
				ps.print(' ');
			}
		}
		ps.write('\n');
		iter = probabilities.keySet().iterator();
		while(iter.hasNext()){
			int val = iter.next();
			ProbabilityContainer pc = probabilities.get(val);
			for(int i = 0; i < pc.negative; ++i){
				ps.print(val);
				ps.print(' ');
			}
		}
		ps.close();
	}
	
	/**
	 * reads the content from a previously stored file
	 * @param file
	 * @throws IOException
	 */
	public void fromFile(File file) throws IOException{
		BufferedReader buf = new BufferedReader(new FileReader(file));
		String line = buf.readLine();
		String[] vals = line.split(" ");
		for(String i : vals){
			int j = Integer.parseInt(i);
			addPositiveIndex(j);
		}
		line = buf.readLine();
		vals = line.split(" ");
		for(String i : vals){
			int j = Integer.parseInt(i);
			addNegativeIndex(j);
		}
	buf.close();
	}
}

class ProbabilityContainer{
	
	float positive = 0f, negative = 0f;
	
	void addPositive(){
		this.positive += 1f;
	}
	
	void addNegative(){
		this.negative += 1f;
	}
	
	float getProbability(){
		if(positive < 1){
			if(negative < 1){
				return 0.5f;
			}else{
				return 0f;
			}
		}
		
		return positive / (positive + negative);
		
	}
}
