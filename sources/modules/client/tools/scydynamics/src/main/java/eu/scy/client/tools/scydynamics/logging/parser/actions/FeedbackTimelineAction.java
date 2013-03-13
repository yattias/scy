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
import eu.scy.client.tools.scydynamics.logging.parser.FeedbackTimeline;
import eu.scy.client.tools.scydynamics.logging.parser.ParserModel;
import eu.scy.client.tools.scydynamics.logging.parser.ParserView;
import eu.scy.client.tools.scydynamics.logging.parser.UserModel;
import eu.scy.client.tools.scydynamics.logging.parser.UserTimeline;

@SuppressWarnings("serial")
public class FeedbackTimelineAction extends AbstractAction {

	private ParserView view;
	private ParserModel model;
	private Domain domain;
	private ArrayList<JFrame> frames;

	public FeedbackTimelineAction(ParserView view, ParserModel model, Domain domain) {
		putValue(Action.NAME, "show feedback");
		this.view = view;
		this.model = model;
		this.domain = domain;
		frames = new ArrayList<JFrame>();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for (UserModel user: model.getUserModels().values()) {
			JFrame frame = new JFrame("feedback timeline for user "+user.getUserName());
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.getContentPane().setLayout(new BorderLayout());
			frame.getContentPane().add(new FeedbackTimeline(user), BorderLayout.CENTER);
			frame.setName(user.getUserName());
	        frame.pack();
	        frame.setVisible(true);
	        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	        RefineryUtilities.centerFrameOnScreen(frame);
	        frames.add(frame);
		}
		//storeScreenshots();
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
