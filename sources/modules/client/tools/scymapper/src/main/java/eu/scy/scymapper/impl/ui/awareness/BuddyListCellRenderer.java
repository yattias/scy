package eu.scy.scymapper.impl.ui.awareness;

import eu.scy.awareness.IAwarenessUser;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.builder.PanelBuilder;
import org.xmpp.packet.Presence;

/**
 * User: Bjoerge Naess
 * Date: 02.sep.2009
 * Time: 17:30:42
 */
public class BuddyListCellRenderer extends JPanel implements ListCellRenderer {
	private JLabel userNameLabel;
	private JLabel realNameLabel;
	private JPanel rootPanel;

	public BuddyListCellRenderer() {
		setOpaque(false);
		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder());

		userNameLabel = new JLabel();
		realNameLabel = new JLabel();
		FormLayout layout = new FormLayout(
				"default:grow",
				"pref, 2dlu, pref"
		);

		PanelBuilder builder = new PanelBuilder(layout);
		CellConstraints cc = new CellConstraints();
		builder.add(userNameLabel, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.FILL));
		builder.add(realNameLabel, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.FILL));

		rootPanel = builder.getPanel();
		add(rootPanel);

	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		IAwarenessUser buddy = (IAwarenessUser) value;
		rootPanel.setBackground(isSelected ? Color.getColor("#e0e0e0") : null);
		rootPanel.setOpaque(isSelected);

		if (buddy.getPresence().equals(Presence.Type.unavailable.toString()))
			userNameLabel.setForeground(Color.gray);
		else
			userNameLabel.setForeground(new Color(0x33aa33));

		userNameLabel.setText(buddy.getUsername()+ " ("+buddy.getPresence()+")");
		//TODO: realNameLabel.setText(buddy.getName());
		return this;
	}
}
