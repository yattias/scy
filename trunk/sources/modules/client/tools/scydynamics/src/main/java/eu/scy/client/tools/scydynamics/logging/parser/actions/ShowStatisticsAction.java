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
import eu.scy.client.tools.scydynamics.logging.parser.ParserModel;
import eu.scy.client.tools.scydynamics.logging.parser.ParserView;
import eu.scy.client.tools.scydynamics.logging.parser.UserModel;
import eu.scy.client.tools.scydynamics.logging.parser.UserTimeline;

@SuppressWarnings("serial")
public class ShowStatisticsAction extends AbstractAction {

	private ParserView view;
	private ParserModel model;
	private Domain domain;

	public ShowStatisticsAction(ParserView view, ParserModel model, Domain domain) {
		putValue(Action.NAME, "show statistics");
		this.view = view;
		this.model = model;
		this.domain = domain;
		System.out.println("domain: "+domain+" concepts: "+domain.getConceptSet().getConcepts().size());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		view.addInfo("");
		view.addInfo("users found: "+model.getUserModels().size());
		view.addInfo("");
		view.addInfo(UserModel.STATISTICS_TEMPLATE);
		
		long startTime = System.currentTimeMillis();
		
		for (UserModel user: model.getUserModels().values()) {
			user.calculateStatistics();
		}
		
		long stopTime = System.currentTimeMillis();
		
		for (UserModel user: model.getUserModels().values()) {
			view.addInfo(user.toString());
		}
				
		JFrame frame = new JFrame("action timeline");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(new UserTimeline(model), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        RefineryUtilities.centerFrameOnScreen(frame);
        
        System.out.println("start: "+startTime);
        System.out.println("stop: "+stopTime);
	}

}
