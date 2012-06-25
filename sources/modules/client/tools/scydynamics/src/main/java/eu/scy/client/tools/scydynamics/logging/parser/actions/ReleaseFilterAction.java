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

	private static JDateChooser endDateChooser;
	private static SpinnerDateModel endTimeModel;
	private static JDateChooser startDateChooser;
	private static SpinnerDateModel startTimeModel;
	private ParserView view;
	private ParserModel model;
	private Domain domain;
	private JDialog dialog;

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
	}
}
