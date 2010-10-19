package eu.scy.tools.math.util;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXButton;

import eu.scy.tools.math.ui.MathTool;

public class UIUtils {

	public static void launchInFrame(final MathTool mathTool) {


				JFrame frame = new JFrame("mathTool"); //$NON-NLS-1$
				frame.setLayout(new MigLayout("insets 0 0 0 0")); //$NON-NLS-1$
				// JScrollPane scrollPane = new JScrollPane(doInit());
				//				
				// scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				// scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				frame.add(mathTool.createMathTool(),"grow");
				frame.setPreferredSize(new Dimension(500, 600));
				// when you close the frame, the app exits
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				// center the frame and show it
				frame.setLocationRelativeTo(null);
				frame.pack();
				frame.setVisible(true);


	}
}
