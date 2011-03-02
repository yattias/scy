package eu.scy.client.tools.chattool;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import eu.scy.awareness.IAwarenessUser;

public class BuddyListRenderer extends JLabel implements ListCellRenderer {

	ImageIcon availIcon = new ImageIcon(this.getClass().getResource("avail-icon.png"));
	ImageIcon awayIcon = new ImageIcon(this.getClass().getResource("away-icon.png"));
	
	public BuddyListRenderer() {
		this.setVerticalTextPosition(JLabel.BOTTOM);
		this.setHorizontalTextPosition(JLabel.CENTER);
	}
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		
		IAwarenessUser user = (IAwarenessUser) value;
		
		if( user.getPresence().equals("available")) {
			this.setIcon(availIcon);
		} else {
			this.setIcon(awayIcon);
		}
		
		if( user.getPresence() != null) {
			this.setToolTipText(user.getPresence());
		} else {
			this.setToolTipText("status not available");
		}
		this.setText(user.getNickName());
		return this;
	}

}