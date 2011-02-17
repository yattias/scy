package edu.scy.tools.math.test;

import javax.swing.SwingUtilities;

import eu.scy.tools.math.controller.MathToolController;
import eu.scy.tools.math.controller.SCYMathToolController;
import eu.scy.tools.math.ui.MathTool;
import eu.scy.tools.math.util.LaunchUtils;

public class SCYStandAloneClientLauncher {

	public static void main(String[] args) {
		
	     SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	SCYMathToolController mc = new SCYMathToolController();
	        		MathTool mathTool = new MathTool(mc);
	        		LaunchUtils.launchInFrame(mathTool);
	            }
	        });
	     
	
		
	}
}
