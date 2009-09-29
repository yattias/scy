package eu.scy.scyplanner.components.frontpage;

import eu.scy.scyplanner.application.SCYPlannerApplicationManager;
import eu.scy.scyplanner.components.text.SCYPlannerTextArea;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 25.sep.2009
 * Time: 09:51:27
 */
public class StartUpMenuItem extends JPanel {
    public final static Font DEFAULT_STARTUP_PANEL_FONT = new Font("Arial", Font.BOLD, 16);

    private final JLabel heading = new JLabel();
    private SCYPlannerTextArea description = new SCYPlannerTextArea();

    public StartUpMenuItem(final Action action, ImageIcon icon) {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(2, 2, 22, 2));
        heading.setFont(DEFAULT_STARTUP_PANEL_FONT);
        if (action != null) {
            heading.setText("<html><u>" + String.valueOf(action.getValue(Action.NAME) + "</u></html>"));
            heading.setForeground(SCYPlannerApplicationManager.getLinkColor());
        } else {
            heading.setText("null");
        }

        heading.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (action.isEnabled()) {
                    action.actionPerformed(new ActionEvent(heading, 11, (String) action.getValue(Action.NAME)));
                }
            }
        });

        setOpaque(false);

        JPanel nameAndDescriptionPanel = new JPanel();
        nameAndDescriptionPanel.setLayout(new BorderLayout());
        nameAndDescriptionPanel.setOpaque(false);

        description.setEditable(false);
        description.setOpaque(false);
        description.setFocusable(false);
        if (action != null) {
            description.setText((String) action.getValue(Action.SHORT_DESCRIPTION));
        } else {
            description.setText("null");
        }

        nameAndDescriptionPanel.add(BorderLayout.NORTH, heading);
        nameAndDescriptionPanel.add(BorderLayout.CENTER, description);
        add(BorderLayout.CENTER, nameAndDescriptionPanel);
        add(BorderLayout.WEST, new JLabel(icon));
    }
}