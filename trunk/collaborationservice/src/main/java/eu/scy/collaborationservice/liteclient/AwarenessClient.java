package eu.scy.collaborationservice.liteclient;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import eu.scy.collaborationservice.CollaborationService;
import eu.scy.collaborationservice.CollaborationServiceClientInterface;
import eu.scy.core.model.impl.ScyBaseObject;

public class AwarenessClient extends JFrame implements
		CollaborationServiceClientInterface {

	private final static Logger logger = Logger.getLogger(AwarenessClient.class
			.getName());
	private static final String HARD_CODED_TOOL_NAME = "Spiffy Awareness Client";
	private static final long LOGIN_KEEPALIVE_DURATION = 1 * 1000;
	// private static final String[] COLUMN_NAMES = { "username", "status",
	// "doc id"};

	private JTextArea textArea;
	private JPanel panel;
	private JScrollPane scrollPane;
	private HashMap<String, Object> toolsToWatch = new HashMap<String, Object>();
	private String userName;
	private String loginId;

	private CollaborationService cs;

	private Timer loginTimer;
	private Timer buddyTimer;

	private TableModel tableModel;

	private HashMap<String, Boolean> usersToWatch = new HashMap<String, Boolean>();

	public AwarenessClient() {
	}

	public AwarenessClient(String userName, String toolName) {
		logger.debug("Awareness is upon you.");

		userName = userName;
		toolsToWatch.put(toolName, toolName);

		// ac.usersToWatch = new ArrayList<String>(Arrays.asList(new
		// String[]{"jeremyt", "olesm", "janad"})); //this list will be
		// populated by usermanagement

		cs = CollaborationService.createCollaborationService(userName,
				CollaborationService.AWARENESS_SERVICE_SPACE, this);
		signUp();

		// Set the frame characteristics
		setTitle("Awereness client makes " + userName + " happy");
		setSize(300, 500);

		textArea = new JTextArea(15, 80);
		textArea.setEditable(false);
		textArea.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		textArea.setFont(new Font("monospaced", Font.PLAIN, 14));
		JScrollPane scrollingText = new JScrollPane(textArea);

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(scrollingText, BorderLayout.CENTER);

		getContentPane().add(panel);
		setLocationRelativeTo(null);
		setLocation(getLocation().x + 200, getLocation().y + 200);
		setVisible(true);

		loginTimer = new Timer();
		loginTimer.schedule(new LoginTimer(), 1, LOGIN_KEEPALIVE_DURATION);
	}

	private void generateBuddyList() {
		// logger.debug("SAC refreshes buddylist every " +
		// LOGIN_KEEPALIVE_DURATION);
		String outputToTextArea = "";
		String status;
		for (String user : usersToWatch.keySet()) {
			if (cs.read(user, HARD_CODED_TOOL_NAME) != null) {
				status = "online";
			} else {
				status = "offline";
			}
			outputToTextArea = outputToTextArea + user + "\t" + status + "\n";
		}
		textArea.setText(outputToTextArea);
	}

	private class LoginTimer extends TimerTask {
		public void run() {
			signOff();
			signUp();
		}
	}

	private void refreshBuddyList() {
		String outputToTextArea = "";
		String status;
		for (String user : usersToWatch.keySet()) {
			if (usersToWatch.get(user)) {
				status = "online";
			} else {
				status = "offline";
			}
			outputToTextArea = outputToTextArea + user + "\t" + status + "\n";
		}
		final String text = outputToTextArea;
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				textArea.setText(text);
			}
		});

	}

	private void signUp() {
		logger.debug("SAC signing up");
		ScyBaseObject sbo = new ScyBaseObject();
		sbo.setId("");
		sbo.setName("");
		sbo.setDescription("");
		this.loginId = cs.write(HARD_CODED_TOOL_NAME, sbo,
				LOGIN_KEEPALIVE_DURATION + 2000);
	}

	private void signOff() {
		logger.debug("SAC signing off");
		cs.takeById(this.loginId);
	}

	public void shutDown() {
		setVisible(false);
		dispose();
	}

	public void actionUponDelete(String username) {
		this.usersToWatch.put(username, false);
		refreshBuddyList();
	}

	public void actionUponWrite(String username) {
		this.usersToWatch.put(username, true);
		refreshBuddyList();
	}
}
