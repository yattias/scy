package eu.scy.scymapper.impl.ui.awareness;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.scy.awareness.IAwarenessUser;
import org.xmpp.packet.Presence;

import javax.swing.*;
import java.awt.*;

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
		rootPanel.setOpaque(isSelected);
		rootPanel.setBackground(isSelected ? new Color(0xB9CFD6) : null);

		if (buddy.getPresence().equals(Presence.Type.unavailable.toString()))
			userNameLabel.setForeground(Color.gray);
		else
			userNameLabel.setForeground(new Color(0x126000));

		userNameLabel.setText(buddy.getUsername()+ " ("+buddy.getPresence()+")");
		//TODO: realNameLabel.setText(buddy.getName());
		return this;
	}
}
