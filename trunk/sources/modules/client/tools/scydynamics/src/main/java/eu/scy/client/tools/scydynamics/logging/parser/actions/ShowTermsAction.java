package eu.scy.client.tools.scydynamics.logging.parser.actions;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.AbstractAction;
import javax.swing.Action;

import eu.scy.actionlogging.api.IAction;
import eu.scy.client.tools.scydynamics.domain.Domain;
import eu.scy.client.tools.scydynamics.logging.ModellingLogger;
import eu.scy.client.tools.scydynamics.logging.parser.ParserModel;
import eu.scy.client.tools.scydynamics.logging.parser.ParserView;
import eu.scy.client.tools.scydynamics.logging.parser.UserModel;

@SuppressWarnings("serial")
public class ShowTermsAction extends AbstractAction {

	private ParserView view;
	private ParserModel model;
	private Domain domain;

	public ShowTermsAction(ParserView view, ParserModel model, Domain domain) {
		putValue(Action.NAME, "show terms");
		this.view = view;
		this.model = model;
		this.domain = domain;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		HashMap<String, Integer> usedTerms = new HashMap<String, Integer>();
		for (IAction action: model.getAction(ModellingLogger.SPECIFICATION_CHANGED)) {
			if (usedTerms.get(action.getAttribute("name")) == null) {
				usedTerms.put(action.getAttribute("name"), 1);
			} else {
				usedTerms.put(action.getAttribute("name"), usedTerms.get(action.getAttribute("name"))+1);
			}
		}
		
		view.addInfo("");
		view.addInfo("frequency, term used, related concept");
		String concept = "";
		for (String term: usedTerms.keySet()) {
			if (term.toLowerCase().startsWith("aux_") || term.toLowerCase().startsWith("const_") || term.toLowerCase().startsWith("stock_")) {
				// it's an unnamed const, stock or aux
				// ignore
			} else {
				concept = domain.getConceptByTerm(term);
				if (concept == null) {
					concept = "";
				}
				view.addInfo(usedTerms.get(term)+", "+term+ ", "+concept);
			}
		}
		
	}

}
