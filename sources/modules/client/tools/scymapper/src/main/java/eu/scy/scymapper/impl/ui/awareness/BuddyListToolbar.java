package eu.scy.scymapper.impl.ui.awareness;

import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.packet.Presence;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Bjoerge Naess
 * Date: 02.sep.2009
 * Time: 17:31:06
 */
public class BuddyListToolbar extends JToolBar implements ActionListener, ListSelectionListener {

	private JList list;
	private IAwarenessService awarenessService;
	private final JButton addButton = new JButton(new ImageIcon(getClass().getResource("add_user.png")));
	private final JButton removeButton = new JButton(new ImageIcon(getClass().getResource("remove_user.png")));
	private final JButton collabButton = new JButton(new ImageIcon(getClass().getResource("collaborate.png")));
	private final static Logger logger = Logger.getLogger(BuddyListToolbar.class);

	public BuddyListToolbar(JList list, IAwarenessService awarenessService) {
		this.list = list;
		this.awarenessService = awarenessService;

		collabButton.setEnabled(false);

		addButton.setText("Add");

		list.addListSelectionListener(this);

		add(addButton);
		//add(removeButton);
		//add(collabButton);

		addButton.addActionListener(this);
		removeButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(addButton)) {
			String username = JOptionPane.showInputDialog("Enter username");
			if (username == null || username.trim().equals("")) return;

			try {
				awarenessService.addBuddy(username);
			} catch (AwarenessServiceException e) {
				JOptionPane.showMessageDialog(null, "Error adding buddy", e.getMessage(), JOptionPane.ERROR_MESSAGE);
			}
		}
		if (event.getSource().equals(removeButton)) {

			Object selected = list.getSelectedValue();

			logger.info("User selected buddy: "+selected);

			if (selected == null || !(selected instanceof IAwarenessUser)) return;

			IAwarenessUser buddy = (IAwarenessUser) selected;

			int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove "+buddy.getName()+" as buddy?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (answer == JOptionPane.YES_OPTION) {
				try {
					logger.debug("Removing buddy "+buddy.getUsername());
					awarenessService.removeBuddy(buddy.getUsername());
				} catch (AwarenessServiceException e) {
					JOptionPane.showMessageDialog(null, "Error adding buddy", e.getMessage(), JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		if (event.getSource().equals(collabButton)) {

			Object selected = list.getSelectedValue();

			if (selected == null || !(selected instanceof IAwarenessUser)) return;

			IAwarenessUser buddy = (IAwarenessUser) selected;

			logger.info("User wants to collaborate with "+buddy);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			collabButton.setEnabled(true);
			for (Object selected : list.getSelectedValues()) {
				if (!(selected instanceof IAwarenessUser)) continue;
				IAwarenessUser buddy = (IAwarenessUser) selected;
				if (!buddy.getPresence().equals(Presence.Mode.available.toString())) {
					collabButton.setEnabled(false);
					break;
				}
			}
		}
	}
}
