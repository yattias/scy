package eu.scy.client.tools.scydynamics.logging.parser.actions;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import java.util.Calendar;

import com.toedter.calendar.JDateChooser;

import eu.scy.client.tools.scydynamics.domain.Domain;
import eu.scy.client.tools.scydynamics.logging.parser.ParserModel;
import eu.scy.client.tools.scydynamics.logging.parser.ParserView;

@SuppressWarnings("serial")
public class ReleaseFilterAction extends AbstractAction implements ActionListener {

	private ParserView view;
	private ParserModel model;
	private Domain domain;

	public ReleaseFilterAction(ParserView view, ParserModel model, Domain domain) {
		putValue(Action.NAME, "release filter");
		this.view = view;
		this.model = model;
		this.domain = domain;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		model.releaseFilters();
		view.updateView();
		view.addInfo("");
		view.addInfo("filters released:");
    	view.addInfo(model.getUserModels().size()+" users with "+model.getActions().size()+" actions.");
	}
}
