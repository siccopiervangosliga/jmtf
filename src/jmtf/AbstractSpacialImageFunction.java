/**
 * Java Motion Tracking Framework
 */
package jmtf;

/**
 * @author Luca Rossetto
 *
 */
public abstract class AbstractSpacialImageFunction extends
		AbstractImageManipulator {

	protected int size;
	
	protected AbstractSpacialImageFunction(ImageSource source, int size) {
		super(source);
		this.size = size;
	}

	/* (non-Javadoc)
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		if(input == null){
			return;
		}

		JMTFImage output = input.copy();//new JMTFImage(input.getWidth(), input.getHeight());
		
		int[] colors = new int[4*this.size*this.size + 2*this.size + 1]; //(size + 1)^2
		int len, i;
		for (int x = input.getROI().minX; x <= input.getROI().maxX; ++x) {
			for (int y = input.getROI().minY; y <= input.getROI().maxY; ++y) {
				
				len = (Math.min(input.getWidth(), x + size) - Math.max(0, x - this.size)) * (Math.min(input.getHeight(), y + size) - Math.max(0, y - this.size));
				i = 0;
				
				for(int x1 = Math.max(x - this.size, 0); x1 < Math.min(x + this.size, input.getWidth()); ++x1){
					for(int y1 = Math.max(y - this.size, 0); y1 < Math.min(y + this.size, input.getHeight()); ++y1){
						colors[i++] = input.getPixel(x1, y1);
					}
				}
				output.setPixel(x, y, func(colors, len));
			}
		}
		input.setPixels(output.getPixels());
	}
	
	protected abstract int func(int[] colors, int len);

}
