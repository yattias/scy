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
public class FilterTimeAction extends AbstractAction implements ActionListener {

	private static JDateChooser endDateChooser;
	private static SpinnerDateModel endTimeModel;
	private static JDateChooser startDateChooser;
	private static SpinnerDateModel startTimeModel;
	private ParserView view;
	private ParserModel model;
	private Domain domain;
	private JDialog dialog;

	public FilterTimeAction(ParserView view, ParserModel model, Domain domain) {
		putValue(Action.NAME, "filter time");
		this.view = view;
		this.model = model;
		this.domain = domain;
	}
	
	private void showDialog() {
		dialog = new JDialog();
		dialog.setTitle("filter action per time");
		dialog.setModal(true);
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.getContentPane().add(buildStartDatePanel("start time", new Date()), BorderLayout.WEST);
		dialog.getContentPane().add(buildEndDatePanel("stop time", new Date()), BorderLayout.EAST);
		JPanel buttonPanel = new JPanel();
		FlowLayout flow = new FlowLayout();
		flow.setAlignment(FlowLayout.CENTER);
		buttonPanel.setLayout(flow);
		JButton button = new JButton("OK");
		button.setActionCommand("ok");
		button.addActionListener(this);
		buttonPanel.add(button);
		button = new JButton("CANCEL");
		button.setActionCommand("cancel");
		button.addActionListener(this);
		buttonPanel.add(button);
		dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		dialog.pack();
		dialog.setLocationRelativeTo(view);
		dialog.setVisible(true);
	}
	
	public static JPanel buildStartDatePanel(String label, Date value) {
		JPanel datePanel = new JPanel();
		datePanel.setBorder(BorderFactory.createTitledBorder(label));
		
		startDateChooser = new JDateChooser();
		if (value != null) {
			startDateChooser.setDate(value);
		}
		for (Component comp : startDateChooser.getComponents()) {
			if (comp instanceof JTextField) {
				((JTextField) comp).setColumns(50);
				((JTextField) comp).setEditable(false);
			}
		}
		datePanel.add(startDateChooser);
		startTimeModel = new SpinnerDateModel();
		JSpinner timeSpinner = new JSpinner(startTimeModel);
		JSpinner.DateEditor editor = new JSpinner.DateEditor(timeSpinner, "HH:mm:ss");
		timeSpinner.setEditor(editor);
		if (value != null) {
			timeSpinner.setValue(value);
		}
		datePanel.add(timeSpinner);
		return datePanel;
	}
	
	public static JPanel buildEndDatePanel(String label, Date value) {
		JPanel datePanel = new JPanel();
		datePanel.setBorder(BorderFactory.createTitledBorder(label));
		
		endDateChooser = new JDateChooser();
		if (value != null) {
			endDateChooser.setDate(value);
		}
		for (Component comp : endDateChooser.getComponents()) {
			if (comp instanceof JTextField) {
				((JTextField) comp).setColumns(50);
				((JTextField) comp).setEditable(false);
			}
		}
		datePanel.add(endDateChooser);
		endTimeModel = new SpinnerDateModel();
		JSpinner timeSpinner = new JSpinner(endTimeModel);
		JSpinner.DateEditor editor = new JSpinner.DateEditor(timeSpinner, "HH:mm:ss");
		timeSpinner.setEditor(editor);
		if (value != null) {
			timeSpinner.setValue(value);
		}
		datePanel.add(timeSpinner);
		return datePanel;
	}
	
	private long getTimeInMillis(SpinnerDateModel timeModel, JDateChooser dateChooser) {
		Calendar timeCalendar = Calendar.getInstance();
		timeCalendar.setTime(timeModel.getDate());
		Calendar dateCalender = Calendar.getInstance();
		dateCalender.setTime(dateChooser.getDate());
		dateCalender.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
		dateCalender.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
		return dateCalender.getTimeInMillis();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			
			long startTimestamp = getTimeInMillis(startTimeModel, startDateChooser);
			long stopTimestamp = getTimeInMillis(endTimeModel, endDateChooser);
			
			model.filterForTime(startTimestamp, stopTimestamp);
			view.updateView();
			view.addInfo("");
			
			Date startDate = new Date(startTimestamp);
			Date stopDate = new Date(stopTimestamp);
			view.addInfo("filtered for time - start: "+startDate.toString()+" | stop: "+stopDate.toString());
			view.addInfo(model.getUserModels().size()+" users with "+model.getActions().size()+" actions remained.");
			
			dialog.setVisible(false);
			dialog.dispose();
		}  else if (e.getActionCommand().equals("cancel")) {
			dialog.setVisible(false);
			dialog.dispose();
		} else {
			showDialog();
		}

	}
}
