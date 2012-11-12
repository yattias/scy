package eu.scy.client.tools.scydynamics.logging.parser.actions;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jfree.ui.RefineryUtilities;

import eu.scy.actionlogging.api.IAction;
import eu.scy.client.tools.scydynamics.domain.Domain;
import eu.scy.client.tools.scydynamics.logging.ModellingLogger;
import eu.scy.client.tools.scydynamics.logging.ModellingLoggerFES;
import eu.scy.client.tools.scydynamics.logging.parser.actiongraph.ActionGraph;
import eu.scy.client.tools.scydynamics.logging.parser.actiongraph.ActionSequenceMatrix;
import eu.scy.client.tools.scydynamics.logging.parser.actiongraph.ActionSequenceMatrix.ActionType;
import eu.scy.client.tools.scydynamics.logging.parser.FeedbackTimeline;
import eu.scy.client.tools.scydynamics.logging.parser.ParserModel;
import eu.scy.client.tools.scydynamics.logging.parser.ParserView;
import eu.scy.client.tools.scydynamics.logging.parser.UserModel;
import eu.scy.client.tools.scydynamics.logging.parser.UserTimeline;

@SuppressWarnings("serial")
public class ActionMatrixAction extends AbstractAction {

	private ParserView view;
	private ParserModel model;
	private Domain domain;
	private ArrayList<JFrame> frames;

	public ActionMatrixAction(ParserView view, ParserModel model, Domain domain) {
		putValue(Action.NAME, "show action matrix");
		this.view = view;
		this.model = model;
		this.domain = domain;
		frames = new ArrayList<JFrame>();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ActionSequenceMatrix jointMatrix = new ActionSequenceMatrix();
		for (UserModel user: model.getUserModels().values()) {
			ActionSequenceMatrix actionSequenceMatrix = new ActionSequenceMatrix(user);
			double[][] matrix = actionSequenceMatrix.getActionMatrix();
			jointMatrix.add(matrix);
			showGraph(user.getUserName(), matrix);
		}
		showGraph("all users", jointMatrix.getActionMatrix());
		dumpMatrix("all users", jointMatrix.getActionMatrix());
		storeScreenshots();
	}
	
	private void showGraph(String username, double[][] matrix) {
		JFrame frame = new JFrame("action matrix for "+username);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());		
		dumpMatrix(username, matrix);
		frame.getContentPane().add(new ActionGraph(matrix), BorderLayout.CENTER);
		frame.pack();
		frame.setName(username);
        frame.setVisible(true);
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(800, 800);
        RefineryUtilities.centerFrameOnScreen(frame);
        frames.add(frame);
	}
	
	private void dumpMatrix(String userName, double[][] matrix) {
		view.addInfo("");
		view.addInfo("action sequence for user: "+userName);
		String header = " ";
		for (ActionType type: ActionSequenceMatrix.ActionType.values()) {
			header = header+", "+type.toString();
		}
		view.addInfo(header);
		for (int row=0; row<matrix.length; row++) {
			String rowString = ActionSequenceMatrix.ActionType.values()[row].toString();
			for (int column=0; column<matrix.length; column++) {
				double value = matrix[row][column];
				rowString = rowString+" ,"+Math.round(value);
			}
			view.addInfo(rowString);
		}	
	}

	private void storeScreenshots() {
		for (final JFrame frame: frames) {
			SwingUtilities.invokeLater(new Runnable() {
			    public void run() {	
					BufferedImage screenshot = getScreenShot(frame);
					writeImageToFile(screenshot, "screenshots\\"+frame.getName()+".png");
			    }
			  });
		}
	}
	
	private void writeImageToFile(BufferedImage image, String fileName) {
	    try {
	    	File outputfile = new File(fileName);
	    	ImageIO.write(image, "png", outputfile);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public BufferedImage getScreenShot(Component component) {
	    BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), java.awt.image.BufferedImage.TYPE_INT_RGB);
	    component.paint( image.getGraphics() );
	    return image;
   }

}
