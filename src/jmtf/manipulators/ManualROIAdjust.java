/**
 * Java Motion Tracking Framework
 */
package jmtf.manipulators;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import jmtf.AbstractImageManipulator;
import jmtf.ImageSource;
import jmtf.JMTFImage;

/**
 * @author Luca Rossetto
 *
 */
public class ManualROIAdjust extends AbstractImageManipulator {

	private float zoom;
	private RoiSelectorFrame frame;
	String title;
	
	public ManualROIAdjust(ImageSource source, float zoom, String title) {
		super(source);
		this.zoom = zoom;
		frame = new RoiSelectorFrame();
		this.title = title;
	}
	public ManualROIAdjust(ImageSource source, float zoom) {
		this(source, zoom, "");
	}

	/* (non-Javadoc)
	 * @see jmtf.ImageManipulator#manipulate(jmtf.JMTFImage)
	 */
	@Override
	public void manipulate(JMTFImage input) {
		frame.update(input);
		frame.selectionDone = false;
		while(!frame.selectionDone && !frame.autoAccept.isSelected()){//XXX replace with waiting
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
	}
	
	class RoiSelectorFrame extends JFrame implements MouseListener, MouseMotionListener{


		private static final long serialVersionUID = 8069265682971256303L;
		private int x1, x2, y1, y2;
		private JPanel canvas;
		private JMTFImage image;
		private BufferedImage img;
		private boolean selectionDone = false;
		private JCheckBox autoAccept;
		
		RoiSelectorFrame() {
			super("ROI Adjust");
			setResizable(false);
			setLayout(new BorderLayout(2, 2));
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			canvas = new JPanel(){

				private static final long serialVersionUID = 7514772366612904153L;

				@Override
				public void paintComponent(Graphics g) {
					if (img != null) {
						g.drawImage(img, 0, 0,
								(int) (img.getWidth() * zoom),
								(int) (img.getHeight() * zoom), null);
						g.setColor(Color.RED);
						g.drawRect((int)(image.getROI().minX * zoom), (int)(image.getROI().minY * zoom), (int)(image.getROI().getWidth() * zoom), (int)(image.getROI().getHeight() * zoom));
						g.setColor(Color.ORANGE);
						g.drawRect((int)(Math.min(x1,x2) * zoom), (int)(Math.min(y1, y2) * zoom), (int)(Math.abs(x1 - x2) * zoom), (int)(Math.abs(y1 - y2) * zoom));
						setPreferredSize(new Dimension((int)((img.getWidth() + 1) * zoom),(int) ((img.getHeight() + 1) * zoom)));
					}
				}
			};
			canvas.addMouseListener(this);
			canvas.addMouseMotionListener(this);
			
			getContentPane().add(canvas);
			
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			JButton button1 = new JButton("Full image");
			JButton button2 = new JButton("Reset mask");
			JButton button3 = new JButton("Accept");
			autoAccept = new JCheckBox("accept automatically");
			buttonPanel.add(button1);
			buttonPanel.add(button2);
			buttonPanel.add(button3);
			buttonPanel.add(autoAccept);
			
			button1.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					x1 = 0;
					x2 = img.getWidth();
					y1 = 0;
					y2 = img.getHeight();
					canvas.repaint();
				}
			});
			
			button2.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					x1 = image.getROI().minX;
					x2 = image.getROI().maxX;
					y1 = image.getROI().minY;
					y2 = image.getROI().maxY;
					canvas.repaint();				
				}
			});
			
			button3.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {

					image.getROI().minX = Math.min(x1, x2);
					image.getROI().maxX = Math.max(x1, x2);
					image.getROI().minY = Math.min(y1, y2);
					image.getROI().maxY = Math.max(y1, y2);
					
					setVisible(false);
					selectionDone = true;
					
				}
			});
			
			
			getContentPane().add(buttonPanel, BorderLayout.PAGE_END);
		}
		
		void update(JMTFImage image){
			setTitle("ROI Adjust " + title);
			this.image = image;
			this.img = this.image.toBufferedImage(null);
			canvas.setPreferredSize(new Dimension((int)((img.getWidth() * zoom + 1) ),(int) ((img.getHeight() * zoom + 1) )));
			x1 = image.getROI().minX;
			x2 = image.getROI().maxX;
			y1 = image.getROI().minY;
			y2 = image.getROI().maxY;
			pack();
			setVisible(true);
			canvas.repaint();
		}
		
		
		@Override
		public void mouseDragged(MouseEvent e) {
			x2 = (int) (e.getX() / zoom);			
			y2 = (int) (e.getY() / zoom);
			canvas.repaint();
		}

		
		@Override
		public void mouseMoved(MouseEvent e) {}

		
		@Override
		public void mouseClicked(MouseEvent e) {}

		
		@Override
		public void mousePressed(MouseEvent e) {
			x1 = (int) (e.getX() / zoom);
			y1 = (int) (e.getY() / zoom);
			canvas.repaint();
		}

		
		@Override
		public void mouseReleased(MouseEvent e) {}

		
		@Override
		public void mouseEntered(MouseEvent e) {}

		
		@Override
		public void mouseExited(MouseEvent e) {}

		
	}

}
