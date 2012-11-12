package eu.scy.client.tools.scydynamics.logging.parser.actions;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashSet;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import java.util.Calendar;

import com.toedter.calendar.JDateChooser;

import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.client.tools.scydynamics.domain.Domain;
import eu.scy.client.tools.scydynamics.logging.parser.ParserModel;
import eu.scy.client.tools.scydynamics.logging.parser.ParserView;
import eu.scy.client.tools.scydynamics.logging.parser.UserModel;
import eu.scy.client.tools.scydynamics.ui.VerticalLayout;

@SuppressWarnings("serial")
public class FilterMissionAction extends AbstractAction implements ActionListener {

	private static JDateChooser endDateChooser;
	private static SpinnerDateModel endTimeModel;
	private static JDateChooser startDateChooser;
	private static SpinnerDateModel startTimeModel;
	private ParserView view;
	private ParserModel model;
	private Domain domain;
	private JDialog dialog;
	private JPanel verticalPanel;

	public FilterMissionAction(ParserView view, ParserModel model, Domain domain) {
		putValue(Action.NAME, "filter mission");
		this.view = view;
		this.model = model;
		this.domain = domain;
	}
	
	private void showDialog() {
		dialog = new JDialog();
		dialog.setTitle("filter action per mission");
		dialog.setModal(true);
		dialog.getContentPane().setLayout(new BorderLayout());

		//dialog.getContentPane().add(buildStartDatePanel("start time", new Date()), BorderLayout.WEST);
		//dialog.getContentPane().add(buildEndDatePanel("stop time", new Date()), BorderLayout.EAST);
		verticalPanel = new JPanel();
		verticalPanel.setLayout(new VerticalLayout());
		for (String mission: getAllMissions()) {
			JCheckBox checkBox = new JCheckBox(mission);
			checkBox.setName(mission);
			checkBox.setSelected(true);
			verticalPanel.add(checkBox);
		}
		dialog.getContentPane().add(verticalPanel, BorderLayout.CENTER);
		
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
	
	private HashSet<String> getAllMissions() {
		HashSet<String> missionSet = new HashSet<String>();
		for (UserModel userModel: model.getUserModels().values()) {
			for (IAction action: userModel.getActions()) {
				missionSet.add(action.getContext(ContextConstants.mission));
			}
		}
		return missionSet;
	}
	
	
	private HashSet<String> getSelectedMissions() {
		HashSet<String> selectedMissions = new HashSet<String>();
		for (Component comp: verticalPanel.getComponents()) {
			if (((JCheckBox)comp).isSelected()) {
				selectedMissions.add(comp.getName());
			}
		}
		return selectedMissions;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			
			model.filterForMissions(getSelectedMissions());
			view.updateView();
			view.addInfo("");
			
			view.addInfo("filtered for missions: "+getSelectedMissions().toString());
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
