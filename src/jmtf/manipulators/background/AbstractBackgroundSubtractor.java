/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators.background;

import java.util.ArrayList;
import java.util.Iterator;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.ImageUpdateListener;
import jmtf.JMTFImage;

/**
 * @author Luca Rossetto
 *
 */
public abstract class AbstractBackgroundSubtractor extends AbstractImageManipulator
		implements BackgroundSubtractor {

	protected int width, height;
	protected JMTFImage background;
	private ArrayList<ImageUpdateListener> bglisteners = new ArrayList<ImageUpdateListener>();

	/**
	 * @param source
	 * @param width
	 * @param height
	 */
	protected AbstractBackgroundSubtractor(ImageSource source, int width, int height) {
		super(source);
		this.width = width;
		this.height = height;
		this.background = new JMTFImage(width, height);
	}

	public void addBackgroundUpdateListener(ImageUpdateListener listener){
		this.bglisteners.add(listener);
	}
	
	public void removeBackgroundUpdateListener(ImageUpdateListener listener){
		this.bglisteners.remove(listener);
	}
	
	protected void updateBackgroundListeners(){
		Iterator<ImageUpdateListener> iter = this.bglisteners.iterator();
		while(iter.hasNext()){
			iter.next().update(this.background);
		}
	}

	public JMTFImage getCurrentBackground(){
		return this.background;
	}

}
