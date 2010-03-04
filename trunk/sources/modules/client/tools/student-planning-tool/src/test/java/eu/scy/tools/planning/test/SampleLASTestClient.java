package eu.scy.tools.planning.test;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.tools.planning.StudentPlanningTool;
import eu.scy.tools.planning.controller.StudentPlanningController;


public class SampleLASTestClient {

	/** simple main driver for this class */
	public static void main(String[] args) {
		
		//ToolBrokerAPI tbi = new ToolBrokerImpl("tony","tony");
		 StudentPlanningController spc = new StudentPlanningController("ff808081272484bf01272861d6bc003e","hulk");
	     final StudentPlanningTool studentPlanningToolMain = new StudentPlanningTool(spc);
	     final JComponent createStudentPlanningPanel = studentPlanningToolMain.createStudentPlanningPanel();
	     
//		 StudentPlanningController spc = new StudentPlanningController(null);
//	      StudentPlanningTool studentPlanningToolMain = new StudentPlanningTool(spc);
//	      studentPlanningToolMain.launchInFrame()
	  SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
	    	
	    
	    	JFrame frame = new JFrame("Planning Tool");
			frame.setLayout(new MigLayout("insets 0 0 0 0"));
			frame.addComponentListener(new ComponentListener() {
				
				@Override
				public void componentShown(ComponentEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void componentResized(ComponentEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void componentMoved(ComponentEvent e) {
					// TODO Auto-generated method stub
					System.out.println(e.getComponent().getClass().getName() + " --- Resized ");     
					JFrame jf = (JFrame) e.getSource();
					studentPlanningToolMain.resizeSPT(jf.getWidth(),jf.getHeight());
				}
				
				@Override
				public void componentHidden(ComponentEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
//			JScrollPane scrollPane = new JScrollPane(doInit());
//			
//			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			
			
		      
		      
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
