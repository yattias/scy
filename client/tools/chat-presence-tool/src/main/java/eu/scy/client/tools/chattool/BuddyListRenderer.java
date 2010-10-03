package eu.scy.client.tools.chattool;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import eu.scy.awareness.IAwarenessUser;
import eu.scy.presence.IPresenceEvent;

public class BuddyListRenderer extends JLabel implements ListCellRenderer {

	ImageIcon availIcon = new ImageIcon(this.getClass().getResource("avail-icon.png"));
	ImageIcon awayIcon = new ImageIcon(this.getClass().getResource("away-icon.png"));
	ImageIcon waitIcon = new ImageIcon(this.getClass().getResource("wait-icon.png"));
	
	public BuddyListRenderer() {
		this.setVerticalTextPosition(JLabel.BOTTOM);
		this.setHorizontalTextPosition(JLabel.CENTER);
	}
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		
		IAwarenessUser user = (IAwarenessUser) value;
		

		if( user.getPresence() != null) {
			if( user.getPresence().equals(IPresenceEvent.AVAILABLE)) {
				this.setIcon(availIcon);
			} else if( user.getPresence().equals(IPresenceEvent.UNAVAILABLE)){
				this.setIcon(awayIcon);
			} else if( user.getPresence().equals(IPresenceEvent.WAITING)){
				this.setIcon(waitIcon);
			}
			
			this.setToolTipText(user.getPresence());
		} else {
			this.setIcon(awayIcon);
			this.setToolTipText("status not available");
		}
		this.setText(user.getNickName());
		return this;
	}

}
