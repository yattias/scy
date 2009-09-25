package eu.scy.scyplanner.components.frontpage;

import eu.scy.scyplanner.components.text.SCYPlannerTextArea;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Oyvind Meistad
 * Date: 25.sep.2009
 * Time: 09:51:27
 */
public class StartUpInformationItem extends JPanel {
    private final JLabel heading = new JLabel();
    private SCYPlannerTextArea description = new SCYPlannerTextArea();
    private final static String EMPTY_STRING = "";

    public StartUpInformationItem(String title, String descritpion, ImageIcon icon) {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(2, 2, 22, 2));
        heading.setFont(StartUpMenuItem.DEFAULT_STARTUP_PANEL_FONT);
        setTitle(title);

        setOpaque(false);

        JPanel nameAndDescriptionPanel = new JPanel();
        nameAndDescriptionPanel.setLayout(new BorderLayout());
        nameAndDescriptionPanel.setOpaque(false);

        description.setEditable(false);
        description.setOpaque(false);
        description.setFocusable(false);
        setDescription(descritpion);

        nameAndDescriptionPanel.add(BorderLayout.NORTH, heading);
        nameAndDescriptionPanel.add(BorderLayout.CENTER, description);
        add(BorderLayout.CENTER, nameAndDescriptionPanel);
        add(BorderLayout.WEST, new JLabel(icon));
    }

    public void setTitle(String title) {
        if (title != null) {
            heading.setText("<html>" + title + "</html>");
            heading.setForeground(Color.BLACK);
        } else {
            heading.setText(EMPTY_STRING);
        }
    }

    public void setDescription(String descritpion) {
        if (descritpion != null) {
            description.setText(descritpion);
        } else {
            description.setText(EMPTY_STRING);
        }
    }
}