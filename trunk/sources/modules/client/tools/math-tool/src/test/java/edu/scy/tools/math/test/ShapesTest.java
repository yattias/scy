package edu.scy.tools.math.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;
import eu.scy.tools.math.shapes.MathTriangle;
import eu.scy.tools.math.ui.panels.ShapeCanvas;

public class ShapesTest {
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("mathTool"); //$NON-NLS-1$
		frame.setLayout(new MigLayout("fill")); //$NON-NLS-1$
		// JScrollPane scrollPane = new JScrollPane(doInit());
		//				
		// scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		// scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		int width = 1280;
		int height = 800;
		frame.setPreferredSize(new Dimension(width,height));
		
		MathTriangle t = new MathTriangle(200, 100,103);
		//MathToolRectangle mtr = new MathToolRectangle(20, 40, 100, 100);
		//MathEllipse me = new MathEllipse(200, 200, 200, 200);
//		ShapeMoverAdapter sm = new ShapeMoverAdapter(mtr);
//		RectangleSizerAdapter ra = new RectangleSizerAdapter(mtr);
		ShapeCanvas r = new ShapeCanvas(true);
		r.addShape(t);
//		new ShapeMoverAdapter(r);
//		new AdjustSizeAdapter(r);
//		r.addShape(me);
//		r.addShape(mtr);
		r.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				System.out.println(e.getPoint());
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		r.setBackground(Color.white);
//		r.add(mtr);
		
		frame.add(r,"grow");
		// when you close the frame, the app exits
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// center the frame and show it
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);

		
		
	}
}
