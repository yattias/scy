package eu.scy.tools.planning.test;

import javax.swing.SwingUtilities;

import eu.scy.tools.planning.StudentPlanningToolMain;


public class SampleLASTestClient {

	/** simple main driver for this class */
	public static void main(String[] args) {
	  SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
	      new StudentPlanningToolMain();
	    }
	  });
	}
	
}
