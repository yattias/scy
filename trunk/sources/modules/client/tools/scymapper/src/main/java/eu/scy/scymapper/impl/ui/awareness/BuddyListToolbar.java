package eu.scy.scymapper.impl.ui.awareness;

import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.IAwarenessUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;

/**
 * User: Bjoerge Naess
 * Date: 02.sep.2009
 * Time: 17:31:06
 */
public class BuddyListToolbar extends JToolBar implements ActionListener {

	private JList list;
	private IAwarenessService awarenessService;
	private final JButton addButton = new JButton("Add");
	private final JButton removeButton = new JButton("Remove");
	private final static Logger logger = Logger.getLogger(BuddyListToolbar.class);

	public BuddyListToolbar(JList list, IAwarenessService awarenessService) {
		this.list = list;
		this.awarenessService = awarenessService;
		add(addButton);
		add(removeButton);

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
	}
}
