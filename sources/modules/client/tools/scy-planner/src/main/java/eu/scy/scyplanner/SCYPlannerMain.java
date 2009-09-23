package eu.scy.scyplanner;

import eu.scy.scymapper.api.diagram.IDiagramModel;
import eu.scy.scyplanner.impl.diagram.SCYPlannerDiagramView;
import eu.scy.scyplanner.components.application.SCYPlannerSplashWindow;
import eu.scy.scyplanner.components.application.SCYPlannerFrame;
import eu.scy.scyplanner.components.demo.SCYPlannerDemo;
import eu.scy.scyplanner.application.ApplicationManager;

import javax.swing.*;
import java.awt.*;

/**
 * User: Bjoerge Naess
 * Date: 28.aug.2009
 * Time: 10:57:50
 */
public class SCYPlannerMain {

	public static void main(String[] args) {
        SCYPlannerSplashWindow splashWindow = new SCYPlannerSplashWindow();
        splashWindow.setVisible(true);

        SCYPlannerFrame frame = new SCYPlannerFrame();
        ApplicationManager.getApplicationManager().setScyPlannerFrame(frame);        
        frame.setVisible(true);

		/*SCYPlannerDemo demo = new SCYPlannerDemo();
        demo.setVisible(true);*/

        splashWindow.setVisible(false);
        splashWindow.dispose();
	}
}
