package eu.scy.tools.planning.test;


import java.util.concurrent.ExecutionException;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

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
	
	//@Ignore
	@Test
	public void fetchBuddyIcon(){
		final StudentPlanningController sc = new StudentPlanningController();
		
		
		
		
		JFrame frame = new JFrame("Planning Tool");
		frame.setLayout(new MigLayout("insets 0 0 0 0"));
//		JScrollPane scrollPane = new JScrollPane(doInit());
//		
//		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		SwingWorker worker = new SwingWorker<Icon, Void>() {
		    @Override
		    public Icon doInBackground() {
		    	Icon buddyImageIcon = sc.getBuddyImageIcon("lars");
		        
		        return buddyImageIcon;
		    }

		    @Override
		    public void done() {
		        
		    }
		};
		
		final JXButton b = new JXButton();
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    Icon buddyImageIcon = sc.getBuddyImageIcon("lars");
		      // Here, we can safely update the GUI
		      // because we'll be called from the
		      // event dispatch thread
		      b.setIcon(buddyImageIcon);
		    }
		  });
//		Icon icon = null;
//		try {
//			icon = (Icon) worker.get();
//		} catch (InterruptedException e1) {
//			e1.printStackTrace();
//		} catch (ExecutionException e1) {
//			e1.printStackTrace();
//		}
		
		
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
