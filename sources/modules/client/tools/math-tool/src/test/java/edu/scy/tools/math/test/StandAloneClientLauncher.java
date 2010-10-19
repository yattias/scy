package edu.scy.tools.math.test;

import javax.swing.SwingUtilities;

import eu.scy.tools.math.controller.MathToolController;
import eu.scy.tools.math.ui.MathTool;
import eu.scy.tools.math.util.UIUtils;

public class StandAloneClientLauncher {

	public static void main(String[] args) {
		
	     SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	MathToolController mc = new MathToolController();
	        		MathTool mathTool = new MathTool(mc);
	        		mathTool.hasMenu(true);
	        		mathTool.hasToolbar(true);
	        		UIUtils.launchInFrame(mathTool);
	            }
	        });
	     
	
		
	}
}
