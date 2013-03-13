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

import colab.um.xml.model.JxmModel;

import eu.scy.actionlogging.api.IAction;
import eu.scy.client.tools.scydynamics.domain.Domain;
import eu.scy.client.tools.scydynamics.domain.Feedback;
import eu.scy.client.tools.scydynamics.logging.ModellingLogger;
import eu.scy.client.tools.scydynamics.logging.ModellingLoggerFES;
import eu.scy.client.tools.scydynamics.logging.parser.FeedbackTimeline;
import eu.scy.client.tools.scydynamics.logging.parser.ParserModel;
import eu.scy.client.tools.scydynamics.logging.parser.ParserView;
import eu.scy.client.tools.scydynamics.logging.parser.UserModel;
import eu.scy.client.tools.scydynamics.logging.parser.UserTimeline;
import eu.scy.client.tools.scydynamics.model.Model;

@SuppressWarnings("serial")
public class ModelScoreAction extends AbstractAction {

	private ParserView view;
	private ParserModel model;
	private Domain domain;

	public ModelScoreAction(ParserView view, ParserModel model, Domain domain) {
		putValue(Action.NAME, "show model score");
		this.view = view;
		this.model = model;
		this.domain = domain;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		view.addInfo("user, id, timestamp, model_score, correct_names, correct_relations, correct_directions, correct_linktypes, correct_nodetype, incorrect_names, incorrect_relations, incorrect_directions, incorrect_linktypes, incorrect_nodetypes");
		for (UserModel user: model.getUserModels().values()) {
			for (IAction action: user.getActions()) {
				try {
					if (action.getType().endsWith("_added") ||
							action.getType().endsWith("_deleted") ||
							action.getType().equals(ModellingLogger.SPECIFICATION_CHANGED) ||
							action.getType().equals(ModellingLogger.ELEMENT_RENAMED) ||
							action.getType().equals(ModellingLogger.MODEL_LOADED) ||
							action.getType().equals(ModellingLogger.MODEL_CLEARED)) {
						String modelString = action.getAttribute("model");
						if ( modelString != null) {
							Model model = new Model(null);
							model.setXmModel(JxmModel.readStringXML(modelString));
							Feedback feedback = new Feedback(model, user.getDomain());
							view.addInfo(action.getUser()+", "+action.getId()+", "+action.getTimeInMillis()+", "+feedback.getCorrectnessRatio()+", "+feedback.getCorrectNames()+", "+feedback.getCorrectRelations()+", "+feedback.getCorrectDirections()+", "+feedback.getCorrectLinkTypes()+
									", "+feedback.getCorrectNodeType()+", "+feedback.getIncorrectNames()+", "+feedback.getIncorrectRelations()+", "+feedback.getIncorrectDirections()+", "+feedback.getIncorrectLinkTypes()+", "+feedback.getIncorrectNodeType());
						}
					}
				} catch (Exception ex) {
					System.out.println("something went wrong: "+ex.getMessage());
				}
			}
		}
	}
	
}
