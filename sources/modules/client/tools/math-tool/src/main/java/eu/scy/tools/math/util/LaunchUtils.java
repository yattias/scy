package eu.scy.tools.math.util;

import java.awt.Dimension;

import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;
import eu.scy.tools.math.ui.MathTool;

public class LaunchUtils {

	public static void launchInFrame(final MathTool mathTool) {


				JFrame frame = new JFrame("mathTool v1.7"); //$NON-NLS-1$
				frame.setLayout(new MigLayout("fill,insets 0 0 0 0")); //$NON-NLS-1$
				// JScrollPane scrollPane = new JScrollPane(doInit());
				//				
				// scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				// scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				
				int width = 1280;
				int height = 800;
				frame.setJMenuBar(mathTool.createMenuBar());
				frame.setPreferredSize(new Dimension(width,height));
				frame.add(mathTool.createMathTool(width,height),"grow");
				
				// when you close the frame, the app exits
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				// center the frame and show it
				frame.setLocationRelativeTo(null);
				frame.pack();
				frame.setVisible(true);


	}
}
