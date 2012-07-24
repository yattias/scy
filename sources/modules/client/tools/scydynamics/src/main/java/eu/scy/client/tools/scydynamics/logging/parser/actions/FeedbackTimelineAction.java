package eu.scy.client.tools.scydynamics.logging.parser.actions;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;

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

	public FeedbackTimelineAction(ParserView view, ParserModel model, Domain domain) {
		putValue(Action.NAME, "show feedback");
		this.view = view;
		this.model = model;
		this.domain = domain;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for (UserModel user: model.getUserModels().values()) {
			if (user.getAction(ModellingLoggerFES.FEEDBACK_REQUESTED).size()>2) {
				JFrame frame = new JFrame("feedback timeline for user "+user.getUserName());
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.getContentPane().setLayout(new BorderLayout());
				frame.getContentPane().add(new FeedbackTimeline(user), BorderLayout.CENTER);
		        frame.pack();
		        frame.setVisible(true);
		        RefineryUtilities.centerFrameOnScreen(frame);
			}
		}
	}

}
