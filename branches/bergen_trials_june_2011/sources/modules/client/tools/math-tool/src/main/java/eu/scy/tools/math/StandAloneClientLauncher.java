package eu.scy.tools.math;

import javax.swing.SwingUtilities;

import eu.scy.tools.math.controller.MathToolController;
import eu.scy.tools.math.ui.MathTool;
import eu.scy.tools.math.util.LaunchUtils;

public class StandAloneClientLauncher {

	public static void main(String[] args) {
		
	     SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	MathToolController mc = new MathToolController();
	        		MathTool mathTool = new MathTool(mc);
	        		LaunchUtils.launchInFrame(mathTool);
	            }
	        });
	     
	
		
	}
}
