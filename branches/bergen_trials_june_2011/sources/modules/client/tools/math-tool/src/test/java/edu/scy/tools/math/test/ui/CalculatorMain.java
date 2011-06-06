package edu.scy.tools.math.test.ui;

import java.awt.Dimension;

import javax.swing.JFrame;

import eu.scy.tools.math.ui.UIUtils;
import eu.scy.tools.math.ui.panels.Calculator;

import net.miginfocom.swing.MigLayout;

public class CalculatorMain {

	public static void main(String[] args) {
		JFrame frame = new JFrame("mathTool"); //$NON-NLS-1$
		frame.setLayout(new MigLayout("fill,insets 0 0 0 0")); //$NON-NLS-1$
		// JScrollPane scrollPane = new JScrollPane(doInit());
		//				
		// scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		// scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		Calculator c = new Calculator(UIUtils._2D);
		int width = 370;
		int height = 220;
		frame.setPreferredSize(new Dimension(width,height));
		frame.add(c,"grow");
		
		// when you close the frame, the app exits
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// center the frame and show it
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);
	}
	
}
