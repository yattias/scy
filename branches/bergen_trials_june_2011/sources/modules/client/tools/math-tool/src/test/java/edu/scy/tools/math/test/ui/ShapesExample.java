package edu.scy.tools.math.test.ui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import net.miginfocom.swing.MigLayout;
import eu.scy.tools.math.adapters.AdjustSizeAdapter;
import eu.scy.tools.math.adapters.ShapeMoverAdapter;
import eu.scy.tools.math.shapes.IMathRectangle;
import eu.scy.tools.math.shapes.IMathShape;
import eu.scy.tools.math.shapes.impl.MathEllipse;
import eu.scy.tools.math.shapes.impl.MathRectangle;
import eu.scy.tools.math.shapes.impl.MathRectangle3D;
import eu.scy.tools.math.shapes.impl.MathTriangle;
import eu.scy.tools.math.ui.panels.ShapeCanvas;

public class ShapesExample {
	public static void main(String[] args) {
		
		final JFrame frame = new JFrame("mathTool"); //$NON-NLS-1$
		frame.setLayout(new BorderLayout(1,1)); //$NON-NLS-1$
		// JScrollPane scrollPane = new JScrollPane(doInit());
		//				
		// scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		// scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		int width = 1280;
		int height = 800;
		frame.setPreferredSize(new Dimension(width,height));
		
		final MathTriangle t = new MathTriangle(200, 100,103);
		t.setHasDecorations(true);
		final MathRectangle mtr = new MathRectangle(250, 100, 100, 100);
		mtr.setHasDecorations(true);
		final MathEllipse me = new MathEllipse(300, 100, 200, 200);
		me.setHasDecorations(true);
		final ShapeCanvas shapeCanvas = new ShapeCanvas(false);
		
		
		new ShapeMoverAdapter(shapeCanvas);
		new AdjustSizeAdapter(shapeCanvas);
		
		shapeCanvas.addShape(t);
		shapeCanvas.addShape(mtr);
		shapeCanvas.addShape(me);
		
//		new ShapeMoverAdapter(r);
//		new AdjustSizeAdapter(r);
//		r.addShape(me);
//		r.addShape(mtr);
//		r.addShape(t);
//		JTextField b = new JTextField("kik");
//		b.setLocation(100, 100);
//		b.setSize(20, 20);
//		
//		r.add(b);
		
//		MathRectangle3D mr3 = new MathRectangle3D(100, 200);
//		shapeCanvas.addShape(mr3);
//		r.setBackground(Color.white);
		
//		new ShapeMoverAdapter(r);
//		r.add(mtr);
		
	      final XStream xs = new XStream(new DomDriver());
      	xs.alias("rectangle", MathRectangle.class);
			xs.alias("triangle", MathTriangle.class);
			xs.alias("ellipse", MathEllipse.class);
			xs.alias("rectange3d", MathRectangle3D.class);
			
		JXButton openButton = new JXButton("Open");
		openButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fc = new JFileChooser();
			
				 
				 FileNameExtensionFilter filter = new FileNameExtensionFilter("XML", new String[] { "XML" });
				 fc.setFileFilter(filter);
				int returnVal = fc.showOpenDialog(frame);
				
				
				 
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	          
					
	                File file = fc.getSelectedFile();
	                String fts = null;
					try {
						fts = FileUtils.readFileToString(file);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					ArrayList newJoe = (ArrayList)xs.fromXML(fts);
					for (Object object : newJoe) {
						shapeCanvas.addShape((IMathShape) object);
					}
					
					shapeCanvas.repaint();
					shapeCanvas.revalidate();
					
	            }
				
				
			}});		
		JXButton saveButton = new JXButton("Save");
		JXButton exportButton = new JXButton("Export");
		JXPanel p = new JXPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT));
		p.add(openButton);
		p.add(saveButton);
		p.add(exportButton);
		exportButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
//				shapeCanvas.getGraphics().
				JFileChooser fc = new JFileChooser();
				
				fc.setSelectedFile(new File("importgooglesketchup.png"));
				 FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG", new String[] { "PNG" });
				 fc.setFileFilter(filter);
				int returnVal = fc.showSaveDialog(frame);
				
				
				 
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	            	
	            	BufferedImage bimage = new BufferedImage(800, 600, BufferedImage.TRANSLUCENT);
	            	
					if (bimage != null) {
						
						File file = fc.getSelectedFile();
					    // The component is not visible on the screen
						
						shapeCanvas.setScreenCaptureMode(true);
						shapeCanvas.repaint();
						shapeCanvas.revalidate();
						
						Graphics2D g2g = (Graphics2D) bimage.getGraphics();
						g2g.setColor(Color.RED);
						
						shapeCanvas.paint(g2g);
							try {
								ImageIO.write( bimage, "PNG" /* format desired */ , file /* target */ );
								shapeCanvas.setScreenCaptureMode(false);
								shapeCanvas.repaint();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
					}
					
	            	
	            	
	            }
				
				
			}});
		saveButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fc = new JFileChooser();
			
				 
				fc.setSelectedFile(new File("mathToolSavedData.xml"));
				 FileNameExtensionFilter filter = new FileNameExtensionFilter("XML", new String[] { "XML" });
				 fc.setFileFilter(filter);
				int returnVal = fc.showSaveDialog(frame);
				
				
				 
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	            	
	                File file = fc.getSelectedFile();
					
					
	                String xml = xs.toXML(shapeCanvas.getMathShapes());
	                try {
						FileUtils.writeStringToFile(file, xml);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					shapeCanvas.getMathShapes().removeAll(shapeCanvas.getMathShapes());
					shapeCanvas.repaint();
					shapeCanvas.revalidate();
	            }
				
				
			}});		
		
		
		frame.add(p,BorderLayout.NORTH);
		frame.add(shapeCanvas,BorderLayout.CENTER);
		// when you close the frame, the app exits
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// center the frame and show it
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);

		
		
	}
}
