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
public class StartUpInformationItem extends JPanel {
    private final JLabel heading = new JLabel();
    private SCYPlannerTextArea description = new SCYPlannerTextArea();

    public StartUpInformationItem(String title, String descriptionText, ImageIcon icon) {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(2, 2, 22, 2));
        heading.setFont(StartupMenuItem.DEFAULT_STARTUP_PANEL_FONT);
        if (title != null) {
            heading.setText("<html>" + title + "</html>");
            heading.setForeground(Color.BLACK);
        } else {
            heading.setText("null");
        }

        setOpaque(false);

        JPanel nameAndDescriptionPanel = new JPanel();
        nameAndDescriptionPanel.setLayout(new BorderLayout());
        nameAndDescriptionPanel.setOpaque(false);

        description.setEditable(false);
        description.setOpaque(false);
        description.setFocusable(false);
        if (descriptionText != null) {
            description.setText(descriptionText);
        } else {
            description.setText("null");
        }

        nameAndDescriptionPanel.add(BorderLayout.NORTH, heading);
        nameAndDescriptionPanel.add(BorderLayout.CENTER, description);
        add(BorderLayout.CENTER, nameAndDescriptionPanel);
        add(BorderLayout.WEST, new JLabel(icon));
    }
}