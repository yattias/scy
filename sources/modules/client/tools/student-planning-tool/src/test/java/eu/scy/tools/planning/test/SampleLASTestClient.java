package eu.scy.tools.planning.test;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

import eu.scy.tools.planning.StudentPlanningTool;
import eu.scy.tools.planning.controller.StudentPlanningController;


public class SampleLASTestClient {

	/** simple main driver for this class */
	public static void main(String[] args) {
		
//		 StudentPlanningController spc = new StudentPlanningController(null);
//	      StudentPlanningTool studentPlanningToolMain = new StudentPlanningTool(spc);
//	      studentPlanningToolMain.launchInFrame()
	  SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
	    	
	    
	    	JFrame frame = new JFrame("Planning Tool");
			frame.setLayout(new MigLayout("insets 0 0 0 0"));
//			JScrollPane scrollPane = new JScrollPane(doInit());
//			
//			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			
			 StudentPlanningController spc = new StudentPlanningController(null);
		      StudentPlanningTool studentPlanningToolMain = new StudentPlanningTool(spc);
		      
		      JComponent createStudentPlanningPanel = studentPlanningToolMain.createStudentPlanningPanel();
			frame.add(createStudentPlanningPanel);
			//frame.add(studentPlanningToolMain.createDragPanel());
			frame.setPreferredSize(createStudentPlanningPanel.getPreferredSize());
			// when you close the frame, the app exits
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			// center the frame and show it
			frame.setLocationRelativeTo(null);
			frame.pack();
			frame.setVisible(true);
			

	    	
	    	
	        
	    
	    }
	  });
	}
	
}
