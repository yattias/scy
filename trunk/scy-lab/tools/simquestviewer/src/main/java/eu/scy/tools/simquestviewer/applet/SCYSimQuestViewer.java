package eu.scy.tools.simquestviewer.applet;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import sqv.SimQuestViewer;
import utils.FileName;

/**
 * This class provides a stand-alone (non-applet) version of the
 * {@link eu.scy.tools.simquestviewer.SCYSimQuestViewerApplet}. It is similar to
 * {@sqv.SimQuestViewerApplication}, but injects a
 * {@link eu.scy.tools.simquestviewer.DataCollector} to the user interface.
 * 
 * @author Lars Bollen
 * 
 */
public class SCYSimQuestViewer {

	SimQuestViewer sqv;
	
	public SCYSimQuestViewer() {
		// if no URI to a simulation file is given, try that one:
		this("tools/simquestviewer/src/main/resources/balance.sqzx");
	}

	public SCYSimQuestViewer(String uri) {
		sqv = new SimQuestViewer();

		FileName fileName = new FileName(uri);
		// Set the file (URI)
		System.out.println("SCYSimQuestViewer(). trying to load: "
				+ fileName.toURI().getPath().toString());
		sqv.setFile(fileName.toURI());
		
		try 
        {
        	SwingUtilities.invokeAndWait(sqv);
        } 
        catch (Exception e) 
        {
            System.err.println("createGUI didn't successfully complete");
            System.err.println(e.getCause());
        }
		
		// injecting the SCY datacollector
		DataCollector dataCollector = new DataCollector(sqv);
		sqv.getMainFrame().getContentPane().add(dataCollector, BorderLayout.SOUTH);
		// I need some repaint here; the DataCollector is only visible when somebody resizes the frame
	}
	
	public JFrame getUIasJFrame() {
		return sqv.getMainFrame();
	}
	
	public JPanel getUIasJPanel() {
		JPanel jp = new JPanel(new BorderLayout());
		Component[] components = sqv.getMainFrame().getContentPane().getComponents();
		for (int i=0; i<components.length; i++) {
			// the following order is important, since the DataCollector also extends JPanel
			if (components[i] instanceof DataCollector) {
				jp.add(components[i], BorderLayout.SOUTH);
			} else if (components[i] instanceof JPanel) {
				jp.add(components[i], BorderLayout.CENTER);
			}
		}
		return jp;
	}
	
	public static void main(String[] args) {
		// for testing purposes
		new SCYSimQuestViewer();
	}
	
}