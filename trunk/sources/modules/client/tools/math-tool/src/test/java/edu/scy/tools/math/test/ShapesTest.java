package edu.scy.tools.math.test;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;
import eu.scy.tools.math.adapters.AdjustSizeAdapter;
import eu.scy.tools.math.adapters.ShapeMoverAdapter;
import eu.scy.tools.math.shapes.MathEllipse;
import eu.scy.tools.math.shapes.MathToolRectangle;
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
		
		MathToolRectangle mtr = new MathToolRectangle(20, 40, 100, 100);
		MathEllipse me = new MathEllipse(200, 200, 200, 200);
//		ShapeMoverAdapter sm = new ShapeMoverAdapter(mtr);
//		RectangleSizerAdapter ra = new RectangleSizerAdapter(mtr);
		ShapeCanvas r = new ShapeCanvas(true);
		new ShapeMoverAdapter(r);
		new AdjustSizeAdapter(r);
		r.addShape(me);
		r.addShape(mtr);
//		r.addMouseListener(new MouseListener() {
//			
//			@Override
//			public void mouseReleased(MouseEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void mousePressed(MouseEvent e) {
//				
//				System.out
//						.println("ShapesTest.main(...).new MouseListener() {...}.mousePressed()");
//				JComponent source = (JComponent) e.getSource();
//				source.requestFocus();
//			}
//			
//			@Override
//			public void mouseExited(MouseEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void mouseEntered(MouseEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				System.out
//						.println("ShapesTest.main(...).new MouseListener() {...}.mouseClicked()");
//				JComponent source = (JComponent) e.getSource();
//				source.requestFocus();
//				
//			}
//		});
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
