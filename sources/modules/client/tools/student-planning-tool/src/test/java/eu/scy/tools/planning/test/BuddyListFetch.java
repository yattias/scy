package eu.scy.tools.planning.test;


import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXButton;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import eu.scy.tools.planning.StudentPlanningTool;
import eu.scy.tools.planning.controller.StudentPlanningController;

public class BuddyListFetch {

	@Ignore
	public void setUp() throws Exception {
	}
	
	@Ignore
	public void fetchBuddyIcon(){
		StudentPlanningController sc = new StudentPlanningController();
		
		
		
		
		JFrame frame = new JFrame("Planning Tool");
		frame.setLayout(new MigLayout("insets 0 0 0 0"));
//		JScrollPane scrollPane = new JScrollPane(doInit());
//		
//		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		Icon buddyImageIcon = sc.getBuddyImageIcon("lars");
		
		JXButton b = new JXButton(buddyImageIcon);
		frame.add(b);
		//frame.add(studentPlanningToolMain.createDragPanel());
		//frame.setPreferredSize(createStudentPlanningPanel.getPreferredSize());
		// when you close the frame, the app exits
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// center the frame and show it
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);
		//for(;;);
	}

}
