package eu.scy.client.tools.scydynamics.logging;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TimedEvent extends TimerTask implements ActionListener {

	private ActionListener listener;
	private String actionCommand;
	private JFrame frame;
	private Timer timer;
	private boolean debug;
	private long millis;

	public TimedEvent(ActionListener listener, String actionCommand, long millis, boolean debug) {
		super();
		this.listener = listener;
		this.actionCommand = actionCommand;
		this.debug = debug;
		this.millis = millis;
		timer = new Timer();
		timer.schedule(this, millis);
		if (debug) {
			createDebugButton();
		}
	}

	public TimedEvent(ActionListener listener, String actionCommand, long millis) {
		this(listener, actionCommand, millis, false);
	}

	public TimedEvent(ActionListener listener, String actionCommand, String millis) {
		this(listener, actionCommand, Long.parseLong(millis), false);
	}

	private void createDebugButton() {
		frame = new JFrame();
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JButton button = new JButton("go");
		button.setActionCommand("go");
		button.addActionListener(this);
		panel.add(new JLabel("wait " + millis / 1000 + " seconds for '" + actionCommand + "' or "));
		panel.add(button);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);
	}

	@Override
	public void run() {
		ActionEvent e = new ActionEvent(this, -1, actionCommand);
		listener.actionPerformed(e);
		if (debug) {
			frame.dispose();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("go")) {
			timer.cancel();
			run();
		}
	}

}
