package eu.scy.client.tools.scydynamics.logging.parser;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import eu.scy.client.tools.scydynamics.logging.ModellingLoggerFES;
import eu.scy.client.tools.scydynamics.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ParserView extends JFrame {

	private JLabel folderLabel;
	private ParserModel model;
	private JButton folderButton;
	protected JButton goButton;
	protected JProgressBar progressBar;
	private JDialog progressDialog;
	private JTextArea textArea;
	private JButton notRecognizedButton;
	private JButton notRecognizedProposalButton;
	protected JButton statisticsButton;
	protected JButton termsButton;
	protected JButton filterTimeButton;
	private UserTimeline userTimeline;
	protected JButton releaseFilterButton;
	protected JButton feedbackButton;
	protected JButton filterMissionButton;
	protected JButton actionMatrixButton;
	protected JButton scoreButton;
	
	public ParserView(ParserModel model) {
		super("log parser");
		this.model = model;
		initUI();
	}
	
	private void initUI() {
		this.getContentPane().setLayout(new BorderLayout());		
		this.getContentPane().add(createNorthPanel(), BorderLayout.NORTH);
		this.getContentPane().add(createCenterPanel(), BorderLayout.CENTER);
		this.getContentPane().add(createWestPanel(), BorderLayout.WEST);
		this.getContentPane().add(createSouthPanel(), BorderLayout.SOUTH);		
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000, 750);
	}

	public void setActionListener(ActionListener listener) {
		folderButton.addActionListener(listener);
		goButton.addActionListener(listener);
		//notRecognizedButton.addActionListener(listener);
		//notRecognizedProposalButton.addActionListener(listener);
		//statisticsButton.addActionListener(listener);
	}
	
	private Component createSouthPanel() {
		userTimeline = new UserTimeline(model);
		return userTimeline;
	}
	
	private void updateUserTimeline() {
		userTimeline.update();
	}
	
	private Component createWestPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new VerticalLayout());

//		notRecognizedButton = new JButton(ModellingLoggerFES.TERM_NOT_RECOGNIZED);
//		notRecognizedButton.setActionCommand(ModellingLoggerFES.TERM_NOT_RECOGNIZED);
//		panel.add(notRecognizedButton);
//		
//		notRecognizedProposalButton = new JButton(ModellingLoggerFES.TERM_NOT_RECOGNIZED_PROPOSALS);
//		notRecognizedProposalButton.setActionCommand(ModellingLoggerFES.TERM_NOT_RECOGNIZED_PROPOSALS);
//		panel.add(notRecognizedProposalButton);
		
		statisticsButton = new JButton("statistics");
		panel.add(statisticsButton);
		
		actionMatrixButton = new JButton();
		panel.add(actionMatrixButton);
		
		termsButton = new JButton();
		panel.add(termsButton);
		
		filterTimeButton = new JButton();
		panel.add(filterTimeButton);
		
		filterMissionButton = new JButton();
		panel.add(filterMissionButton);
		
		releaseFilterButton = new JButton();
		panel.add(releaseFilterButton);
		
		feedbackButton = new JButton();
		panel.add(feedbackButton);
		
		scoreButton = new JButton();
		panel.add(scoreButton);
		return panel;
	}
	
	private Component createCenterPanel() {
		textArea = new JTextArea(20,50);
		JScrollPane scroller = new JScrollPane(textArea);
		return scroller;
	}
	
	private Component createNorthPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		folderLabel = new JLabel("- no folder chosen -");
		panel.add(folderLabel);
		folderButton = new JButton("choose folder");
		folderButton.setActionCommand("folder");
		panel.add(folderButton);
		goButton = new JButton("go!");
		goButton.setActionCommand("go");
		panel.add(goButton);
		return panel;
	}
	
	public void addInfo(String info) {
		textArea.append(info+"\n");
	}

	public void updateView() {
		folderLabel.setText(model.getDirectory());	
		updateUserTimeline();
	}
	
	public void startProgressBar() {
		progressDialog = new JDialog(this, "Please wait while collecting actions.", true);
	    progressBar = new JProgressBar();
	    progressBar.setIndeterminate(true);
	    progressDialog.add(BorderLayout.CENTER, progressBar);
	    progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	    progressDialog.setSize(300, 75);
	    progressDialog.setLocationRelativeTo(this);
	    progressDialog.setVisible(true);
	}
	
	public void stopProgressBar() {
		progressBar.setVisible(false);
		progressDialog.setVisible(false);
		progressDialog.dispose();
	}
	
}
