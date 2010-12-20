package edu.scy.tools.math.test.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.miginfocom.swing.MigLayout;
import eu.scy.tools.math.ui.UIUtils;
import eu.scy.tools.math.ui.panels.Calculator;

public class NotationHtml {

	public static void main(String[] args) {
		JFrame frame = new JFrame("mathTool"); //$NON-NLS-1$
		frame.setLayout(new MigLayout("fill,insets 0 0 0 0")); //$NON-NLS-1$
		// JScrollPane scrollPane = new JScrollPane(doInit());
		//				
		// scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		// scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		JButton testButton = new JButton("test");
		testButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				JOptionPane.showMessageDialog(null, UIUtils.notationHtml);
				JOptionPane.showMessageDialog(null, UIUtils.invalidExpressionErrorMessage, "There was a problem", JOptionPane.ERROR_MESSAGE, null);

			}
		});
		frame.add(testButton);
		
		// when you close the frame, the app exits
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// center the frame and show it
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);
	}
}
